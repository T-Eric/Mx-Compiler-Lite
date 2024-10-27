import ast.programNode;
import backend.asmPreOptimizer;
import backend.irOptimizer;
import frontend.ASTBuilder;
import frontend.ForwardCollector;
import frontend.SemanticChecker;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
// import midend.asm.asmBuilder;
import midend.llvm_ir.irBuilder;
import midend.neo.neoBuilder;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.MxLexer;
import parser.MxParser;
import utility.MxErrorListener;
import utility.error.*;
import utility.scope.globalScope;

public class sema {
  public static void main(String[] args) throws Exception {
    // run monitors
    boolean run_by_bash = false;
    boolean watchTree = false;
    boolean outToFile = !run_by_bash;
    boolean debug_ir = false;

    if (outToFile) {
      PrintStream ps = new PrintStream(
          new BufferedOutputStream(new FileOutputStream("src/out.ll")), true);
      System.setOut(ps);
    }

    InputStream input;
    if (run_by_bash) {
      input = System.in;
    } else {
      String file = "testcases/codegen/e0.mx";
      input = new FileInputStream(file);
    }

    try {
      globalScope mainScope = new globalScope(null);
      MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
      lexer.removeErrorListeners();
      lexer.addErrorListener(new MxErrorListener());
      MxParser parser = new MxParser(new CommonTokenStream(lexer));
      parser.removeErrorListeners();
      parser.addErrorListener(new MxErrorListener());
      ParseTree parseTreeRoot = parser.program();

      // ParseTree viewer
      if (watchTree) {
        JFrame frame = new JFrame("ASTWatcher");
        JPanel panel = new JPanel();
        TreeViewer viewer =
            new TreeViewer(Arrays.asList(parser.getRuleNames()), parseTreeRoot);
        viewer.setScale(1.5);
        panel.add(viewer);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
      }

      ASTBuilder builder = new ASTBuilder(mainScope);
      programNode ASTRoot = (programNode)builder.visit(parseTreeRoot);
      new ForwardCollector(mainScope).visit(ASTRoot);
      new SemanticChecker(mainScope).visit(ASTRoot);

      var ir = new irBuilder(ASTRoot);
      ir.visit(ir.program);
      ir.world.genIndex();

      // Optimize
      for (var cls : ir.world.classes.values()) {
        if (!cls.builtIn) {
          var opter = new irOptimizer(cls.constructor);
          var preparer = new asmPreOptimizer(cls.constructor);
          opter.ssa();
          preparer.activeAnalysis();
        }
      }

      for (var func : ir.world.functions.values()) {
        if (!func.builtIn) {
          var opter = new irOptimizer(func);
          var preparer = new asmPreOptimizer(func);
          opter.ssa();
          preparer.activeAnalysis();
        }
      }

      String irCode = ir.world.toString();
      if (outToFile)
        System.out.println(irCode);

      if (!debug_ir) {
        if (outToFile) {
          PrintStream ps = new PrintStream(
              new BufferedOutputStream(new FileOutputStream("src/out.s")),
              true);
          System.setOut(ps);
        }

        // var asm = new asmBuilder(ir.world);
        // asm.visitWorld();

        var neo = new neoBuilder(ir.world);
        neo.visitWorld();

        if (!outToFile)
          try (FileInputStream fis = new FileInputStream("builtin.s")) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
              System.out.write(buffer, 0, length);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }

        System.out.println(neo.world.toString());
      }
    } catch (error e) {
      System.out.println(e.toString());
      throw new RuntimeException();
    }
  }
}

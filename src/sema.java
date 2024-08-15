import ast.programNode;
import frontend.ASTBuilder;
import frontend.ForwardCollector;
import frontend.SemanticChecker;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
    boolean run_by_bash = true;
    boolean watchTree = false;
    boolean outToFile = false;

    if (outToFile) {
      PrintStream ps = new PrintStream(
          new BufferedOutputStream(new FileOutputStream("src/out.txt")), true);
      System.setOut(ps);
    }

    InputStream input;
    if (run_by_bash) {
      input = System.in;
    } else {
      String file = "testcases/sema/basic-package/basic-8.mx";
      // testcases/sema/basic-package/basic-8.mx has sth wrong with oj test
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
      System.out.println("Successful!");
    } catch (error e) {
      System.out.println(e.toString());
      throw new RuntimeException();
    }
  }
}

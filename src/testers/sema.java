package testers;

import ast.programNode;
import frontend.ASTBuilder;
import frontend.ForwardCollector;
import frontend.SemanticChecker;
import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import parser.MxLexer;
import parser.MxParser;
import utility.MxErrorListener;
import utility.error.*;
import utility.scope.globalScope;

public class sema {
  public static void main(String[] args) throws Exception {
    String file = "src/test.mx";
    InputStream input = new FileInputStream(file);

    try {
      globalScope mainScope = new globalScope(null);
      MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
      lexer.removeErrorListeners();
      ;
      lexer.addErrorListener(new MxErrorListener());
      MxParser parser = new MxParser(new CommonTokenStream(lexer));
      parser.removeErrorListeners();
      parser.addErrorListener(new MxErrorListener());
      ParseTree parseTreeRoot = parser.program();
      ASTBuilder builder = new ASTBuilder(mainScope);
      programNode ASTRoot = (programNode)builder.visit(parseTreeRoot);
      new ForwardCollector(mainScope).visit(ASTRoot);
      new SemanticChecker(mainScope).visit(ASTRoot);
      System.out.println("Successful!");
    } catch (error e) {
      System.err.println(e.toString());
      throw new RuntimeException();
    }
  }
}

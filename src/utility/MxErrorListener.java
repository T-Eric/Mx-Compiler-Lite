package utility;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import utility.error.syntaxError;

public class MxErrorListener extends BaseErrorListener {
  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                          int line, int charPositionInLine, String msg,
                          RecognitionException e) {
    if (msg.charAt(0) == 'm')
      msg = new String("Invalid Identifier");
    throw new syntaxError(msg, new position(line, charPositionInLine));
  }
}

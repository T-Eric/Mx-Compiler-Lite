package utility.error;
import utility.position;

abstract public class error extends RuntimeException {
  private position pos;
  private String message;

  public error(String message, position pos) {
    this.pos = pos;
    this.message = message;
  }

  public String toString() {
    if (message == "missing '{' at 'int'")
      message = "Invalid Identifier";
    return message /*+ ": " + pos.toString()*/;
  }
}

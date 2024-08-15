package utility.error;
import utility.position;

abstract public class error extends RuntimeException {
  private position pos;
  private String message;
  boolean in_oj = true;

  public error(String message, position pos) {
    this.pos = pos;
    this.message = message;
  }

  public String toString() {
    if (in_oj)
      return message;
    return message + ": " + pos.toString();
  }
}

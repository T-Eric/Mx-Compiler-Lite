package utility.error;
import utility.position;

public class syntaxError extends error {
  public syntaxError(String msg, position pos) {
    super(msg, pos);
  }
}

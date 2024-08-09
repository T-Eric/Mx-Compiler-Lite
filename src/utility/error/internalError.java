package utility.error;
import utility.position;

public class internalError extends error {
  public internalError(String msg, position pos) {
    super("Internal Error: " + msg, pos);
  }
}

package utility.error;
import utility.position;

public class semanticError extends error {
  public semanticError(String msg, position pos) {
    super(msg, pos);
  }
}

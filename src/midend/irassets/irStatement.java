package midend.irassets;

public abstract class irStatement {
  // generate the index of the values
  public abstract void genIndex();

  // generate the code
  public abstract String toString();
}

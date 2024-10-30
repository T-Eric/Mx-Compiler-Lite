package backend;

import java.util.HashMap;
import java.util.HashSet;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.irIns;
import midend.llvm_ir.irassets.statements.irWorld;

public class conster {
  public irWorld world;
  HashSet<irIns> worklist=new HashSet<>();
  HashMap<irId,HashSet<irIns>> varis=new HashMap<>();

  public conster(irWorld world){
    
  }
}

package midend.asm.asmassets;

import java.util.ArrayList;

import midend.llvm_ir.irassets.statements.irClass;

public class asmClass {
  // 只有一个用处：告诉一个类指针，其getelementptr汇编地址偏移量是多少
  public irClass theClass;
  public ArrayList<Integer> offsets=new ArrayList<>();

}

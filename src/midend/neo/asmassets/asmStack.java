package midend.neo.asmassets;

import java.util.ArrayList;
import java.util.HashMap;
import midend.neo.asmassets.asmId.AsmType;
import midend.neo.asmassets.asmId.RegName;

public class asmStack {
  // 起点在上，往低地址为生长
  // 是虚拟寄存器的集合，以imm(sp)存取值
  public ArrayList<asmId> lowStack = new ArrayList<>();
  public ArrayList<asmId> highStack = new ArrayList<>(); // 倒序
  public HashMap<RegName, Integer> saveMap = new HashMap<>();
  public HashMap<RegName, Integer> reserveMap = new HashMap<>();
  public int low = 0, high = 0, total = 0,
             saveSum = 0; // 夹住中间空白区域的两端位置

  // size当然是由asmId自行存储，offset此时输入id即可
  public void pushLow(asmId id) {
    assert id.asmType.equals(AsmType.Address);
    assert id.reg.equals(RegName.sp);
    id.offset = low;
    lowStack.add(id);
    low += id.size;
    total += id.size;
  }

  // 暂时不修改offset
  public void pushHigh(asmId id) {
    assert id.asmType.equals(AsmType.Address);
    assert id.reg.equals(RegName.sp);
    highStack.add(id);
    total += id.size;
  }

  public void pushSave(RegName re) {
    // 保存在次高层的s字辈
    saveMap.put(re, ++saveSum);
    total += 4;
  }

  public void pushAllReserve(RegName[] regs) {
    // 保证在pushSave之后做，沿用saveSum
    for (var re : regs) {
      reserveMap.put(re, ++saveSum);
      total += 4;
    }
  }

  public int savePos(RegName re) { return total - high - saveMap.get(re) * 4; }

  public int reservePos(RegName re) {
    return total - high - reserveMap.get(re) * 4;
  }

  public void offSet() {
    total = (total / 16) * 16;
    for (var id : highStack) {
      id.offset = total - high - 4;
      high += 4; // 比如total=32，那么第一个值在28处
    }
  }
}

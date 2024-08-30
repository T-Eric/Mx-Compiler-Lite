package midend.asm.asmassets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import org.antlr.v4.runtime.misc.Pair;

public class asmStack {
  // 起点在上，往低地址为生长
  // 是虚拟寄存器的集合，以imm(sp)存取值
  public ArrayList<asmId> lowStack = new ArrayList<>();
  public Stack<Pair<asmId, Integer>> highStack = new Stack<>();
  public LinkedList<Integer> trash = new LinkedList<>();
  // the thing, its size
  public int low = 0, high = 0, total = 0; // 夹住中间空白区域的两端位置

  public int push_low(asmId id) {
    int ret;
    if (trash.isEmpty()) {
      ret = low;
      low += 4;
      total += 4;
      lowStack.add(id);

    } else {
      ret=trash.getFirst();
      trash.removeFirst();
    }
    return ret;
  }

  public int push_high(Pair<asmId, Integer> pair) {
    int ret = high;
    high += pair.b;
    total += pair.b;
    highStack.push(pair);
    return ret;
  }

  public void recycle(asmId id) {
    boolean found = false;
    int i = 0;
    for (; i < lowStack.size(); ++i) {
      if (id == lowStack.get(i)) {
        found = true;
        break;
      }
    }
    if (!found)
      return;
    trash.push(i*4);
  }
}

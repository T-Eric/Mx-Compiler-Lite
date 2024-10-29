#include <cstring>
#include <iostream>
using namespace std;

int count = 0;     // 记录总调用次数
int depth = 0;     // 当前调用深度
int maxDepth = 0;  // 记录最大调用深度

int tak(int x, int y, int z) {
  ++count;                          // 增加调用次数
  ++depth;                          // 进入新的调用，增加深度
  maxDepth = max(maxDepth, depth);  // 更新最大深度

  int result;
  if (y < x) {
    result = 1 + tak(tak(x - 1, y, z), tak(y - 1, z, x), tak(z - 1, x, y));
  } else {
    result = z;
  }

  --depth;  // 完成当前调用，减少深度
  return result;
}

int main() {
  cout << tak(18, 12, 6) << " " << count << " " << maxDepth << endl;
  return 0;
}
// #include <algorithm>
// #include <cstring>
// #include <iostream>
// #include <queue>
// #include <unordered_set>
// #include <vector>
// using namespace std;

// struct Block {
//   int id;
//   vector<int> pres;
//   int parent;// for dfs
//   int idom,sdom;

// };

#include <bits/stdc++.h>
using namespace std;
const int N = 200005, M = 300005;
int n, m, sdom[N], idom[N], df[N], rs[N];
int siz[N], fa[N], dfn[N], dot[N];
int nx[M * 2 + N], to[M * 2 + N], hd1[N], hd2[N], hd3[N], ecnt;
// hd1 是原图，hd2 是反图，hd3 是求 idom 时挂标记用的
inline int read() {
  int x = 0;
  char c = getchar();
  while (c < '0' || c > '9')
    c = getchar();
  while (c >= '0' && c <= '9')
    x = (x << 3) + (x << 1) + (c ^ 48), c = getchar();
  return x;
}
inline void add(int u, int v, int* hd) {
  nx[++ecnt] = hd[u], hd[u] = ecnt, to[ecnt] = v;
  // 链式前向星连边，hd[u]存储了编号为u的点在图中讨论边时的编号
}
inline void dfs(int u) {
  dfn[u] = ++*dfn, dot[*dfn] = u;//dfn按点的编号存dfs序，dot按dfs序存点的编号
  for (int i = hd1[u], v; i; i = nx[i])
    if (!dfn[to[i]])
      fa[to[i]] = u, dfs(to[i]);
}  // 建出搜索树，记录 dfs 序
inline int dcmp(int u, int v) {
  return dfn[sdom[u]] < dfn[sdom[v]];
}
inline int Calc(int u) {
  if (df[u] == u)
    return u;
  Calc(df[u]), rs[u] = dcmp(rs[u], rs[df[u]]) ? rs[u] : rs[df[u]];
  return df[u] = df[df[u]], rs[u];
}  // 查询一个点到其所属的并查集的根的 sdom(u) 最小的 u，使用了路径压缩
inline void Solve() {
  for (int i = n; i > 1; --i) {
    int u = dot[i];
    for (int j = hd2[u], v, t; j; j = nx[j])
      v = to[j], t = Calc(v), sdom[u] = dcmp(u, t) ? sdom[u] : sdom[t];
    // 求出 sdom
    add(sdom[u], u, hd3);
    for (int j = hd3[fa[u]], v, t; j; j = nx[j])
      v = to[j], t = Calc(v), idom[v] = (sdom[t] == sdom[v]) ? sdom[v] : t;
    // 求出 idom，不能确定的标记一下和哪个点相同
    df[u] = fa[u], hd3[fa[u]] = 0;
  }
}
int main() {
  n = read(), m = read();
  for (int i = 1, u, v; i <= m; ++i)
    u = read(), v = read(), add(u, v, hd1), add(v, u, hd2);
  for (int i = 1; i <= n; ++i)
    df[i] = rs[i] = sdom[i] = i, siz[i] = 1;
  dfs(1), Solve();
  for (int i = 2; i <= n; ++i) {
    int u = dot[i];
    if (idom[u] != sdom[u])
      idom[u] = idom[idom[u]];
  }  // 按 dfs 序从小到大确定每个点的 idom
  for (int i = n; i > 1; --i)
    siz[idom[dot[i]]] += siz[dot[i]];
  for (int i = 1; i <= n; ++i)
    printf("%d ", siz[i]);
  putchar('\n');
  return 0;
}
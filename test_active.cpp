#include <algorithm>
#include <cstring>
#include <iostream>
#include <queue>
#include <unordered_set>
#include <vector>
using namespace std;

struct Block;

queue<Block*> Q;

struct Block {
  string name;
  unordered_set<string> use, def, in, out;
  vector<Block*> pres, sucs;

  Block() {}
  Block(vector<string> us, vector<string> df) {
    for (auto& s : us)
      use.insert(s);
    for (auto& s : df)
      def.insert(s);
  }
  void addInfo(string name_, vector<Block*> pr, vector<Block*> sc) {
    name = name_;
    for (auto& s : pr)
      pres.push_back(s);
    for (auto& s : sc)
      sucs.push_back(s);
  }
};

void get_difference(unordered_set<string>& s1, unordered_set<string>& s2, unordered_set<string>& res) {
  vector<string> v1(s1.begin(), s1.end()), v2(s2.begin(), s2.end()), vr;
  sort(v1.begin(), v1.end());
  sort(v2.begin(), v2.end());
  set_difference(v1.begin(), v1.end(), v2.begin(), v2.end(), inserter(vr, vr.begin()));
  res.clear();
  res.insert(vr.begin(), vr.end());
}

int main() {
  vector<string> ub1 = {"m", "n", "u1"}, ub2 = {"i", "j"}, ub3 = {"u2"}, ub4 = {"u3"};
  vector<string> db1 = {"i", "j", "a"}, db2 = {}, db3 = {"a"}, db4 = {"i"};
  Block *b1 = new Block(ub1, db1), *b2 = new Block(ub2, db2), *b3 = new Block(ub3, db3), *b4 = new Block(ub4, db4);
  b1->addInfo("B1", {}, {b2});
  b2->addInfo("B2", {b1, b4}, {b3, b4});
  b3->addInfo("B3", {b2}, {b4});
  b4->addInfo("B4", {b2, b3}, {b2});
  Block* blocks[4] = {b1, b2, b3, b4};
  Q.push(b4);
  while (!Q.empty()) {
    Block* cur = Q.front();
    Q.pop();

    printf("Current Block:%s\n", cur->name.c_str());
    printf("Original Out: ");
    for (auto& s : cur->out)
      printf("%s, ", s.c_str());
    printf("\n\n");

    unordered_set<string> minus;
    // set_difference(cur->out.begin(), cur->out.end(), cur->def.begin(), cur->def.end(), inserter(minus, minus.begin()));
    get_difference(cur->out,cur->def,minus);
    set_union(cur->use.begin(), cur->use.end(), minus.begin(), minus.end(), inserter(cur->in, cur->in.begin()));

    printf("New In: ");
    for (auto& s : cur->in)
      printf("%s, ", s.c_str());
    printf("\n\n");

    for (auto& pre : cur->pres) {
      unordered_set<string> result;
      for (auto& suc : pre->sucs)
        set_union(result.begin(), result.end(), suc->in.begin(), suc->in.end(), inserter(result, result.begin()));
      if (result != pre->out) {
        printf("Changing the Out of %s from {", pre->name.c_str());
        for (auto& s : pre->out)
          printf("%s, ", s.c_str());
        printf("} to {");
        for (auto& s : result)
          printf("%s, ", s.c_str());
        printf("}\n");
        pre->out.clear();
        pre->out.insert(result.begin(), result.end());
        Q.push(pre);
      }
    }

    cout << "----------------------------------------------------------------" << endl;
  }

  cout << "Final Out:" << endl;
  for (auto& block : blocks) {
    printf("Block %s\nOut: ", block->name.c_str());
    for (auto& s : block->out)
      printf("%s, ", s.c_str());
    printf("\n");
    printf("In: ");
    for (auto& s : block->in)
      printf("%s, ", s.c_str());
    printf("\n\n");
  }

  for (auto& block : blocks)
    delete block;
}
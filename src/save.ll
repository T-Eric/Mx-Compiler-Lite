define i32 @main() {
0  call void @_init()
1  %8.tmp.0.0 <- 0
2  %2.tmp.0.0 <- 0
3  %3.tmp.0.0 <- 0
4  br label %4

4:
5  %3.phi.0 <- %3.tmp.0.0
6  %2.phi.0 <- %2.tmp.0.0
7  %8.phi.0 <- %8.tmp.0.0
8  %6 = icmp sle i32 %2.phi.0, 29
9  br i1 %6, label %7, label %76

7:
10  %9 = load i8**, i8*** @global.str
11  %11 = getelementptr i8*, i8** %9, i32 %2.phi.0
12  %12 = load i32**, i32*** @global.a
13  %14 = getelementptr i32*, i32** %12, i32 %2.phi.0
14  %15 = load i32*, i32** %14
15  %16 = getelementptr i32, i32* %15, i32 0
16  %17 = load i32, i32* %16
17  %18 = call i8* @_toString(i32 %17)
18  store i8* %18, i8** %11
19  %8.tmp.2.1 <- 0
20  %3.tmp.1.1 <- 0
21  br label %19

19:
22  %3.phi.1 <- %3.tmp.1.1
23  %8.phi.2 <- %8.tmp.2.1
24  %22 = icmp slt i32 %3.phi.1, %2.phi.0
25  br i1 %22, label %23, label %53

23:
26  %25 = and i32 %3.phi.1, 1
27  %26 = icmp eq i32 %25, 0
28  %8.tmp.1.2 <- %8.phi.2
29  br i1 %26, label %27, label %36

27:
30  %29 = load i32**, i32*** @global.a
31  %31 = getelementptr i32*, i32** %29, i32 %2.phi.0
32  %32 = load i32*, i32** %31
33  %33 = getelementptr i32, i32* %32, i32 0
34  %34 = load i32, i32* %33
35  %35 = add i32 %8.phi.2, %34
36  %8.tmp.1.2 <- %35
37  br label %36

36:
38  %8.phi.1 <- %8.tmp.1.2
39  %38 = and i32 %3.phi.1, 1
40  %39 = icmp eq i32 %38, 1
41  %8.tmp.3.3 <- %8.phi.1
42  br i1 %39, label %40, label %50

40:
43  %42 = load i32**, i32*** @global.a
44  %44 = getelementptr i32*, i32** %42, i32 %2.phi.0
45  %45 = load i32*, i32** %44
46  %46 = getelementptr i32, i32* %45, i32 29
47  %47 = load i32, i32* %46
48  %48 = add i32 %8.phi.1, %47
49  %8.tmp.3.3 <- %48
50  br label %50

50:
51  %8.phi.3 <- %8.tmp.3.3
52  %52 = add i32 %3.phi.1, 1
53  %8.tmp.2.1 <- %8.phi.3
54  %3.tmp.1.1 <- %52
55  br label %19

53:
56  %54 = getelementptr [5 x i8], [5 x i8]* @.strconst.0, i32 0, i32 0
57  %55 = getelementptr [5 x i8], [5 x i8]* @.strconst.1, i32 0, i32 0
58  %56 = call i8* @_string_add(i8* %54, i8* %55)
59  %57 = getelementptr [5 x i8], [5 x i8]* @.strconst.2, i32 0, i32 0
60  %58 = call i8* @_string_add(i8* %56, i8* %57)
61  %59 = getelementptr [5 x i8], [5 x i8]* @.strconst.3, i32 0, i32 0
62  %60 = call i8* @_string_add(i8* %58, i8* %59)
63  %61 = getelementptr [5 x i8], [5 x i8]* @.strconst.4, i32 0, i32 0
64  %62 = call i8* @_string_add(i8* %60, i8* %61)
65  %63 = getelementptr [5 x i8], [5 x i8]* @.strconst.5, i32 0, i32 0
66  %64 = call i8* @_string_add(i8* %62, i8* %63)
67  %65 = getelementptr [5 x i8], [5 x i8]* @.strconst.6, i32 0, i32 0
68  %66 = call i8* @_string_add(i8* %64, i8* %65)
69  %67 = getelementptr [5 x i8], [5 x i8]* @.strconst.7, i32 0, i32 0
70  %68 = call i8* @_string_add(i8* %66, i8* %67)
71  %69 = getelementptr [5 x i8], [5 x i8]* @.strconst.8, i32 0, i32 0
72  %70 = call i8* @_string_add(i8* %68, i8* %69)
73  %71 = getelementptr [6 x i8], [6 x i8]* @.strconst.9, i32 0, i32 0
74  %72 = call i8* @_string_add(i8* %70, i8* %71)
75  call void @_println(i8* %72)
76  %75 = add i32 %2.phi.0, 1
77  %8.tmp.0.0 <- %8.phi.2
78  %2.tmp.0.0 <- %75
79  %3.tmp.0.0 <- %3.phi.1
80  br label %4

76:
81  ret i32 0
}

--------

define i32 @main() {
0  call void @_init()
1  br label %4

4:
  %8.phi.0 = phi i32 [ %8.phi.2, %73 ], [ 0, %0 ]
  %2.phi.0 = phi i32 [ %75, %73 ], [ 0, %0 ]
  %3.phi.0 = phi i32 [ %3.phi.1, %73 ], [ 0, %0 ]
2  %6 = icmp sle i32 %2.phi.0, 29
3  br i1 %6, label %7, label %76

7:
4  %9 = load i8**, i8*** @global.str
5  %11 = getelementptr i8*, i8** %9, i32 %2.phi.0
6  %12 = load i32**, i32*** @global.a
7  %14 = getelementptr i32*, i32** %12, i32 %2.phi.0
8  %15 = load i32*, i32** %14
9  %16 = getelementptr i32, i32* %15, i32 0
10  %17 = load i32, i32* %16
11  %18 = call i8* @_toString(i32 %17)
12  store i8* %18, i8** %11
13  br label %19

19:
  %8.phi.2 = phi i32 [ %8.phi.3, %50 ], [ 0, %7 ]
  %3.phi.1 = phi i32 [ %52, %50 ], [ 0, %7 ]
14  %22 = icmp slt i32 %3.phi.1, %2.phi.0
15  br i1 %22, label %23, label %53

23:
16  %25 = and i32 %3.phi.1, 1
17  %26 = icmp eq i32 %25, 0
18  br i1 %26, label %27, label %36

27:
19  %29 = load i32**, i32*** @global.a
20  %31 = getelementptr i32*, i32** %29, i32 %2.phi.0
21  %32 = load i32*, i32** %31
22  %33 = getelementptr i32, i32* %32, i32 0
23  %34 = load i32, i32* %33
24  %35 = add i32 %8.phi.2, %34
25  br label %36

36:
  %8.phi.1 = phi i32 [ %8.phi.2, %23 ], [ %35, %27 ]
26  %38 = and i32 %3.phi.1, 1
27  %39 = icmp eq i32 %38, 1
28  br i1 %39, label %40, label %50

40:
29  %42 = load i32**, i32*** @global.a
30  %44 = getelementptr i32*, i32** %42, i32 %2.phi.0
31  %45 = load i32*, i32** %44
32  %46 = getelementptr i32, i32* %45, i32 29
33  %47 = load i32, i32* %46
34  %48 = add i32 %8.phi.1, %47
35  br label %50

50:
  %8.phi.3 = phi i32 [ %48, %40 ], [ %8.phi.1, %36 ]
36  %52 = add i32 %3.phi.1, 1
37  br label %19

53:
38  %54 = getelementptr [5 x i8], [5 x i8]* @.strconst.0, i32 0, i32 0
39  %55 = getelementptr [5 x i8], [5 x i8]* @.strconst.1, i32 0, i32 0
40  %56 = call i8* @_string_add(i8* %54, i8* %55)
41  %57 = getelementptr [5 x i8], [5 x i8]* @.strconst.2, i32 0, i32 0
42  %58 = call i8* @_string_add(i8* %56, i8* %57)
43  %59 = getelementptr [5 x i8], [5 x i8]* @.strconst.3, i32 0, i32 0
44  %60 = call i8* @_string_add(i8* %58, i8* %59)
45  %61 = getelementptr [5 x i8], [5 x i8]* @.strconst.4, i32 0, i32 0
46  %62 = call i8* @_string_add(i8* %60, i8* %61)
47  %63 = getelementptr [5 x i8], [5 x i8]* @.strconst.5, i32 0, i32 0
48  %64 = call i8* @_string_add(i8* %62, i8* %63)
49  %65 = getelementptr [5 x i8], [5 x i8]* @.strconst.6, i32 0, i32 0
50  %66 = call i8* @_string_add(i8* %64, i8* %65)
51  %67 = getelementptr [5 x i8], [5 x i8]* @.strconst.7, i32 0, i32 0
52  %68 = call i8* @_string_add(i8* %66, i8* %67)
53  %69 = getelementptr [5 x i8], [5 x i8]* @.strconst.8, i32 0, i32 0
54  %70 = call i8* @_string_add(i8* %68, i8* %69)
55  %71 = getelementptr [6 x i8], [6 x i8]* @.strconst.9, i32 0, i32 0
56  %72 = call i8* @_string_add(i8* %70, i8* %71)
57  call void @_println(i8* %72)
58  %75 = add i32 %2.phi.0, 1
59  br label %4

76:
60  ret i32 0
}
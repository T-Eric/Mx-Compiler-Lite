declare i8* @malloc(i32)
declare i8* @_new_array(i32, i32, i32*)
declare void @_print(i8*)
declare void @_println(i8*)
declare void @_printInt(i32)
declare void @_printlnInt(i32)
declare i8* @_getString()
declare i32 @_getInt()
declare i8* @_toString(i32)
declare i8* @_string_add(i8*, i8*)
declare i1 @_string_eq(i8*, i8*)
declare i1 @_string_ne(i8*, i8*)
declare i1 @_string_le(i8*, i8*)
declare i1 @_string_leq(i8*, i8*)
declare i1 @_string_ge(i8*, i8*)
declare i1 @_string_geq(i8*, i8*)
declare i8* @_string_substring(i8*, i32, i32)
declare i32 @_string_parseInt(i8*)
declare i32 @_string_ord(i8*, i32)
declare i8* @_boolToString(i1)

@global.N = global i32 0
@global.row = global i32* null
@global.col = global i32* null
@global.d = global i32** null
@.strconst.0 = private unnamed_addr constant [3 x i8] c" O\00"
@.strconst.1 = private unnamed_addr constant [3 x i8] c" .\00"
@.strconst.2 = private unnamed_addr constant [1 x i8] c"\00"
@.strconst.3 = private unnamed_addr constant [1 x i8] c"\00"




define void @_init() {
-1  store i32 8, i32* @global.N
-1  %1 = mul i32 4, 8
-1  %2 = add i32 %1, 4
-1  %3 = call i8* @malloc(i32 %2)
-1  %4 = bitcast i8* %3 to i32*
-1  store i32 8, i32* %4
-1  %5 = getelementptr i32, i32* %4, i32 1
-1  %6 = bitcast i32* %5 to i32*
-1  store i32* %6, i32** @global.row
-1  %7 = mul i32 4, 8
-1  %8 = add i32 %7, 4
-1  %9 = call i8* @malloc(i32 %8)
-1  %10 = bitcast i8* %9 to i32*
-1  store i32 8, i32* %10
-1  %11 = getelementptr i32, i32* %10, i32 1
-1  %12 = bitcast i32* %11 to i32*
-1  store i32* %12, i32** @global.col
-1  %13 = alloca i32, i32 2
-1  store i32 2, i32* %13
-1  %14 = getelementptr i32, i32* %13, i32 1
-1  store i32 0, i32* %14
-1  %15 = call i8* @_new_array(i32 4, i32 2, i32* %13)
-1  %16 = bitcast i8* %15 to i32**
-1  store i32** %16, i32*** @global.d
-1  ret void
}

define void @global.printBoard() {
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  store i32 0, i32* %1
-1  br label %3

3:
-1  %4 = load i32, i32* %1
-1  %5 = load i32, i32* @global.N
-1  %6 = icmp slt i32 %4, %5
-1  br i1 %6, label %7, label %32

7:
-1  store i32 0, i32* %2
-1  br label %8

8:
-1  %9 = load i32, i32* %2
-1  %10 = load i32, i32* @global.N
-1  %11 = icmp slt i32 %9, %10
-1  br i1 %11, label %12, label %27

12:
-1  %13 = load i32*, i32** @global.col
-1  %14 = load i32, i32* %1
-1  %15 = getelementptr i32, i32* %13, i32 %14
-1  %16 = load i32, i32* %15
-1  %17 = load i32, i32* %2
-1  %18 = icmp eq i32 %16, %17
-1  br i1 %18, label %19, label %21

19:
-1  %20 = getelementptr [3 x i8], [3 x i8]* @.strconst.0, i32 0, i32 0
-1  call void @_print(i8* %20)
-1  br label %23

21:
-1  %22 = getelementptr [3 x i8], [3 x i8]* @.strconst.1, i32 0, i32 0
-1  call void @_print(i8* %22)
-1  br label %23

23:
-1  br label %24

24:
-1  %25 = load i32, i32* %2
-1  %26 = add i32 %25, 1
-1  store i32 %26, i32* %2
-1  br label %8

27:
-1  %28 = getelementptr [1 x i8], [1 x i8]* @.strconst.2, i32 0, i32 0
-1  call void @_println(i8* %28)
-1  br label %29

29:
-1  %30 = load i32, i32* %1
-1  %31 = add i32 %30, 1
-1  store i32 %31, i32* %1
-1  br label %3

32:
-1  %33 = getelementptr [1 x i8], [1 x i8]* @.strconst.3, i32 0, i32 0
-1  call void @_println(i8* %33)
-1  br label %34

34:
-1  ret void
}

define void @global.search(i32 %0) {
-1  %2 = alloca i32
-1  store i32 %0, i32* %2
-1  %3 = load i32, i32* %2
-1  %4 = load i32, i32* @global.N
-1  %5 = icmp eq i32 %3, %4
-1  br i1 %5, label %6, label %7

6:
-1  call void @global.printBoard()
-1  br label %97

7:
-1  %8 = alloca i32
-1  store i32 0, i32* %8
-1  store i32 0, i32* %8
-1  br label %9

9:
-1  %10 = load i32, i32* %8
-1  %11 = load i32, i32* @global.N
-1  %12 = icmp slt i32 %10, %11
-1  br i1 %12, label %13, label %96

13:
-1  %14 = alloca i1
-1  %15 = load i32*, i32** @global.row
-1  %16 = load i32, i32* %8
-1  %17 = getelementptr i32, i32* %15, i32 %16
-1  %18 = load i32, i32* %17
-1  %19 = icmp eq i32 %18, 0
-1  store i1 0, i1* %14
-1  br i1 %19, label %20, label %43

20:
-1  %21 = load i32**, i32*** @global.d
-1  %22 = getelementptr i32*, i32** %21, i32 0
-1  %23 = load i32*, i32** %22
-1  %24 = load i32, i32* %8
-1  %25 = load i32, i32* %2
-1  %26 = add i32 %24, %25
-1  %27 = getelementptr i32, i32* %23, i32 %26
-1  %28 = load i32, i32* %27
-1  %29 = icmp eq i32 %28, 0
-1  store i1 0, i1* %14
-1  br i1 %29, label %30, label %43

30:
-1  %31 = load i32**, i32*** @global.d
-1  %32 = getelementptr i32*, i32** %31, i32 1
-1  %33 = load i32*, i32** %32
-1  %34 = load i32, i32* %8
-1  %35 = load i32, i32* @global.N
-1  %36 = add i32 %34, %35
-1  %37 = sub i32 %36, 1
-1  %38 = load i32, i32* %2
-1  %39 = sub i32 %37, %38
-1  %40 = getelementptr i32, i32* %33, i32 %39
-1  %41 = load i32, i32* %40
-1  %42 = icmp eq i32 %41, 0
-1  store i1 %42, i1* %14
-1  br label %43

43:
-1  %44 = load i1, i1* %14
-1  br i1 %44, label %45, label %92

45:
-1  %46 = load i32**, i32*** @global.d
-1  %47 = getelementptr i32*, i32** %46, i32 1
-1  %48 = load i32*, i32** %47
-1  %49 = load i32, i32* %8
-1  %50 = load i32, i32* @global.N
-1  %51 = add i32 %49, %50
-1  %52 = sub i32 %51, 1
-1  %53 = load i32, i32* %2
-1  %54 = sub i32 %52, %53
-1  %55 = getelementptr i32, i32* %48, i32 %54
-1  store i32 1, i32* %55
-1  %56 = load i32**, i32*** @global.d
-1  %57 = getelementptr i32*, i32** %56, i32 0
-1  %58 = load i32*, i32** %57
-1  %59 = load i32, i32* %8
-1  %60 = load i32, i32* %2
-1  %61 = add i32 %59, %60
-1  %62 = getelementptr i32, i32* %58, i32 %61
-1  store i32 1, i32* %62
-1  %63 = load i32*, i32** @global.row
-1  %64 = load i32, i32* %8
-1  %65 = getelementptr i32, i32* %63, i32 %64
-1  store i32 1, i32* %65
-1  %66 = load i32*, i32** @global.col
-1  %67 = load i32, i32* %2
-1  %68 = getelementptr i32, i32* %66, i32 %67
-1  %69 = load i32, i32* %8
-1  store i32 %69, i32* %68
-1  %70 = load i32, i32* %2
-1  %71 = add i32 %70, 1
-1  call void @global.search(i32 %71)
-1  %72 = load i32*, i32** @global.row
-1  %73 = load i32, i32* %8
-1  %74 = getelementptr i32, i32* %72, i32 %73
-1  store i32 0, i32* %74
-1  %75 = load i32**, i32*** @global.d
-1  %76 = getelementptr i32*, i32** %75, i32 0
-1  %77 = load i32*, i32** %76
-1  %78 = load i32, i32* %8
-1  %79 = load i32, i32* %2
-1  %80 = add i32 %78, %79
-1  %81 = getelementptr i32, i32* %77, i32 %80
-1  store i32 0, i32* %81
-1  %82 = load i32**, i32*** @global.d
-1  %83 = getelementptr i32*, i32** %82, i32 1
-1  %84 = load i32*, i32** %83
-1  %85 = load i32, i32* %8
-1  %86 = load i32, i32* @global.N
-1  %87 = add i32 %85, %86
-1  %88 = sub i32 %87, 1
-1  %89 = load i32, i32* %2
-1  %90 = sub i32 %88, %89
-1  %91 = getelementptr i32, i32* %84, i32 %90
-1  store i32 0, i32* %91
-1  br label %92

92:
-1  br label %93

93:
-1  %94 = load i32, i32* %8
-1  %95 = add i32 %94, 1
-1  store i32 %95, i32* %8
-1  br label %9

96:
-1  br label %97

97:
-1  br label %98

98:
-1  ret void
}

define i32 @main() {
-1  call void @_init()
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  store i32 0, i32* %2
-1  br label %3

3:
-1  %4 = load i32, i32* %2
-1  %5 = icmp slt i32 %4, 8
-1  br i1 %5, label %6, label %16

6:
-1  %7 = load i32*, i32** @global.row
-1  %8 = load i32, i32* %2
-1  %9 = getelementptr i32, i32* %7, i32 %8
-1  store i32 0, i32* %9
-1  %10 = load i32*, i32** @global.col
-1  %11 = load i32, i32* %2
-1  %12 = getelementptr i32, i32* %10, i32 %11
-1  store i32 0, i32* %12
-1  br label %13

13:
-1  %14 = load i32, i32* %2
-1  %15 = add i32 %14, 1
-1  store i32 %15, i32* %2
-1  br label %3

16:
-1  store i32 0, i32* %2
-1  br label %17

17:
-1  %18 = load i32, i32* %2
-1  %19 = icmp slt i32 %18, 2
-1  br i1 %19, label %20, label %52

20:
-1  %21 = load i32**, i32*** @global.d
-1  %22 = load i32, i32* %2
-1  %23 = getelementptr i32*, i32** %21, i32 %22
-1  %24 = add i32 8, 8
-1  %25 = sub i32 %24, 1
-1  %26 = mul i32 4, %25
-1  %27 = add i32 %26, 4
-1  %28 = call i8* @malloc(i32 %27)
-1  %29 = bitcast i8* %28 to i32*
-1  store i32 %25, i32* %29
-1  %30 = getelementptr i32, i32* %29, i32 1
-1  %31 = bitcast i32* %30 to i32*
-1  store i32* %31, i32** %23
-1  %32 = alloca i32
-1  store i32 0, i32* %32
-1  store i32 0, i32* %32
-1  br label %33

33:
-1  %34 = load i32, i32* %32
-1  %35 = add i32 8, 8
-1  %36 = sub i32 %35, 1
-1  %37 = icmp slt i32 %34, %36
-1  br i1 %37, label %38, label %48

38:
-1  %39 = load i32**, i32*** @global.d
-1  %40 = load i32, i32* %2
-1  %41 = getelementptr i32*, i32** %39, i32 %40
-1  %42 = load i32*, i32** %41
-1  %43 = load i32, i32* %32
-1  %44 = getelementptr i32, i32* %42, i32 %43
-1  store i32 0, i32* %44
-1  br label %45

45:
-1  %46 = load i32, i32* %32
-1  %47 = add i32 %46, 1
-1  store i32 %47, i32* %32
-1  br label %33

48:
-1  br label %49

49:
-1  %50 = load i32, i32* %2
-1  %51 = add i32 %50, 1
-1  store i32 %51, i32* %2
-1  br label %17

52:
-1  call void @global.search(i32 0)
-1  store i32 0, i32* %1
-1  br label %53

53:
-1  %54 = load i32, i32* %1
-1  ret i32 %54
}















declare i8* @malloc(i32)
declare i8* @_new_array(i32, i32, i32*)
declare void @_print(i8*)
declare void @_println(i8*)
declare void @_printInt(i32)
declare void @_printlnInt(i32)
declare i8* @_getString()
declare i32 @_getInt()
declare i8* @_toString(i32)
declare i8* @_string_add(i8*, i8*)
declare i1 @_string_eq(i8*, i8*)
declare i1 @_string_ne(i8*, i8*)
declare i1 @_string_le(i8*, i8*)
declare i1 @_string_leq(i8*, i8*)
declare i1 @_string_ge(i8*, i8*)
declare i1 @_string_geq(i8*, i8*)
declare i8* @_string_substring(i8*, i32, i32)
declare i32 @_string_parseInt(i8*)
declare i32 @_string_ord(i8*, i32)
declare i8* @_boolToString(i1)

@global.N = global i32 0
@global.row = global i32* null
@global.col = global i32* null
@global.d = global i32** null
@.strconst.0 = private unnamed_addr constant [3 x i8] c" O\00"
@.strconst.1 = private unnamed_addr constant [3 x i8] c" .\00"
@.strconst.2 = private unnamed_addr constant [1 x i8] c"\00"
@.strconst.3 = private unnamed_addr constant [1 x i8] c"\00"




define void @_init() {
0  store i32 8, i32* @global.N
1  %1 = mul i32 4, 8
2  %2 = add i32 %1, 4
3  %3 = call i8* @malloc(i32 %2)
4  %4 = bitcast i8* %3 to i32*
5  store i32 8, i32* %4
6  %5 = getelementptr i32, i32* %4, i32 1
7  %6 = bitcast i32* %5 to i32*
8  store i32* %6, i32** @global.row
9  %7 = mul i32 4, 8
10  %8 = add i32 %7, 4
11  %9 = call i8* @malloc(i32 %8)
12  %10 = bitcast i8* %9 to i32*
13  store i32 8, i32* %10
14  %11 = getelementptr i32, i32* %10, i32 1
15  %12 = bitcast i32* %11 to i32*
16  store i32* %12, i32** @global.col
17  %13 = alloca i32, i32 2
18  store i32 2, i32* %13
19  %14 = getelementptr i32, i32* %13, i32 1
20  store i32 0, i32* %14
21  %15 = call i8* @_new_array(i32 4, i32 2, i32* %13)
22  %16 = bitcast i8* %15 to i32**
23  store i32** %16, i32*** @global.d
24  ret void
}

define void @global.printBoard() {
0  %1.tmp.0.0 <- 0
1  br label %3

3:
2  %1.phi.0 <- %1.tmp.0.0
3  %5 = load i32, i32* @global.N
4  %6 = icmp slt i32 %1.phi.0, %5
5  br i1 %6, label %7, label %32

7:
6  %2.tmp.1.0 <- 0
7  br label %8

8:
8  %2.phi.1 <- %2.tmp.1.0
9  %10 = load i32, i32* @global.N
10  %11 = icmp slt i32 %2.phi.1, %10
11  br i1 %11, label %12, label %27

12:
12  %13 = load i32*, i32** @global.col
13  %15 = getelementptr i32, i32* %13, i32 %1.phi.0
14  %16 = load i32, i32* %15
15  %18 = icmp eq i32 %16, %2.phi.1
16  br i1 %18, label %19, label %21

19:
17  %20 = getelementptr [3 x i8], [3 x i8]* @.strconst.0, i32 0, i32 0
18  call void @_print(i8* %20)
19  br label %24

21:
20  %22 = getelementptr [3 x i8], [3 x i8]* @.strconst.1, i32 0, i32 0
21  call void @_print(i8* %22)
22  br label %24

24:
23  %26 = add i32 %2.phi.1, 1
24  %2.tmp.1.0 <- %26
25  br label %8

27:
26  %28 = getelementptr [1 x i8], [1 x i8]* @.strconst.2, i32 0, i32 0
27  call void @_println(i8* %28)
28  %31 = add i32 %1.phi.0, 1
29  %1.tmp.0.0 <- %31
30  br label %3

32:
31  %33 = getelementptr [1 x i8], [1 x i8]* @.strconst.3, i32 0, i32 0
32  call void @_println(i8* %33)
33  ret void
}

define void @global.search(i32 %0) {
0  %4 = load i32, i32* @global.N
1  %5 = icmp eq i32 %0, %4
2  br i1 %5, label %6, label %7

6:
3  call void @global.printBoard()
4  br label %98

7:
5  %8.tmp.1.0 <- 0
6  br label %9

9:
7  %8.phi.1 <- %8.tmp.1.0
8  %11 = load i32, i32* @global.N
9  %12 = icmp slt i32 %8.phi.1, %11
10  br i1 %12, label %13, label %98

13:
11  %15 = load i32*, i32** @global.row
12  %17 = getelementptr i32, i32* %15, i32 %8.phi.1
13  %18 = load i32, i32* %17
14  %19 = icmp eq i32 %18, 0
15  %14.tmp.2.0 <- 0
16  br i1 %19, label %20, label %43

20:
17  %21 = load i32**, i32*** @global.d
18  %22 = getelementptr i32*, i32** %21, i32 0
19  %23 = load i32*, i32** %22
20  %26 = add i32 %8.phi.1, %0
21  %27 = getelementptr i32, i32* %23, i32 %26
22  %28 = load i32, i32* %27
23  %29 = icmp eq i32 %28, 0
24  %14.tmp.2.0 <- 0
25  br i1 %29, label %30, label %43

30:
26  %31 = load i32**, i32*** @global.d
27  %32 = getelementptr i32*, i32** %31, i32 1
28  %33 = load i32*, i32** %32
29  %35 = load i32, i32* @global.N
30  %36 = add i32 %8.phi.1, %35
31  %37 = sub i32 %36, 1
32  %39 = sub i32 %37, %0
33  %40 = getelementptr i32, i32* %33, i32 %39
34  %41 = load i32, i32* %40
35  %42 = icmp eq i32 %41, 0
36  %14.tmp.2.0 <- %42
37  br label %43

43:
38  %14.phi.2 <- %14.tmp.2.0
39  br i1 %14.phi.2, label %45, label %93

45:
40  %46 = load i32**, i32*** @global.d
41  %47 = getelementptr i32*, i32** %46, i32 1
42  %48 = load i32*, i32** %47
43  %50 = load i32, i32* @global.N
44  %51 = add i32 %8.phi.1, %50
45  %52 = sub i32 %51, 1
46  %54 = sub i32 %52, %0
47  %55 = getelementptr i32, i32* %48, i32 %54
48  store i32 1, i32* %55
49  %56 = load i32**, i32*** @global.d
50  %57 = getelementptr i32*, i32** %56, i32 0
51  %58 = load i32*, i32** %57
52  %61 = add i32 %8.phi.1, %0
53  %62 = getelementptr i32, i32* %58, i32 %61
54  store i32 1, i32* %62
55  %63 = load i32*, i32** @global.row
56  %65 = getelementptr i32, i32* %63, i32 %8.phi.1
57  store i32 1, i32* %65
58  %66 = load i32*, i32** @global.col
59  %68 = getelementptr i32, i32* %66, i32 %0
60  store i32 %8.phi.1, i32* %68
61  %71 = add i32 %0, 1
62  call void @global.search(i32 %71)
63  %72 = load i32*, i32** @global.row
64  %74 = getelementptr i32, i32* %72, i32 %8.phi.1
65  store i32 0, i32* %74
66  %75 = load i32**, i32*** @global.d
67  %76 = getelementptr i32*, i32** %75, i32 0
68  %77 = load i32*, i32** %76
69  %80 = add i32 %8.phi.1, %0
70  %81 = getelementptr i32, i32* %77, i32 %80
71  store i32 0, i32* %81
72  %82 = load i32**, i32*** @global.d
73  %83 = getelementptr i32*, i32** %82, i32 1
74  %84 = load i32*, i32** %83
75  %86 = load i32, i32* @global.N
76  %87 = add i32 %8.phi.1, %86
77  %88 = sub i32 %87, 1
78  %90 = sub i32 %88, %0
79  %91 = getelementptr i32, i32* %84, i32 %90
80  store i32 0, i32* %91
81  br label %93

93:
82  %95 = add i32 %8.phi.1, 1
83  %8.tmp.1.0 <- %95
84  br label %9

98:
85  ret void
}

define i32 @main() {
0  call void @_init()
1  %2.tmp.0.0 <- 0
2  br label %3

3:
3  %2.phi.0 <- %2.tmp.0.0
4  %5 = icmp slt i32 %2.phi.0, 8
5  br i1 %5, label %6, label %16

6:
6  %7 = load i32*, i32** @global.row
7  %9 = getelementptr i32, i32* %7, i32 %2.phi.0
8  store i32 0, i32* %9
9  %10 = load i32*, i32** @global.col
10  %12 = getelementptr i32, i32* %10, i32 %2.phi.0
11  store i32 0, i32* %12
12  %15 = add i32 %2.phi.0, 1
13  %2.tmp.0.0 <- %15
14  br label %3

16:
15  %2.tmp.1.1 <- 0
16  br label %17

17:
17  %2.phi.1 <- %2.tmp.1.1
18  %19 = icmp slt i32 %2.phi.1, 2
19  br i1 %19, label %20, label %52

20:
20  %21 = load i32**, i32*** @global.d
21  %23 = getelementptr i32*, i32** %21, i32 %2.phi.1
22  %24 = add i32 8, 8
23  %25 = sub i32 %24, 1
24  %26 = mul i32 4, %25
25  %27 = add i32 %26, 4
26  %28 = call i8* @malloc(i32 %27)
27  %29 = bitcast i8* %28 to i32*
28  store i32 %25, i32* %29
29  %30 = getelementptr i32, i32* %29, i32 1
30  %31 = bitcast i32* %30 to i32*
31  store i32* %31, i32** %23
32  %32.tmp.1.0 <- 0
33  br label %33

33:
34  %32.phi.1 <- %32.tmp.1.0
35  %35 = add i32 8, 8
36  %36 = sub i32 %35, 1
37  %37 = icmp slt i32 %32.phi.1, %36
38  br i1 %37, label %38, label %49

38:
39  %39 = load i32**, i32*** @global.d
40  %41 = getelementptr i32*, i32** %39, i32 %2.phi.1
41  %42 = load i32*, i32** %41
42  %44 = getelementptr i32, i32* %42, i32 %32.phi.1
43  store i32 0, i32* %44
44  %47 = add i32 %32.phi.1, 1
45  %32.tmp.1.0 <- %47
46  br label %33

49:
47  %51 = add i32 %2.phi.1, 1
48  %2.tmp.1.1 <- %51
49  br label %17

52:
50  call void @global.search(i32 0)
51  ret i32 0
}
















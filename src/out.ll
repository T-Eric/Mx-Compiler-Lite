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

@.strconst.0 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.1 = private unnamed_addr constant [1 x i8] c"\00"




define void @_init() {
-1  ret void
}

define i32 @global.partition(i32* %0, i32 %1, i32 %2) {
-1  %4 = alloca i32*
-1  store i32* %0, i32** %4
-1  %5 = alloca i32
-1  store i32 %1, i32* %5
-1  %6 = alloca i32
-1  store i32 %2, i32* %6
-1  %7 = alloca i32
-1  store i32 0, i32* %7
-1  %8 = alloca i32
-1  store i32 0, i32* %8
-1  %9 = load i32*, i32** %4
-1  %10 = load i32, i32* %6
-1  %11 = getelementptr i32, i32* %9, i32 %10
-1  %12 = load i32, i32* %11
-1  store i32 %12, i32* %8
-1  %13 = alloca i32
-1  store i32 0, i32* %13
-1  %14 = load i32, i32* %5
-1  %15 = sub i32 %14, 1
-1  store i32 %15, i32* %13
-1  %16 = alloca i32
-1  store i32 0, i32* %16
-1  %17 = load i32, i32* %5
-1  store i32 %17, i32* %16
-1  br label %18

18:
-1  %19 = load i32, i32* %16
-1  %20 = load i32, i32* %6
-1  %21 = icmp slt i32 %19, %20
-1  br i1 %21, label %22, label %52

22:
-1  %23 = load i32*, i32** %4
-1  %24 = load i32, i32* %16
-1  %25 = getelementptr i32, i32* %23, i32 %24
-1  %26 = load i32, i32* %25
-1  %27 = load i32, i32* %8
-1  %28 = icmp sle i32 %26, %27
-1  br i1 %28, label %29, label %48

29:
-1  %30 = load i32, i32* %13
-1  %31 = add i32 %30, 1
-1  store i32 %31, i32* %13
-1  %32 = alloca i32
-1  store i32 0, i32* %32
-1  %33 = load i32*, i32** %4
-1  %34 = load i32, i32* %13
-1  %35 = getelementptr i32, i32* %33, i32 %34
-1  %36 = load i32, i32* %35
-1  store i32 %36, i32* %32
-1  %37 = load i32*, i32** %4
-1  %38 = load i32, i32* %13
-1  %39 = getelementptr i32, i32* %37, i32 %38
-1  %40 = load i32*, i32** %4
-1  %41 = load i32, i32* %16
-1  %42 = getelementptr i32, i32* %40, i32 %41
-1  %43 = load i32, i32* %42
-1  store i32 %43, i32* %39
-1  %44 = load i32*, i32** %4
-1  %45 = load i32, i32* %16
-1  %46 = getelementptr i32, i32* %44, i32 %45
-1  %47 = load i32, i32* %32
-1  store i32 %47, i32* %46
-1  br label %48

48:
-1  br label %49

49:
-1  %50 = load i32, i32* %16
-1  %51 = add i32 %50, 1
-1  store i32 %51, i32* %16
-1  br label %18

52:
-1  %53 = alloca i32
-1  store i32 0, i32* %53
-1  %54 = load i32*, i32** %4
-1  %55 = load i32, i32* %13
-1  %56 = add i32 %55, 1
-1  %57 = getelementptr i32, i32* %54, i32 %56
-1  %58 = load i32, i32* %57
-1  store i32 %58, i32* %53
-1  %59 = load i32*, i32** %4
-1  %60 = load i32, i32* %13
-1  %61 = add i32 %60, 1
-1  %62 = getelementptr i32, i32* %59, i32 %61
-1  %63 = load i32*, i32** %4
-1  %64 = load i32, i32* %6
-1  %65 = getelementptr i32, i32* %63, i32 %64
-1  %66 = load i32, i32* %65
-1  store i32 %66, i32* %62
-1  %67 = load i32*, i32** %4
-1  %68 = load i32, i32* %6
-1  %69 = getelementptr i32, i32* %67, i32 %68
-1  %70 = load i32, i32* %53
-1  store i32 %70, i32* %69
-1  %71 = load i32, i32* %13
-1  %72 = add i32 %71, 1
-1  store i32 %72, i32* %7
-1  br label %73

73:
-1  %74 = load i32, i32* %7
-1  ret i32 %74
}

define void @global.quick_sort(i32* %0, i32 %1, i32 %2) {
-1  %4 = alloca i32*
-1  store i32* %0, i32** %4
-1  %5 = alloca i32
-1  store i32 %1, i32* %5
-1  %6 = alloca i32
-1  store i32 %2, i32* %6
-1  %7 = load i32, i32* %5
-1  %8 = load i32, i32* %6
-1  %9 = icmp sge i32 %7, %8
-1  br i1 %9, label %10, label %11

10:
-1  br label %25

11:
-1  %12 = alloca i32
-1  store i32 0, i32* %12
-1  %13 = load i32*, i32** %4
-1  %14 = load i32, i32* %5
-1  %15 = load i32, i32* %6
-1  %16 = call i32 @global.partition(i32* %13, i32 %14, i32 %15)
-1  store i32 %16, i32* %12
-1  %17 = load i32*, i32** %4
-1  %18 = load i32, i32* %5
-1  %19 = load i32, i32* %12
-1  %20 = sub i32 %19, 1
-1  call void @global.quick_sort(i32* %17, i32 %18, i32 %20)
-1  %21 = load i32*, i32** %4
-1  %22 = load i32, i32* %12
-1  %23 = add i32 %22, 1
-1  %24 = load i32, i32* %6
-1  call void @global.quick_sort(i32* %21, i32 %23, i32 %24)
-1  br label %25

25:
-1  ret void
}

define void @global.quick_sort_inf(i32* %0) {
-1  %2 = alloca i32*
-1  store i32* %0, i32** %2
-1  %3 = load i32*, i32** %2
-1  %4 = load i32*, i32** %2
-1  %5 = bitcast i32* %4 to i32*
-1  %6 = getelementptr i32, i32* %5, i32 -1
-1  %7 = load i32, i32* %6
-1  %8 = sub i32 %7, 1
-1  call void @global.quick_sort(i32* %3, i32 0, i32 %8)
-1  br label %9

9:
-1  ret void
}

define i32 @main() {
-1  call void @_init()
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = call i32 @_getInt()
-1  store i32 %3, i32* %2
-1  %4 = alloca i32*
-1  %5 = load i32, i32* %2
-1  %6 = mul i32 4, %5
-1  %7 = add i32 %6, 4
-1  %8 = call i8* @malloc(i32 %7)
-1  %9 = bitcast i8* %8 to i32*
-1  store i32 %5, i32* %9
-1  %10 = getelementptr i32, i32* %9, i32 1
-1  %11 = bitcast i32* %10 to i32*
-1  store i32* %11, i32** %4
-1  %12 = alloca i32
-1  store i32 0, i32* %12
-1  store i32 0, i32* %12
-1  br label %13

13:
-1  %14 = load i32, i32* %12
-1  %15 = load i32, i32* %2
-1  %16 = icmp slt i32 %14, %15
-1  br i1 %16, label %17, label %25

17:
-1  %18 = load i32*, i32** %4
-1  %19 = load i32, i32* %12
-1  %20 = getelementptr i32, i32* %18, i32 %19
-1  %21 = call i32 @_getInt()
-1  store i32 %21, i32* %20
-1  br label %22

22:
-1  %23 = load i32, i32* %12
-1  %24 = add i32 %23, 1
-1  store i32 %24, i32* %12
-1  br label %13

25:
-1  %26 = load i32*, i32** %4
-1  call void @global.quick_sort_inf(i32* %26)
-1  store i32 0, i32* %12
-1  br label %27

27:
-1  %28 = load i32, i32* %12
-1  %29 = load i32, i32* %2
-1  %30 = icmp slt i32 %28, %29
-1  br i1 %30, label %31, label %42

31:
-1  %32 = load i32*, i32** %4
-1  %33 = load i32, i32* %12
-1  %34 = getelementptr i32, i32* %32, i32 %33
-1  %35 = load i32, i32* %34
-1  %36 = call i8* @_toString(i32 %35)
-1  %37 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
-1  %38 = call i8* @_string_add(i8* %36, i8* %37)
-1  call void @_print(i8* %38)
-1  br label %39

39:
-1  %40 = load i32, i32* %12
-1  %41 = add i32 %40, 1
-1  store i32 %41, i32* %12
-1  br label %27

42:
-1  %43 = getelementptr [1 x i8], [1 x i8]* @.strconst.1, i32 0, i32 0
-1  call void @_println(i8* %43)
-1  store i32 0, i32* %1
-1  br label %44

44:
-1  %45 = load i32, i32* %1
-1  ret i32 %45
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

@.strconst.0 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.1 = private unnamed_addr constant [1 x i8] c"\00"




define void @_init() {
0  ret void
}

define i32 @global.partition(i32* %0, i32 %1, i32 %2) {
0  %11 = getelementptr i32, i32* %0, i32 %2
1  %12 = load i32, i32* %11
2  %15 = sub i32 %1, 1
3  %16.tmp.0.0 <- %1
4  %13.tmp.1.0 <- %15
5  %32.tmp.1.0 <- 0
6  br label %18

18:
7  %32.phi.1 <- %32.tmp.1.0
8  %13.phi.1 <- %13.tmp.1.0
9  %16.phi.0 <- %16.tmp.0.0
10  %21 = icmp slt i32 %16.phi.0, %2
11  br i1 %21, label %22, label %52

22:
12  %25 = getelementptr i32, i32* %0, i32 %16.phi.0
13  %26 = load i32, i32* %25
14  %28 = icmp sle i32 %26, %12
15  %13.tmp.0.1 <- %13.phi.1
16  %32.tmp.0.1 <- %32.phi.1
17  br i1 %28, label %29, label %49

29:
18  %31 = add i32 %13.phi.1, 1
19  %35 = getelementptr i32, i32* %0, i32 %31
20  %36 = load i32, i32* %35
21  %39 = getelementptr i32, i32* %0, i32 %31
22  %42 = getelementptr i32, i32* %0, i32 %16.phi.0
23  %43 = load i32, i32* %42
24  store i32 %43, i32* %39
25  %46 = getelementptr i32, i32* %0, i32 %16.phi.0
26  store i32 %36, i32* %46
27  %13.tmp.0.1 <- %31
28  %32.tmp.0.1 <- %36
29  br label %49

49:
30  %32.phi.0 <- %32.tmp.0.1
31  %13.phi.0 <- %13.tmp.0.1
32  %51 = add i32 %16.phi.0, 1
33  %16.tmp.0.0 <- %51
34  %13.tmp.1.0 <- %13.phi.0
35  %32.tmp.1.0 <- %32.phi.0
36  br label %18

52:
37  %56 = add i32 %13.phi.1, 1
38  %57 = getelementptr i32, i32* %0, i32 %56
39  %58 = load i32, i32* %57
40  %61 = add i32 %13.phi.1, 1
41  %62 = getelementptr i32, i32* %0, i32 %61
42  %65 = getelementptr i32, i32* %0, i32 %2
43  %66 = load i32, i32* %65
44  store i32 %66, i32* %62
45  %69 = getelementptr i32, i32* %0, i32 %2
46  store i32 %58, i32* %69
47  %72 = add i32 %13.phi.1, 1
48  ret i32 %72
}

define void @global.quick_sort(i32* %0, i32 %1, i32 %2) {
0  %9 = icmp sge i32 %1, %2
1  %12.tmp.0.0 <- 0
2  br i1 %9, label %25, label %11

11:
3  %16 = call i32 @global.partition(i32* %0, i32 %1, i32 %2)
4  %20 = sub i32 %16, 1
5  call void @global.quick_sort(i32* %0, i32 %1, i32 %20)
6  %23 = add i32 %16, 1
7  call void @global.quick_sort(i32* %0, i32 %23, i32 %2)
8  %12.tmp.0.0 <- %16
9  br label %25

25:
10  %12.phi.0 <- %12.tmp.0.0
11  ret void
}

define void @global.quick_sort_inf(i32* %0) {
0  %5 = bitcast i32* %0 to i32*
1  %6 = getelementptr i32, i32* %5, i32 -1
2  %7 = load i32, i32* %6
3  %8 = sub i32 %7, 1
4  call void @global.quick_sort(i32* %0, i32 0, i32 %8)
5  ret void
}

define i32 @main() {
0  call void @_init()
1  %3 = call i32 @_getInt()
2  %6 = mul i32 4, %3
3  %7 = add i32 %6, 4
4  %8 = call i8* @malloc(i32 %7)
5  %9 = bitcast i8* %8 to i32*
6  store i32 %3, i32* %9
7  %10 = getelementptr i32, i32* %9, i32 1
8  %11 = bitcast i32* %10 to i32*
9  %12.tmp.0.0 <- 0
10  br label %13

13:
11  %12.phi.0 <- %12.tmp.0.0
12  %16 = icmp slt i32 %12.phi.0, %3
13  br i1 %16, label %17, label %25

17:
14  %20 = getelementptr i32, i32* %11, i32 %12.phi.0
15  %21 = call i32 @_getInt()
16  store i32 %21, i32* %20
17  %24 = add i32 %12.phi.0, 1
18  %12.tmp.0.0 <- %24
19  br label %13

25:
20  call void @global.quick_sort_inf(i32* %11)
21  %12.tmp.1.1 <- 0
22  br label %27

27:
23  %12.phi.1 <- %12.tmp.1.1
24  %30 = icmp slt i32 %12.phi.1, %3
25  br i1 %30, label %31, label %42

31:
26  %34 = getelementptr i32, i32* %11, i32 %12.phi.1
27  %35 = load i32, i32* %34
28  %36 = call i8* @_toString(i32 %35)
29  %37 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
30  %38 = call i8* @_string_add(i8* %36, i8* %37)
31  call void @_print(i8* %38)
32  %41 = add i32 %12.phi.1, 1
33  %12.tmp.1.1 <- %41
34  br label %27

42:
35  %43 = getelementptr [1 x i8], [1 x i8]* @.strconst.1, i32 0, i32 0
36  call void @_println(i8* %43)
37  ret i32 0
}
















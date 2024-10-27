class.A.A
------

_init
------

global.fun
%8: 6 6
%23: 11 12
%0: 0 9
%19.0: 12 17
%18: 9 10
%15: 8 14
%27: 14 15
%8.0: 2 6
%1: 0 14
%19: 17 19
%14: 7 8
%7: 0 1
------

main
%28: 28 44
%41: 37 41
%3: 9 23
%44: 39 40
%16: 18 19
%48: 43 46
%32: 30 31
%5: 1 2
%10: 11 12
%14: 15 16
%26: 26 28
%21: 23 25
%33: 31 35
%23: 25 45
%2.0: 4 47
%2: 10 26
%11: 12 13
%30: 29 36
%3.0: 5 47
%43: 38 39
%35: 32 33
%17: 20 21
%6: 2 3
%15: 17 18
%27: 27 28
%22: 24 25
%7.0: 6 47
%36: 33 34
%38: 34 35
%45: 40 41
%39: 35 36
%7: 8 43
%13: 14 15
------

declare i8* @malloc(i32)
declare i8* @_new_array(i32, i32, i32*)
declare void @_print(i8*)
declare void @_println(i8*)
declare void @_printInt(i32)
declare void @_printlnInt(i32)
declare i8* @_getString()
declare i32 @_getInt()
declare i32 @_toString()
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

@global.n = global i32 0
@global.m = global i32 0
@.strconst.0 = private unnamed_addr constant [3 x i8] c"i=\00"

%class.A = type { i32, i1 }

define void @class.A.A(%class.A* %0) {
0  ret void
}




define void @_init() {
0  store i32 114, i32* @global.n
1  store i32 514, i32* @global.m
2  ret void
}

define i32 @global.fun(i32 %0, i32 %1) {
0  %7 = icmp sgt i32 10, 20
1  br i1 %7, label %9, label %10

9:
2  %8.0 <- 10
3  br label %11

10:
4  %8.0 <- 20
5  br label %11

11:
6  %8 <- %8.0
7  %14 = add i32 100, 200
8  %15 = add i32 %14, 300
9  %18 = icmp sgt i32 %0, %1
10  br i1 %18, label %20, label %24

20:
11  %23 = sub i32 %15, %1
12  %19.0 <- %23
13  br label %28

24:
14  %27 = add i32 %15, %1
15  %19.0 <- %27
16  br label %28

28:
17  %19 <- %19.0
18  br label %30

30:
19  ret i32 %19
}

define i32 @main() {
0  call void @_init()
1  %5 = call i8* @malloc(i32 8)
2  %6 = bitcast i8* %5 to %class.A*
3  call void @class.A.A(%class.A* %6)
4  %2.0 <- 0
5  %3.0 <- 0
6  %7.0 <- 0
7  br label %8

8:
8  %7 <- %7.0
9  %3 <- %3.0
10  %2 <- %2.0
11  %10 = load i32, i32* @global.n
12  %11 = icmp slt i32* %7, %10
13  br i1 %11, label %12, label %50

12:
14  %13 = load i32, i32* @global.n
15  %14 = add i32 %13, 1
16  store i32 %14, i32* @global.n
17  %15 = load i32, i32* @global.m
18  %16 = sub i32 %15, 1
19  store i32 %16, i32* @global.m
20  %17 = getelementptr [3 x i8], [3 x i8]* @.strconst.0, i32 0, i32 0
21  call void @_println(i8* %17)
22  call void @_printlnInt(i32* %7)
23  %21 = add i32 %3, %2
24  %22 = load i32, i32* @global.n
25  %23 = add i32 %21, %22
26  %26 = add i32 %2, %7
27  %27 = load i32, i32* @global.m
28  %28 = add i32 %26, %27
29  %30 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
30  %32 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
31  %33 = load i32, i32* %32
32  %35 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
33  %36 = load i32, i32* %35
34  %38 = call i32 @global.fun(i32 %36, i32* %7)
35  %39 = add i32 %33, %38
36  store i32 %39, i32* %30
37  %41 = getelementptr %class.A, %class.A* %6, i32 0, i32 1
38  %43 = getelementptr %class.A, %class.A* %6, i32 0, i32 1
39  %44 = load i1, i1* %43
40  %45 = xor i1 %44, 1
41  store i1 %45, i1* %41
42  br label %46

46:
43  %48 = add i32 %7, 2
44  %2.0 <- %28
45  %3.0 <- %23
46  %7.0 <- %48
47  br label %8

50:
48  ret i32 0
}
















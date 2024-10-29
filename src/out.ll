_init
------

main
%24: 20 22
%14.phi.0: 26 26
%3.phi.1: 28 28
%53: 48 52
%58: 52 68
%3.phi.2: 41 53
%14.tmp.1.1: 38 72
%74: 57 58
%13: 7 72
%3.phi.0: 10 20
%5.tmp.0.0: 13 67
%14.tmp.0.0: 16 67
%47: 46 52
%19: 12 18
%67: 68 69
%50: 47 50
%57: 51 71
%70: 56 64
%12: 6 7
%3.tmp.2.2: 37 72
%5.tmp.1.1: 36 72
%26: 21 22
%5.phi.1: 42 46
%10: 3 4
%6.tmp.0.0: 17 67
%9: 2 3
%14.phi.1: 40 48
%43: 43 44
%3.tmp.1.1: 14 67
%80: 61 66
%18: 11 12
%61: 54 55
%32: 30 38
%3.tmp.0.0: 8 24
%75: 58 59
%23: 19 23
%6.phi.0: 25 72
%54: 49 50
%44: 44 45
%60: 53 70
%46: 45 46
%82: 33 34
%5.phi.0: 29 29
%34: 31 32
%4.tmp.0.0: 15 67
%8: 1 2
%11: 4 6
%4.phi.0: 27 72
%76: 59 60
------

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

@.strconst.0 = private unnamed_addr constant [2 x i8] c"\0A\00"




define void @_init() {
0  ret void
}

define i32 @main() {
0  call void @_init()
1  %8 = mul i32 4, 2801
2  %9 = add i32 %8, 4
3  %10 = call i8* @malloc(i32 %9)
4  %11 = bitcast i8* %10 to i32*
5  store i32 2801, i32* %11
6  %12 = getelementptr i32, i32* %11, i32 1
7  %13 = bitcast i32* %12 to i32*
8  %3.tmp.0.0 <- 0
9  br label %15

15:
10  %3.phi.0 <- %3.tmp.0.0
11  %18 = sub i32 %3.phi.0, 2800
12  %19 = icmp ne i32 %18, 0
13  %5.tmp.0.0 <- 0
14  %3.tmp.1.1 <- %3.phi.0
15  %4.tmp.0.0 <- 2800
16  %14.tmp.0.0 <- 0
17  %6.tmp.0.0 <- 0
18  br i1 %19, label %20, label %30

20:
19  %23 = add i32 %3.phi.0, 1
20  %24 = getelementptr i32, i32* %13, i32 %3.phi.0
21  %26 = sdiv i32 10000, 5
22  store i32 %26, i32* %24
23  %3.tmp.0.0 <- %23
24  br label %15

30:
25  %6.phi.0 <- %6.tmp.0.0
26  %14.phi.0 <- %14.tmp.0.0
27  %4.phi.0 <- %4.tmp.0.0
28  %3.phi.1 <- %3.tmp.1.1
29  %5.phi.0 <- %5.tmp.0.0
30  %32 = mul i32 %4.phi.0, 2
31  %34 = icmp eq i32 %32, 0
32  br i1 %34, label %81, label %36

36:
36  %5.tmp.1.1 <- 0
37  %3.tmp.2.2 <- %4.phi.0
38  %14.tmp.1.1 <- %32
39  br label %39

39:
40  %14.phi.1 <- %14.tmp.1.1
41  %3.phi.2 <- %3.tmp.2.2
42  %5.phi.1 <- %5.tmp.1.1
43  %43 = getelementptr i32, i32* %13, i32 %3.phi.2
44  %44 = load i32, i32* %43
45  %46 = mul i32 %44, 10000
46  %47 = add i32 %5.phi.1, %46
47  %50 = getelementptr i32, i32* %13, i32 %3.phi.2
48  %53 = sub i32 %14.phi.1, 1
49  %54 = srem i32 %47, %53
50  store i32 %54, i32* %50
51  %57 = sub i32 %53, 1
52  %58 = sdiv i32 %47, %53
53  %60 = sub i32 %3.phi.2, 1
54  %61 = icmp eq i32 %60, 0
55  br i1 %61, label %68, label %64

64:
68  %67 = mul i32 %58, %60
69  %5.tmp.1.1 <- %67
70  %3.tmp.2.2 <- %60
71  %14.tmp.1.1 <- %57
72  br label %39

68:
56  %70 = sub i32 %4.phi.0, 14
57  %74 = sdiv i32 %58, 10000
58  %75 = add i32 %6.phi.0, %74
59  %76 = call i8* @_toString(i32 %75)
60  call void @_print(i8* %76)
61  %80 = srem i32 %58, 10000
62  %5.tmp.0.0 <- %58
63  %3.tmp.1.1 <- %60
64  %4.tmp.0.0 <- %70
65  %14.tmp.0.0 <- %57
66  %6.tmp.0.0 <- %80
67  br label %30

81:
33  %82 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
34  call void @_print(i8* %82)
35  ret i32 0
}
















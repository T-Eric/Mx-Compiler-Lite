_init
------

global.tak
%13: 2 3
%7: 14 15
%26: 7 8
%2: 0 12
%7.0: 10 14
%28: 9 10
%21: 5 8
%10: 0 1
%18: 4 5
%27: 8 9
%16: 3 8
%0: 0 7
%23: 6 7
%1: 0 7
------

main
%11: 4 5
%6: 2 4
%12: 5 6
%7: 3 4
%5: 1 4
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





define void @_init() {
0  ret void
}

define i32 @global.tak(i32 %0, i32 %1, i32 %2) {
0  %10 = icmp slt i32 %1, %0
1  br i1 %10, label %11, label %29

11:
2  %13 = sub i32 %0, 1
3  %16 = call i32 @global.tak(i32 %13, i32 %1, i32 %2)
4  %18 = sub i32 %1, 1
5  %21 = call i32 @global.tak(i32 %18, i32 %2, i32 %0)
6  %23 = sub i32 %2, 1
7  %26 = call i32 @global.tak(i32 %23, i32 %0, i32 %1)
8  %27 = call i32 @global.tak(i32 %16, i32 %21, i32 %26)
9  %28 = add i32 1, %27
10  %7.0 <- %28
11  br label %32

29:
12  %7.0 <- %2
13  br label %32

32:
14  %7 <- %7.0
15  ret i32 %7
}

define i32 @main() {
0  call void @_init()
1  %5 = call i32 @_getInt()
2  %6 = call i32 @_getInt()
3  %7 = call i32 @_getInt()
4  %11 = call i32 @global.tak(i32 %5, i32 %6, i32 %7)
5  %12 = call i8* @_toString(i32 %11)
6  call void @_println(i8* %12)
7  br label %13

13:
8  ret i32 0
}
















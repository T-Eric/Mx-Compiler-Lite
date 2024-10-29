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

@global.a = global %class.Node** null
@global.b = global %class.Node** null
@global.str = global i8* null
@.strconst.0 = private unnamed_addr constant [13 x i8] c"19asdojkhkj1\00"
@global.map = global i32** null

%class.Node = type { i32 }

define void @class.Node.Node(%class.Node* %0) {
-1  %2 = getelementptr %class.Node, %class.Node* %0, i32 0, i32 0
-1  store i32 1, i32* %2
-1  br label %3

3:
-1  ret void
}

define %class.Node* @class.Node.work(%class.Node* %0, i32 %1) {
-1  %3 = alloca i32
-1  store i32 %1, i32* %3
-1  %4 = alloca %class.Node*
-1  %5 = alloca i32
-1  store i32 0, i32* %5
-1  store i32 10, i32* %5
-1  br label %6

6:
-1  %7 = load i32, i32* %5
-1  %8 = getelementptr %class.Node, %class.Node* %0, i32 0, i32 0
-1  %9 = load i32, i32* %8
-1  %10 = icmp sle i32 %7, %9
-1  br i1 %10, label %11, label %15

11:
-1  store %class.Node* %0, %class.Node** %4
-1  br label %16

12:
-1  %13 = load i32, i32* %5
-1  %14 = add i32 %13, 1
-1  store i32 %14, i32* %5
-1  br label %6

15:
-1  store %class.Node* %0, %class.Node** %4
-1  br label %16

16:
-1  %17 = load %class.Node*, %class.Node** %4
-1  ret %class.Node* %17
}




define void @_init() {
-1  %1 = mul i32 4, 723
-1  %2 = add i32 %1, 4
-1  %3 = call i8* @malloc(i32 %2)
-1  %4 = bitcast i8* %3 to i32*
-1  store i32 723, i32* %4
-1  %5 = getelementptr i32, i32* %4, i32 1
-1  %6 = bitcast i32* %5 to %class.Node**
-1  store %class.Node** %6, %class.Node*** @global.a
-1  store %class.Node** null, %class.Node*** @global.b
-1  %7 = getelementptr [13 x i8], [13 x i8]* @.strconst.0, i32 0, i32 0
-1  store i8* %7, i8** @global.str
-1  %8 = alloca i32, i32 2
-1  store i32 153, i32* %8
-1  %9 = getelementptr i32, i32* %8, i32 1
-1  store i32 144, i32* %9
-1  %10 = call i8* @_new_array(i32 4, i32 2, i32* %8)
-1  %11 = bitcast i8* %10 to i32**
-1  store i32** %11, i32*** @global.map
-1  ret void
}

define void @global.work() {
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  store i32 11, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = load i32, i32* %1
-1  store i32 %3, i32* %2
-1  %4 = load %class.Node**, %class.Node*** @global.a
-1  %5 = load i32, i32* %2
-1  %6 = getelementptr %class.Node*, %class.Node** %4, i32 %5
-1  %7 = load %class.Node*, %class.Node** %6
-1  %8 = load i32, i32* %1
-1  %9 = call %class.Node* @class.Node.work(%class.Node* %7, i32 %8)
-1  br label %10

10:
-1  ret void
}

define i32 @main() {
-1  call void @_init()
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = alloca i32
-1  store i32 0, i32* %3
-1  store i32 10213, i32* %3
-1  store i32 0, i32* %2
-1  br label %4

4:
-1  %5 = load i32, i32* %2
-1  %6 = load i32, i32* %3
-1  %7 = icmp slt i32 %5, %6
-1  br i1 %7, label %8, label %50

8:
-1  %9 = alloca i32
-1  store i32 0, i32* %9
-1  %10 = load i32, i32* %3
-1  %11 = load i32, i32* %2
-1  %12 = mul i32 %10, %11
-1  %13 = sub i32 %12, 13
-1  store i32 %13, i32* %9
-1  %14 = load i32, i32* %9
-1  %15 = load %class.Node**, %class.Node*** @global.a
-1  %16 = load i32, i32* %2
-1  %17 = getelementptr %class.Node*, %class.Node** %15, i32 %16
-1  %18 = load %class.Node*, %class.Node** %17
-1  %19 = getelementptr %class.Node, %class.Node* %18, i32 0, i32 0
-1  %20 = load i32, i32* %19
-1  %21 = icmp slt i32 %14, %20
-1  br i1 %21, label %22, label %23

22:
-1  br label %46

23:
-1  br i1 0, label %24, label %32

24:
-1  %25 = load %class.Node**, %class.Node*** @global.a
-1  %26 = load i32, i32* %2
-1  %27 = sub i32 %26, 1
-1  %28 = getelementptr %class.Node*, %class.Node** %25, i32 %27
-1  %29 = load %class.Node*, %class.Node** %28
-1  %30 = load i32, i32* %9
-1  %31 = call %class.Node* @class.Node.work(%class.Node* %29, i32 %30)
-1  br label %50

32:
-1  br i1 1, label %33, label %44

33:
-1  %34 = load %class.Node**, %class.Node*** @global.a
-1  %35 = load i32, i32* %2
-1  %36 = getelementptr %class.Node*, %class.Node** %34, i32 %35
-1  %37 = load %class.Node**, %class.Node*** @global.a
-1  %38 = load i32, i32* %2
-1  %39 = add i32 %38, 1
-1  %40 = getelementptr %class.Node*, %class.Node** %37, i32 %39
-1  %41 = load %class.Node*, %class.Node** %40
-1  %42 = load i32, i32* %9
-1  %43 = call %class.Node* @class.Node.work(%class.Node* %41, i32 %42)
-1  store %class.Node* %43, %class.Node** %36
-1  call void @global.work()
-1  br label %47

44:
-1  br label %45

45:
-1  br label %46

46:
-1  br label %47

47:
-1  %48 = load i32, i32* %2
-1  %49 = add i32 %48, 1
-1  store i32 %49, i32* %2
-1  br label %4

50:
-1  store i32 0, i32* %1
-1  br label %51

51:
-1  %52 = load i32, i32* %1
-1  ret i32 %52
}















class.Node.Node
%2: 0 1
%0: 0 0
------


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

@.strconst.0 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.1 = private unnamed_addr constant [40 x i8] c"Warning: Slice_int::slice: out of range\00"
@.strconst.2 = private unnamed_addr constant [46 x i8] c"Warning: Slice_int::copy: size() < arr.size()\00"
@.strconst.3 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.4 = private unnamed_addr constant [1 x i8] c"\00"

%class.Slice_int = type { i32*, i32, i32 }

define void @class.Slice_int.Slice_int(%class.Slice_int* %0) {
  ret void
}

define void @class.Slice_int.init(%class.Slice_int* %0, i32* %1) {
  %3 = alloca i32*
  store i32* %1, i32** %3
  %4 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %5 = load i32*, i32** %3
  store i32* %5, i32** %4
  %6 = load i32*, i32** %3
  %7 = bitcast i32* %6 to i32*
  %8 = getelementptr i32, i32* %7, i32 -1
  %9 = load i32, i32* %8
  call void @_printlnInt(i32 %9)
  %10 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %11 = load i32*, i32** %10
  %12 = bitcast i32* %11 to i32*
  %13 = getelementptr i32, i32* %12, i32 -1
  %14 = load i32, i32* %13
  call void @_printlnInt(i32 %14)
  %15 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  store i32 0, i32* %15
  %16 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 2
  %17 = load i32*, i32** %3
  %18 = bitcast i32* %17 to i32*
  %19 = getelementptr i32, i32* %18, i32 -1
  %20 = load i32, i32* %19
  store i32 %20, i32* %16
  %21 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %22 = load i32*, i32** %21
  %23 = bitcast i32* %22 to i32*
  %24 = getelementptr i32, i32* %23, i32 -1
  %25 = load i32, i32* %24
  call void @_printlnInt(i32 %25)
  %26 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %27 = load i32, i32* %26
  call void @_printlnInt(i32 %27)
  %28 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 2
  %29 = load i32, i32* %28
  call void @_printlnInt(i32 %29)
  br label %30

30:
  ret void
}

define i32 @class.Slice_int.size(%class.Slice_int* %0) {
  %2 = alloca i32
  %3 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 2
  %4 = load i32, i32* %3
  %5 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %6 = load i32, i32* %5
  %7 = sub i32 %4, %6
  store i32 %7, i32* %2
  br label %8

8:
  %9 = load i32, i32* %2
  ret i32 %9
}

define i32 @class.Slice_int.get(%class.Slice_int* %0, i32 %1) {
  %3 = alloca i32
  store i32 %1, i32* %3
  %4 = alloca i32
  %5 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %6 = load i32*, i32** %5
  %7 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %8 = load i32, i32* %7
  %9 = load i32, i32* %3
  %10 = add i32 %8, %9
  %11 = getelementptr i32, i32* %6, i32 %10
  %12 = load i32, i32* %11
  store i32 %12, i32* %4
  br label %13

13:
  %14 = load i32, i32* %4
  ret i32 %14
}

define void @class.Slice_int.set(%class.Slice_int* %0, i32 %1, i32 %2) {
  %4 = alloca i32
  store i32 %1, i32* %4
  %5 = alloca i32
  store i32 %2, i32* %5
  %6 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %7 = load i32*, i32** %6
  %8 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %9 = load i32, i32* %8
  %10 = load i32, i32* %4
  %11 = add i32 %9, %10
  %12 = getelementptr i32, i32* %7, i32 %11
  %13 = load i32, i32* %5
  store i32 %13, i32* %12
  br label %14

14:
  ret void
}

define %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %0, i32 %1, i32 %2) {
  %4 = alloca i32
  store i32 %1, i32* %4
  %5 = alloca i32
  store i32 %2, i32* %5
  %6 = alloca %class.Slice_int*
  %7 = load i32, i32* %4
  call void @_printInt(i32 %7)
  %8 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
  call void @_print(i8* %8)
  %9 = load i32, i32* %5
  call void @_printlnInt(i32 %9)
  %10 = alloca %class.Slice_int*
  %11 = call i8* @malloc(i32 12)
  %12 = bitcast i8* %11 to %class.Slice_int*
  call void @class.Slice_int.Slice_int(%class.Slice_int* %12)
  store %class.Slice_int* %12, %class.Slice_int** %10
  %13 = load %class.Slice_int*, %class.Slice_int** %10
  %14 = getelementptr %class.Slice_int, %class.Slice_int* %13, i32 0, i32 0
  %15 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %16 = load i32*, i32** %15
  store i32* %16, i32** %14
  %17 = load %class.Slice_int*, %class.Slice_int** %10
  %18 = getelementptr %class.Slice_int, %class.Slice_int* %17, i32 0, i32 1
  %19 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %20 = load i32, i32* %19
  %21 = load i32, i32* %4
  %22 = add i32 %20, %21
  store i32 %22, i32* %18
  %23 = load %class.Slice_int*, %class.Slice_int** %10
  %24 = getelementptr %class.Slice_int, %class.Slice_int* %23, i32 0, i32 2
  %25 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %26 = load i32, i32* %25
  %27 = load i32, i32* %5
  %28 = add i32 %26, %27
  store i32 %28, i32* %24
  %29 = alloca i1
  %30 = load %class.Slice_int*, %class.Slice_int** %10
  %31 = getelementptr %class.Slice_int, %class.Slice_int* %30, i32 0, i32 1
  %32 = load i32, i32* %31
  %33 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %34 = load i32*, i32** %33
  %35 = bitcast i32* %34 to i32*
  %36 = getelementptr i32, i32* %35, i32 -1
  %37 = load i32, i32* %36
  %38 = icmp sgt i32 %32, %37
  store i1 1, i1* %29
  br i1 %38, label %49, label %39

39:
  %40 = load %class.Slice_int*, %class.Slice_int** %10
  %41 = getelementptr %class.Slice_int, %class.Slice_int* %40, i32 0, i32 2
  %42 = load i32, i32* %41
  %43 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %44 = load i32*, i32** %43
  %45 = bitcast i32* %44 to i32*
  %46 = getelementptr i32, i32* %45, i32 -1
  %47 = load i32, i32* %46
  %48 = icmp sgt i32 %42, %47
  store i1 %48, i1* %29
  br label %49

49:
  %50 = load i1, i1* %29
  br i1 %50, label %51, label %53

51:
  %52 = getelementptr [40 x i8], [40 x i8]* @.strconst.1, i32 0, i32 0
  call void @_println(i8* %52)
  br label %53

53:
  %54 = load %class.Slice_int*, %class.Slice_int** %10
  store %class.Slice_int* %54, %class.Slice_int** %6
  br label %55

55:
  %56 = load %class.Slice_int*, %class.Slice_int** %6
  ret %class.Slice_int* %56
}

define %class.Slice_int* @class.Slice_int.tail(%class.Slice_int* %0) {
  %2 = alloca %class.Slice_int*
  %3 = call i32 @class.Slice_int.size(%class.Slice_int* %0)
  %4 = call %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %0, i32 1, i32 %3)
  store %class.Slice_int* %4, %class.Slice_int** %2
  br label %5

5:
  %6 = load %class.Slice_int*, %class.Slice_int** %2
  ret %class.Slice_int* %6
}

define void @class.Slice_int.copy(%class.Slice_int* %0, %class.Slice_int* %1) {
  %3 = alloca %class.Slice_int*
  store %class.Slice_int* %1, %class.Slice_int** %3
  %4 = call i32 @class.Slice_int.size(%class.Slice_int* %0)
  %5 = load %class.Slice_int*, %class.Slice_int** %3
  %6 = call i32 @class.Slice_int.size(%class.Slice_int* %5)
  %7 = icmp slt i32 %4, %6
  br i1 %7, label %8, label %10

8:
  %9 = getelementptr [46 x i8], [46 x i8]* @.strconst.2, i32 0, i32 0
  call void @_println(i8* %9)
  br label %10

10:
  %11 = alloca i32
  store i32 0, i32* %11
  br label %12

12:
  %13 = load i32, i32* %11
  %14 = load %class.Slice_int*, %class.Slice_int** %3
  %15 = call i32 @class.Slice_int.size(%class.Slice_int* %14)
  %16 = icmp slt i32 %13, %15
  br i1 %16, label %17, label %31

17:
  %18 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %19 = load i32*, i32** %18
  %20 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %21 = load i32, i32* %20
  %22 = load i32, i32* %11
  %23 = add i32 %21, %22
  %24 = getelementptr i32, i32* %19, i32 %23
  %25 = load %class.Slice_int*, %class.Slice_int** %3
  %26 = load i32, i32* %11
  %27 = call i32 @class.Slice_int.get(%class.Slice_int* %25, i32 %26)
  store i32 %27, i32* %24
  br label %28

28:
  %29 = load i32, i32* %11
  %30 = add i32 %29, 1
  store i32 %30, i32* %11
  br label %12

31:
  br label %32

32:
  ret void
}

define i32* @class.Slice_int.cloneArray(%class.Slice_int* %0) {
  %2 = alloca i32*
  %3 = alloca i32*
  %4 = call i32 @class.Slice_int.size(%class.Slice_int* %0)
  %5 = mul i32 4, %4
  %6 = add i32 %5, 4
  %7 = call i8* @malloc(i32 %6)
  %8 = bitcast i8* %7 to i32*
  store i32 %4, i32* %8
  %9 = getelementptr i32, i32* %8, i32 1
  %10 = bitcast i32* %9 to i32*
  store i32* %10, i32** %3
  %11 = alloca i32
  store i32 0, i32* %11
  br label %12

12:
  %13 = load i32, i32* %11
  %14 = call i32 @class.Slice_int.size(%class.Slice_int* %0)
  %15 = icmp slt i32 %13, %14
  br i1 %15, label %16, label %31

16:
  %17 = load i32*, i32** %3
  %18 = load i32, i32* %11
  %19 = getelementptr i32, i32* %17, i32 %18
  %20 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 0
  %21 = load i32*, i32** %20
  %22 = getelementptr %class.Slice_int, %class.Slice_int* %0, i32 0, i32 1
  %23 = load i32, i32* %22
  %24 = load i32, i32* %11
  %25 = add i32 %23, %24
  %26 = getelementptr i32, i32* %21, i32 %25
  %27 = load i32, i32* %26
  store i32 %27, i32* %19
  br label %28

28:
  %29 = load i32, i32* %11
  %30 = add i32 %29, 1
  store i32 %30, i32* %11
  br label %12

31:
  %32 = load i32*, i32** %3
  store i32* %32, i32** %2
  br label %33

33:
  %34 = load i32*, i32** %2
  ret i32* %34
}




define void @_init() {
  ret void
}

define %class.Slice_int* @global.makeSlice_int(i32* %0) {
  %2 = alloca i32*
  store i32* %0, i32** %2
  %3 = alloca %class.Slice_int*
  %4 = alloca %class.Slice_int*
  %5 = call i8* @malloc(i32 12)
  %6 = bitcast i8* %5 to %class.Slice_int*
  call void @class.Slice_int.Slice_int(%class.Slice_int* %6)
  store %class.Slice_int* %6, %class.Slice_int** %4
  %7 = load %class.Slice_int*, %class.Slice_int** %4
  %8 = load i32*, i32** %2
  call void @class.Slice_int.init(%class.Slice_int* %7, i32* %8)
  %9 = load %class.Slice_int*, %class.Slice_int** %4
  store %class.Slice_int* %9, %class.Slice_int** %3
  br label %10

10:
  %11 = load %class.Slice_int*, %class.Slice_int** %3
  ret %class.Slice_int* %11
}

define i32* @global.merge(%class.Slice_int* %0, %class.Slice_int* %1) {
  %3 = alloca %class.Slice_int*
  store %class.Slice_int* %0, %class.Slice_int** %3
  %4 = alloca %class.Slice_int*
  store %class.Slice_int* %1, %class.Slice_int** %4
  %5 = alloca i32*
  %6 = load %class.Slice_int*, %class.Slice_int** %3
  %7 = call i32 @class.Slice_int.size(%class.Slice_int* %6)
  %8 = icmp eq i32 %7, 0
  br i1 %8, label %9, label %12

9:
  %10 = load %class.Slice_int*, %class.Slice_int** %4
  %11 = call i32* @class.Slice_int.cloneArray(%class.Slice_int* %10)
  store i32* %11, i32** %5
  br label %65

12:
  %13 = load %class.Slice_int*, %class.Slice_int** %4
  %14 = call i32 @class.Slice_int.size(%class.Slice_int* %13)
  %15 = icmp eq i32 %14, 0
  br i1 %15, label %16, label %19

16:
  %17 = load %class.Slice_int*, %class.Slice_int** %3
  %18 = call i32* @class.Slice_int.cloneArray(%class.Slice_int* %17)
  store i32* %18, i32** %5
  br label %65

19:
  %20 = alloca i32*
  %21 = load %class.Slice_int*, %class.Slice_int** %3
  %22 = call i32 @class.Slice_int.size(%class.Slice_int* %21)
  %23 = load %class.Slice_int*, %class.Slice_int** %4
  %24 = call i32 @class.Slice_int.size(%class.Slice_int* %23)
  %25 = add i32 %22, %24
  %26 = mul i32 4, %25
  %27 = add i32 %26, 4
  %28 = call i8* @malloc(i32 %27)
  %29 = bitcast i8* %28 to i32*
  store i32 %25, i32* %29
  %30 = getelementptr i32, i32* %29, i32 1
  %31 = bitcast i32* %30 to i32*
  store i32* %31, i32** %20
  %32 = alloca %class.Slice_int*
  %33 = load i32*, i32** %20
  %34 = call %class.Slice_int* @global.makeSlice_int(i32* %33)
  %35 = call %class.Slice_int* @class.Slice_int.tail(%class.Slice_int* %34)
  store %class.Slice_int* %35, %class.Slice_int** %32
  %36 = load %class.Slice_int*, %class.Slice_int** %3
  %37 = call i32 @class.Slice_int.get(%class.Slice_int* %36, i32 0)
  %38 = load %class.Slice_int*, %class.Slice_int** %4
  %39 = call i32 @class.Slice_int.get(%class.Slice_int* %38, i32 0)
  %40 = icmp slt i32 %37, %39
  br i1 %40, label %41, label %52

41:
  %42 = load i32*, i32** %20
  %43 = getelementptr i32, i32* %42, i32 0
  %44 = load %class.Slice_int*, %class.Slice_int** %3
  %45 = call i32 @class.Slice_int.get(%class.Slice_int* %44, i32 0)
  store i32 %45, i32* %43
  %46 = load %class.Slice_int*, %class.Slice_int** %32
  %47 = load %class.Slice_int*, %class.Slice_int** %3
  %48 = call %class.Slice_int* @class.Slice_int.tail(%class.Slice_int* %47)
  %49 = load %class.Slice_int*, %class.Slice_int** %4
  %50 = call i32* @global.merge(%class.Slice_int* %48, %class.Slice_int* %49)
  %51 = call %class.Slice_int* @global.makeSlice_int(i32* %50)
  call void @class.Slice_int.copy(%class.Slice_int* %46, %class.Slice_int* %51)
  br label %63

52:
  %53 = load i32*, i32** %20
  %54 = getelementptr i32, i32* %53, i32 0
  %55 = load %class.Slice_int*, %class.Slice_int** %4
  %56 = call i32 @class.Slice_int.get(%class.Slice_int* %55, i32 0)
  store i32 %56, i32* %54
  %57 = load %class.Slice_int*, %class.Slice_int** %32
  %58 = load %class.Slice_int*, %class.Slice_int** %3
  %59 = load %class.Slice_int*, %class.Slice_int** %4
  %60 = call %class.Slice_int* @class.Slice_int.tail(%class.Slice_int* %59)
  %61 = call i32* @global.merge(%class.Slice_int* %58, %class.Slice_int* %60)
  %62 = call %class.Slice_int* @global.makeSlice_int(i32* %61)
  call void @class.Slice_int.copy(%class.Slice_int* %57, %class.Slice_int* %62)
  br label %63

63:
  %64 = load i32*, i32** %20
  store i32* %64, i32** %5
  br label %65

65:
  %66 = load i32*, i32** %5
  ret i32* %66
}

define void @global.mergeSort(%class.Slice_int* %0) {
  %2 = alloca %class.Slice_int*
  store %class.Slice_int* %0, %class.Slice_int** %2
  %3 = load %class.Slice_int*, %class.Slice_int** %2
  %4 = call i32 @class.Slice_int.size(%class.Slice_int* %3)
  %5 = icmp eq i32 %4, 1
  br i1 %5, label %6, label %7

6:
  br label %32

7:
  %8 = alloca i32
  %9 = load %class.Slice_int*, %class.Slice_int** %2
  %10 = call i32 @class.Slice_int.size(%class.Slice_int* %9)
  store i32 %10, i32* %8
  %11 = alloca i32
  %12 = load i32, i32* %8
  %13 = sdiv i32 %12, 2
  store i32 %13, i32* %11
  %14 = load i32, i32* %11
  call void @_printlnInt(i32 %14)
  %15 = load %class.Slice_int*, %class.Slice_int** %2
  %16 = load i32, i32* %11
  %17 = call %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %15, i32 0, i32 %16)
  call void @global.mergeSort(%class.Slice_int* %17)
  %18 = load %class.Slice_int*, %class.Slice_int** %2
  %19 = load i32, i32* %11
  %20 = load i32, i32* %8
  %21 = call %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %18, i32 %19, i32 %20)
  call void @global.mergeSort(%class.Slice_int* %21)
  %22 = load %class.Slice_int*, %class.Slice_int** %2
  %23 = load %class.Slice_int*, %class.Slice_int** %2
  %24 = load i32, i32* %11
  %25 = call %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %23, i32 0, i32 %24)
  %26 = load %class.Slice_int*, %class.Slice_int** %2
  %27 = load i32, i32* %11
  %28 = load i32, i32* %8
  %29 = call %class.Slice_int* @class.Slice_int.slice(%class.Slice_int* %26, i32 %27, i32 %28)
  %30 = call i32* @global.merge(%class.Slice_int* %25, %class.Slice_int* %29)
  %31 = call %class.Slice_int* @global.makeSlice_int(i32* %30)
  call void @class.Slice_int.copy(%class.Slice_int* %22, %class.Slice_int* %31)
  br label %32

32:
  ret void
}

define void @global.mergeSortInf(i32* %0) {
  %2 = alloca i32*
  store i32* %0, i32** %2
  %3 = load i32*, i32** %2
  %4 = call %class.Slice_int* @global.makeSlice_int(i32* %3)
  call void @global.mergeSort(%class.Slice_int* %4)
  br label %5

5:
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca i32
  %3 = call i32 @_getInt()
  store i32 %3, i32* %2
  %4 = alloca i32*
  %5 = load i32, i32* %2
  %6 = mul i32 4, %5
  %7 = add i32 %6, 4
  %8 = call i8* @malloc(i32 %7)
  %9 = bitcast i8* %8 to i32*
  store i32 %5, i32* %9
  %10 = getelementptr i32, i32* %9, i32 1
  %11 = bitcast i32* %10 to i32*
  store i32* %11, i32** %4
  %12 = alloca i32
  store i32 0, i32* %12
  br label %13

13:
  %14 = load i32, i32* %12
  %15 = load i32, i32* %2
  %16 = icmp slt i32 %14, %15
  br i1 %16, label %17, label %25

17:
  %18 = load i32*, i32** %4
  %19 = load i32, i32* %12
  %20 = getelementptr i32, i32* %18, i32 %19
  %21 = call i32 @_getInt()
  store i32 %21, i32* %20
  br label %22

22:
  %23 = load i32, i32* %12
  %24 = add i32 %23, 1
  store i32 %24, i32* %12
  br label %13

25:
  %26 = load i32*, i32** %4
  call void @global.mergeSortInf(i32* %26)
  store i32 0, i32* %12
  br label %27

27:
  %28 = load i32, i32* %12
  %29 = load i32, i32* %2
  %30 = icmp slt i32 %28, %29
  br i1 %30, label %31, label %42

31:
  %32 = load i32*, i32** %4
  %33 = load i32, i32* %12
  %34 = getelementptr i32, i32* %32, i32 %33
  %35 = load i32, i32* %34
  %36 = call i8* @_toString(i32 %35)
  %37 = getelementptr [2 x i8], [2 x i8]* @.strconst.3, i32 0, i32 0
  %38 = call i8* @_string_add(i8* %36, i8* %37)
  call void @_print(i8* %38)
  br label %39

39:
  %40 = load i32, i32* %12
  %41 = add i32 %40, 1
  store i32 %41, i32* %12
  br label %27

42:
  %43 = getelementptr [1 x i8], [1 x i8]* @.strconst.4, i32 0, i32 0
  call void @_println(i8* %43)
  store i32 0, i32* %1
  br label %44

44:
  %45 = load i32, i32* %1
  ret i32 %45
}
















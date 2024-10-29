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

@global.n = global i32 0
@global.a = global i32* null
@.strconst.0 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.1 = private unnamed_addr constant [2 x i8] c"\0A\00"




define void @_init() {
-1  ret void
}

define void @global.exchange(i32 %0, i32 %1) {
-1  %3 = alloca i32
-1  store i32 %0, i32* %3
-1  %4 = alloca i32
-1  store i32 %1, i32* %4
-1  %5 = alloca i32
-1  store i32 0, i32* %5
-1  %6 = load i32*, i32** @global.a
-1  %7 = load i32, i32* %3
-1  %8 = getelementptr i32, i32* %6, i32 %7
-1  %9 = load i32, i32* %8
-1  store i32 %9, i32* %5
-1  %10 = load i32*, i32** @global.a
-1  %11 = load i32, i32* %3
-1  %12 = getelementptr i32, i32* %10, i32 %11
-1  %13 = load i32*, i32** @global.a
-1  %14 = load i32, i32* %4
-1  %15 = getelementptr i32, i32* %13, i32 %14
-1  %16 = load i32, i32* %15
-1  store i32 %16, i32* %12
-1  %17 = load i32*, i32** @global.a
-1  %18 = load i32, i32* %4
-1  %19 = getelementptr i32, i32* %17, i32 %18
-1  %20 = load i32, i32* %5
-1  store i32 %20, i32* %19
-1  br label %21

21:
-1  ret void
}

define i32 @global.makeHeap() {
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = alloca i32
-1  store i32 0, i32* %3
-1  %4 = alloca i32
-1  store i32 0, i32* %4
-1  %5 = load i32, i32* @global.n
-1  %6 = sub i32 %5, 1
-1  %7 = sdiv i32 %6, 2
-1  store i32 %7, i32* %2
-1  store i32 0, i32* %3
-1  store i32 0, i32* %4
-1  br label %8

8:
-1  %9 = load i32, i32* %2
-1  %10 = icmp sge i32 %9, 0
-1  br i1 %10, label %11, label %55

11:
-1  %12 = load i32, i32* %2
-1  %13 = mul i32 %12, 2
-1  store i32 %13, i32* %4
-1  %14 = alloca i1
-1  %15 = load i32, i32* %2
-1  %16 = mul i32 %15, 2
-1  %17 = add i32 %16, 1
-1  %18 = load i32, i32* @global.n
-1  %19 = icmp slt i32 %17, %18
-1  store i1 0, i1* %14
-1  br i1 %19, label %20, label %33

20:
-1  %21 = load i32*, i32** @global.a
-1  %22 = load i32, i32* %2
-1  %23 = mul i32 %22, 2
-1  %24 = add i32 %23, 1
-1  %25 = getelementptr i32, i32* %21, i32 %24
-1  %26 = load i32, i32* %25
-1  %27 = load i32*, i32** @global.a
-1  %28 = load i32, i32* %2
-1  %29 = mul i32 %28, 2
-1  %30 = getelementptr i32, i32* %27, i32 %29
-1  %31 = load i32, i32* %30
-1  %32 = icmp slt i32 %26, %31
-1  store i1 %32, i1* %14
-1  br label %33

33:
-1  %34 = load i1, i1* %14
-1  br i1 %34, label %35, label %39

35:
-1  %36 = load i32, i32* %2
-1  %37 = mul i32 %36, 2
-1  %38 = add i32 %37, 1
-1  store i32 %38, i32* %4
-1  br label %39

39:
-1  %40 = load i32*, i32** @global.a
-1  %41 = load i32, i32* %2
-1  %42 = getelementptr i32, i32* %40, i32 %41
-1  %43 = load i32, i32* %42
-1  %44 = load i32*, i32** @global.a
-1  %45 = load i32, i32* %4
-1  %46 = getelementptr i32, i32* %44, i32 %45
-1  %47 = load i32, i32* %46
-1  %48 = icmp sgt i32 %43, %47
-1  br i1 %48, label %49, label %52

49:
-1  %50 = load i32, i32* %2
-1  %51 = load i32, i32* %4
-1  call void @global.exchange(i32 %50, i32 %51)
-1  br label %52

52:
-1  %53 = load i32, i32* %2
-1  %54 = sub i32 %53, 1
-1  store i32 %54, i32* %2
-1  br label %8

55:
-1  store i32 0, i32* %1
-1  br label %56

56:
-1  %57 = load i32, i32* %1
-1  ret i32 %57
}

define i32 @global.adjustHeap(i32 %0) {
-1  %2 = alloca i32
-1  store i32 %0, i32* %2
-1  %3 = alloca i32
-1  store i32 0, i32* %3
-1  %4 = alloca i32
-1  store i32 0, i32* %4
-1  store i32 0, i32* %4
-1  %5 = alloca i32
-1  store i32 0, i32* %5
-1  store i32 0, i32* %5
-1  %6 = alloca i32
-1  store i32 0, i32* %6
-1  store i32 0, i32* %6
-1  br label %7

7:
-1  %8 = load i32, i32* %4
-1  %9 = mul i32 %8, 2
-1  %10 = load i32, i32* %2
-1  %11 = icmp slt i32 %9, %10
-1  br i1 %11, label %12, label %70

12:
-1  %13 = load i32, i32* %4
-1  %14 = mul i32 %13, 2
-1  store i32 %14, i32* %5
-1  %15 = alloca i1
-1  %16 = load i32, i32* %4
-1  %17 = mul i32 %16, 2
-1  %18 = add i32 %17, 1
-1  %19 = load i32, i32* %2
-1  %20 = icmp slt i32 %18, %19
-1  store i1 0, i1* %15
-1  br i1 %20, label %21, label %34

21:
-1  %22 = load i32*, i32** @global.a
-1  %23 = load i32, i32* %4
-1  %24 = mul i32 %23, 2
-1  %25 = add i32 %24, 1
-1  %26 = getelementptr i32, i32* %22, i32 %25
-1  %27 = load i32, i32* %26
-1  %28 = load i32*, i32** @global.a
-1  %29 = load i32, i32* %4
-1  %30 = mul i32 %29, 2
-1  %31 = getelementptr i32, i32* %28, i32 %30
-1  %32 = load i32, i32* %31
-1  %33 = icmp slt i32 %27, %32
-1  store i1 %33, i1* %15
-1  br label %34

34:
-1  %35 = load i1, i1* %15
-1  br i1 %35, label %36, label %40

36:
-1  %37 = load i32, i32* %4
-1  %38 = mul i32 %37, 2
-1  %39 = add i32 %38, 1
-1  store i32 %39, i32* %5
-1  br label %40

40:
-1  %41 = load i32*, i32** @global.a
-1  %42 = load i32, i32* %4
-1  %43 = getelementptr i32, i32* %41, i32 %42
-1  %44 = load i32, i32* %43
-1  %45 = load i32*, i32** @global.a
-1  %46 = load i32, i32* %5
-1  %47 = getelementptr i32, i32* %45, i32 %46
-1  %48 = load i32, i32* %47
-1  %49 = icmp sgt i32 %44, %48
-1  br i1 %49, label %50, label %68

50:
-1  %51 = alloca i32
-1  store i32 0, i32* %51
-1  %52 = load i32*, i32** @global.a
-1  %53 = load i32, i32* %4
-1  %54 = getelementptr i32, i32* %52, i32 %53
-1  %55 = load i32, i32* %54
-1  store i32 %55, i32* %51
-1  %56 = load i32*, i32** @global.a
-1  %57 = load i32, i32* %4
-1  %58 = getelementptr i32, i32* %56, i32 %57
-1  %59 = load i32*, i32** @global.a
-1  %60 = load i32, i32* %5
-1  %61 = getelementptr i32, i32* %59, i32 %60
-1  %62 = load i32, i32* %61
-1  store i32 %62, i32* %58
-1  %63 = load i32*, i32** @global.a
-1  %64 = load i32, i32* %5
-1  %65 = getelementptr i32, i32* %63, i32 %64
-1  %66 = load i32, i32* %51
-1  store i32 %66, i32* %65
-1  %67 = load i32, i32* %5
-1  store i32 %67, i32* %4
-1  br label %69

68:
-1  br label %70

69:
-1  br label %7

70:
-1  store i32 0, i32* %3
-1  br label %71

71:
-1  %72 = load i32, i32* %3
-1  ret i32 %72
}

define i32 @global.heapSort() {
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = alloca i32
-1  store i32 0, i32* %3
-1  store i32 0, i32* %2
-1  store i32 0, i32* %3
-1  br label %4

4:
-1  %5 = load i32, i32* %3
-1  %6 = load i32, i32* @global.n
-1  %7 = icmp slt i32 %5, %6
-1  br i1 %7, label %8, label %36

8:
-1  %9 = load i32*, i32** @global.a
-1  %10 = getelementptr i32, i32* %9, i32 0
-1  %11 = load i32, i32* %10
-1  store i32 %11, i32* %2
-1  %12 = load i32*, i32** @global.a
-1  %13 = getelementptr i32, i32* %12, i32 0
-1  %14 = load i32*, i32** @global.a
-1  %15 = load i32, i32* @global.n
-1  %16 = load i32, i32* %3
-1  %17 = sub i32 %15, %16
-1  %18 = sub i32 %17, 1
-1  %19 = getelementptr i32, i32* %14, i32 %18
-1  %20 = load i32, i32* %19
-1  store i32 %20, i32* %13
-1  %21 = load i32*, i32** @global.a
-1  %22 = load i32, i32* @global.n
-1  %23 = load i32, i32* %3
-1  %24 = sub i32 %22, %23
-1  %25 = sub i32 %24, 1
-1  %26 = getelementptr i32, i32* %21, i32 %25
-1  %27 = load i32, i32* %2
-1  store i32 %27, i32* %26
-1  %28 = load i32, i32* @global.n
-1  %29 = load i32, i32* %3
-1  %30 = sub i32 %28, %29
-1  %31 = sub i32 %30, 1
-1  %32 = call i32 @global.adjustHeap(i32 %31)
-1  br label %33

33:
-1  %34 = load i32, i32* %3
-1  %35 = add i32 %34, 1
-1  store i32 %35, i32* %3
-1  br label %4

36:
-1  store i32 0, i32* %1
-1  br label %37

37:
-1  %38 = load i32, i32* %1
-1  ret i32 %38
}

define i32 @main() {
-1  call void @_init()
-1  %1 = alloca i32
-1  store i32 0, i32* %1
-1  %2 = alloca i32
-1  store i32 0, i32* %2
-1  %3 = call i8* @_getString()
-1  %4 = call i32 @_string_parseInt(i8* %3)
-1  store i32 %4, i32* @global.n
-1  %5 = load i32, i32* @global.n
-1  %6 = mul i32 4, %5
-1  %7 = add i32 %6, 4
-1  %8 = call i8* @malloc(i32 %7)
-1  %9 = bitcast i8* %8 to i32*
-1  store i32 %5, i32* %9
-1  %10 = getelementptr i32, i32* %9, i32 1
-1  %11 = bitcast i32* %10 to i32*
-1  store i32* %11, i32** @global.a
-1  store i32 0, i32* %2
-1  br label %12

12:
-1  %13 = load i32, i32* %2
-1  %14 = load i32*, i32** @global.a
-1  %15 = bitcast i32* %14 to i32*
-1  %16 = getelementptr i32, i32* %15, i32 -1
-1  %17 = load i32, i32* %16
-1  %18 = icmp slt i32 %13, %17
-1  br i1 %18, label %19, label %27

19:
-1  %20 = load i32*, i32** @global.a
-1  %21 = load i32, i32* %2
-1  %22 = getelementptr i32, i32* %20, i32 %21
-1  %23 = load i32, i32* %2
-1  store i32 %23, i32* %22
-1  br label %24

24:
-1  %25 = load i32, i32* %2
-1  %26 = add i32 %25, 1
-1  store i32 %26, i32* %2
-1  br label %12

27:
-1  %28 = call i32 @global.makeHeap()
-1  %29 = call i32 @global.heapSort()
-1  store i32 0, i32* %2
-1  br label %30

30:
-1  %31 = load i32, i32* %2
-1  %32 = load i32*, i32** @global.a
-1  %33 = bitcast i32* %32 to i32*
-1  %34 = getelementptr i32, i32* %33, i32 -1
-1  %35 = load i32, i32* %34
-1  %36 = icmp slt i32 %31, %35
-1  br i1 %36, label %37, label %48

37:
-1  %38 = load i32*, i32** @global.a
-1  %39 = load i32, i32* %2
-1  %40 = getelementptr i32, i32* %38, i32 %39
-1  %41 = load i32, i32* %40
-1  %42 = call i8* @_toString(i32 %41)
-1  %43 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
-1  %44 = call i8* @_string_add(i8* %42, i8* %43)
-1  call void @_print(i8* %44)
-1  br label %45

45:
-1  %46 = load i32, i32* %2
-1  %47 = add i32 %46, 1
-1  store i32 %47, i32* %2
-1  br label %30

48:
-1  %49 = getelementptr [2 x i8], [2 x i8]* @.strconst.1, i32 0, i32 0
-1  call void @_print(i8* %49)
-1  store i32 0, i32* %1
-1  br label %50

50:
-1  %51 = load i32, i32* %1
-1  ret i32 %51
}
















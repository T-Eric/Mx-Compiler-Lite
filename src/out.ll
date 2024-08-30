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
@global.p = global i32 0
@global.k = global i32 0
@global.i = global i32 0
@.strconst.0 = private unnamed_addr constant [4 x i8] c"<< \00"
@.strconst.1 = private unnamed_addr constant [2 x i8] c"(\00"
@.strconst.2 = private unnamed_addr constant [3 x i8] c") \00"
@.strconst.3 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.4 = private unnamed_addr constant [4 x i8] c">> \00"




define void @_init() {
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = call i32 @_getInt()
  store i32 %2, i32* @global.n
  %3 = call i32 @_getInt()
  store i32 %3, i32* @global.p
  %4 = call i32 @_getInt()
  store i32 %4, i32* @global.k
  %5 = load i32, i32* @global.p
  %6 = load i32, i32* @global.k
  %7 = sub i32 %5, %6
  %8 = icmp sgt i32 %7, 1
  br i1 %8, label %9, label %11

9:
  %10 = getelementptr [4 x i8], [4 x i8]* @.strconst.0, i32 0, i32 0
  call void @_print(i8* %10)
  br label %11

11:
  %12 = load i32, i32* @global.p
  %13 = load i32, i32* @global.k
  %14 = sub i32 %12, %13
  store i32 %14, i32* @global.i
  br label %15

15:
  %16 = load i32, i32* @global.i
  %17 = load i32, i32* @global.p
  %18 = load i32, i32* @global.k
  %19 = add i32 %17, %18
  %20 = icmp sle i32 %16, %19
  br i1 %20, label %21, label %48

21:
  %22 = alloca i1
  %23 = load i32, i32* @global.i
  %24 = icmp sle i32 1, %23
  store i1 0, i1* %22
  br i1 %24, label %25, label %29

25:
  %26 = load i32, i32* @global.i
  %27 = load i32, i32* @global.n
  %28 = icmp sle i32 %26, %27
  store i1 %28, i1* %22
  br label %29

29:
  %30 = load i1, i1* %22
  br i1 %30, label %31, label %44

31:
  %32 = load i32, i32* @global.i
  %33 = load i32, i32* @global.p
  %34 = icmp eq i32 %32, %33
  br i1 %34, label %35, label %40

35:
  %36 = getelementptr [2 x i8], [2 x i8]* @.strconst.1, i32 0, i32 0
  call void @_print(i8* %36)
  %37 = load i32, i32* @global.i
  %38 = call i8* @_toString(i32 %37)
  call void @_print(i8* %38)
  %39 = getelementptr [3 x i8], [3 x i8]* @.strconst.2, i32 0, i32 0
  call void @_print(i8* %39)
  br label %43

40:
  %41 = load i32, i32* @global.i
  call void @_printInt(i32 %41)
  %42 = getelementptr [2 x i8], [2 x i8]* @.strconst.3, i32 0, i32 0
  call void @_print(i8* %42)
  br label %43

43:
  br label %44

44:
  br label %45

45:
  %46 = load i32, i32* @global.i
  %47 = add i32 %46, 1
  store i32 %47, i32* @global.i
  br label %15

48:
  %49 = load i32, i32* @global.p
  %50 = load i32, i32* @global.k
  %51 = add i32 %49, %50
  %52 = load i32, i32* @global.n
  %53 = icmp slt i32 %51, %52
  br i1 %53, label %54, label %56

54:
  %55 = getelementptr [4 x i8], [4 x i8]* @.strconst.4, i32 0, i32 0
  call void @_print(i8* %55)
  br label %56

56:
  store i32 0, i32* %1
  br label %57

57:
  %58 = load i32, i32* %1
  ret i32 %58
}
















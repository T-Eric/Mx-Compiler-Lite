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

@.strconst.0 = private unnamed_addr constant [4 x i8] c"abc\00"




define void @_init() {
  ret void
}

define i32 @global.f() {
  %1 = alloca i32
  %2 = alloca i32*
  %3 = alloca i32, i32 3
  store i32 2, i32* %3
  %4 = getelementptr i32, i32* %3, i32 1
  store i32 3, i32* %4
  %5 = getelementptr i32, i32* %3, i32 2
  store i32 3, i32* %5
  %6 = call i8* @_new_array(i32 4, i32 3, i32* %3)
  %7 = bitcast i8* %6 to i32***
  %8 = load i32**, i32*** %7
  %9 = getelementptr i32*, i32** %8, i32 0
  %10 = load i32*, i32** %9
  %11 = getelementptr i32, i32* %10, i32 1
  %12 = load i32, i32* %11
  store i32 %12, i32* %2
  %13 = alloca i8*
  %14 = mul i32 4, 3
  %15 = add i32 %14, 4
  %16 = call i8* @malloc(i32 %15)
  %17 = bitcast i8* %16 to i32*
  store i32 3, i32* %17
  %18 = getelementptr i32, i32* %17, i32 1
  %19 = bitcast i32* %18 to i8**
  %20 = load i8*, i8** %19
  %21 = getelementptr i8, i8* %20, i32 0
  store i8* %21, i8** %13
  %22 = alloca i32
  %23 = alloca i32, i32 3
  store i32 3, i32* %23
  %24 = getelementptr i32, i32* %23, i32 1
  store i32 3, i32* %24
  %25 = getelementptr i32, i32* %23, i32 2
  store i32 3, i32* %25
  %26 = call i8* @_new_array(i32 4, i32 3, i32* %23)
  %27 = bitcast i8* %26 to i8****
  %28 = load i8***, i8**** %27
  %29 = getelementptr i8**, i8*** %28, i32 0
  %30 = load i8**, i8*** %29
  %31 = getelementptr i8*, i8** %30, i32 1
  %32 = load i8*, i8** %31
  %33 = getelementptr i8, i8* %32, i32 2
  %34 = bitcast i8* %33 to i32*
  %35 = getelementptr i32, i32* %34, i32 -1
  %36 = load i32, i32* %35
  store i32 %36, i32* %22
  %37 = load i32, i32* %22
  store i32 %37, i32* %1
  br label %38

38:
  %39 = load i32, i32* %1
  ret i32 %39
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca i8*
  %3 = getelementptr [4 x i8], [4 x i8]* @.strconst.0, i32 0, i32 0
  store i8* %3, i8** %2
  %4 = load i8*, i8** %2
  call void @_print(i8* %4)
  %5 = load i8*, i8** %2
  %6 = bitcast i8* %5 to i32*
  %7 = getelementptr i32, i32* %6, i32 -1
  %8 = load i32, i32* %7
  %9 = call i8* @_toString(i32 %8)
  call void @_println(i8* %9)
  %10 = call i8* @_getString()
  call void @_print(i8* %10)
  %11 = call i8* @_toString(i32 3)
  call void @_print(i8* %11)
  %12 = alloca i8*
  %13 = load i8*, i8** %2
  %14 = call i8* @_string_substring(i8* %13, i32 0, i32 10)
  store i8* %14, i8** %12
  %15 = alloca i32
  %16 = load i8*, i8** %2
  %17 = call i32 @_string_parseInt(i8* %16)
  store i32 %17, i32* %15
  %18 = alloca i32
  %19 = load i8*, i8** %2
  %20 = call i32 @_string_ord(i8* %19, i32 3)
  store i32 %20, i32* %18
  %21 = alloca i8*
  %22 = load i32, i32* %18
  store i32 %22, i32* %1
  %23 = call i32 @_getInt()
  store i32 %23, i32* %1
  br label %24

24:
  %25 = load i32, i32* %1
  ret i32 %25
}
















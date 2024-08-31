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
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca i32*
  %3 = call i8* @malloc(i32 20)
  %4 = bitcast i8* %3 to i32*
  store i32 4, i32* %4
  %5 = getelementptr i32, i32* %4, i32 1
  %6 = bitcast i32* %5 to i32*
  %7 = getelementptr i32, i32* %6, i32 0
  store i32 2, i32* %7
  %8 = getelementptr i32, i32* %6, i32 1
  store i32 0, i32* %8
  %9 = getelementptr i32, i32* %6, i32 2
  store i32 2, i32* %9
  %10 = getelementptr i32, i32* %6, i32 3
  store i32 4, i32* %10
  store i32* %6, i32** %2
  %11 = alloca i32
  %12 = load i32*, i32** %2
  %13 = bitcast i32* %12 to i32*
  %14 = getelementptr i32, i32* %13, i32 -1
  %15 = load i32, i32* %14
  store i32 %15, i32* %11
  %16 = alloca i32
  store i32 0, i32* %16
  br label %17

17:
  %18 = load i32, i32* %16
  %19 = load i32, i32* %11
  %20 = icmp slt i32 %18, %19
  br i1 %20, label %21, label %29

21:
  %22 = load i32*, i32** %2
  %23 = load i32, i32* %16
  %24 = getelementptr i32, i32* %22, i32 %23
  %25 = load i32, i32* %24
  call void @_printlnInt(i32 %25)
  br label %26

26:
  %27 = load i32, i32* %16
  %28 = add i32 %27, 1
  store i32 %28, i32* %16
  br label %17

29:
  store i32 0, i32* %1
  br label %30

30:
  %31 = load i32, i32* %1
  ret i32 %31
}
















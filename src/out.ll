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


%class.A = type { i32 }

define void @class.A.A(%class.A* %0) {
  ret void
}

define i32 @class.A.f(%class.A* %0) {
  %2 = alloca i32
  %3 = getelementptr %class.A, %class.A* %0, i32 0, i32 0
  %4 = load i32, i32* %3
  store i32 %4, i32* %2
  br label %5

5:
  %6 = load i32, i32* %2
  ret i32 %6
}

define %class.A* @class.A.copy(%class.A* %0) {
  %2 = alloca %class.A*
  store %class.A* %0, %class.A** %2
  br label %3

3:
  %4 = load %class.A*, %class.A** %2
  ret %class.A* %4
}




define void @_init() {
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca %class.A*
  %3 = call i8* @malloc(i32 4)
  %4 = bitcast i8* %3 to %class.A*
  call void @class.A.A(%class.A* %4)
  store %class.A* %4, %class.A** %2
  %5 = alloca %class.A*
  %6 = load %class.A*, %class.A** %2
  %7 = call i32 @class.A.f(%class.A* %6)
  %8 = icmp slt i32 %7, 0
  %9 = alloca i32
  br i1 %8, label %1, label %2

1:
  store i32 null, i32* %9
  br label %0

2:
  %10 = load %class.A*, %class.A** %2
  %11 = call %class.A* @class.A.copy(%class.A* %10)
  store %class.A* %11, %class.A** %9
  br label %0

0:
  %12 = load i32, i32* %9
  store i32 %12, i32* %5
  %13 = alloca %class.A*
  %14 = load %class.A*, %class.A** %5
  %15 = call i32 @class.A.f(%class.A* %14)
  %16 = icmp sgt i32 %15, 0
  %17 = alloca %class.A*
  br i1 %16, label %18, label %21

18:
  %19 = load %class.A*, %class.A** %5
  %20 = call %class.A* @class.A.copy(%class.A* %19)
  store %class.A* %20, %class.A** %17
  br label %3

21:
  store i32 null, i32* %17
  br label %3

3:
  %22 = load %class.A*, %class.A** %17
  store %class.A* %22, %class.A** %13
  %23 = alloca i32*
  %24 = mul i32 4, 10
  %25 = add i32 %24, 4
  %26 = call i8* @malloc(i32 %25)
  %27 = bitcast i8* %26 to i32*
  store i32 10, i32* %27
  %28 = getelementptr i32, i32* %27, i32 1
  %29 = bitcast i32* %28 to i32*
  store i32* %29, i32** %23
  %30 = alloca i32*
  %31 = load i32*, i32** %23
  %32 = getelementptr i32, i32* %31, i32 9
  %33 = load i32, i32* %32
  %34 = load i32*, i32** %23
  %35 = getelementptr i32, i32* %34, i32 1
  %36 = load i32, i32* %35
  %37 = icmp sgt i32 %33, %36
  %38 = alloca i32*
  br i1 %37, label %39, label %41

39:
  %40 = load i32*, i32** %23
  store i32* %40, i32** %38
  br label %42

41:
  store i32 null, i32* %38
  br label %42

42:
  %43 = load i32*, i32** %38
  store i32* %43, i32** %30
  %44 = load %class.A*, %class.A** %13
  %45 = call %class.A* @class.A.copy(%class.A* %44)
  %46 = call %class.A* @class.A.copy(%class.A* %45)
  %47 = call %class.A* @class.A.copy(%class.A* %46)
  %48 = call %class.A* @class.A.copy(%class.A* %47)
  %49 = call i32 @class.A.f(%class.A* %48)
  %50 = load i32*, i32** %30
  %51 = getelementptr i32, i32* %50, i32 0
  %52 = load i32, i32* %51
  %53 = add i32 %49, %52
  store i32 %53, i32* %1
  br label %54

54:
  %55 = load i32, i32* %1
  ret i32 %55
}
















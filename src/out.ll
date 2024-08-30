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

@global.N = global i32 0
@global.b = global i1* null
@global.resultCount = global i32 0
@.strconst.0 = private unnamed_addr constant [2 x i8] c" \00"
@.strconst.1 = private unnamed_addr constant [8 x i8] c"Total: \00"




define void @_init() {
  store i32 15000, i32* @global.N
  %1 = mul i32 1, 15001
  %2 = add i32 %1, 4
  %3 = call i8* @malloc(i32 %2)
  %4 = bitcast i8* %3 to i32*
  store i32 15001, i32* %4
  %5 = getelementptr i32, i32* %4, i32 1
  %6 = bitcast i32* %5 to i1*
  store i1* %6, i1** @global.b
  store i32 0, i32* @global.resultCount
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca i32
  store i32 1, i32* %2
  br label %3

3:
  %4 = load i32, i32* %2
  %5 = load i32, i32* @global.N
  %6 = icmp sle i32 %4, %5
  br i1 %6, label %7, label %14

7:
  %8 = load i1*, i1** @global.b
  %9 = load i32, i32* %2
  %10 = getelementptr i1, i1* %8, i32 %9
  store i1 1, i1* %10
  br label %11

11:
  %12 = load i32, i32* %2
  %13 = add i32 %12, 1
  store i32 %13, i32* %2
  br label %3

14:
  store i32 2, i32* %2
  br label %15

15:
  %16 = load i32, i32* %2
  %17 = load i32, i32* @global.N
  %18 = icmp sle i32 %16, %17
  br i1 %18, label %19, label %68

19:
  %20 = load i1*, i1** @global.b
  %21 = load i32, i32* %2
  %22 = getelementptr i1, i1* %20, i32 %21
  %23 = load i1, i1* %22
  br i1 %23, label %24, label %64

24:
  %25 = alloca i32
  store i32 2, i32* %25
  %26 = alloca i1
  %27 = load i32, i32* %2
  %28 = icmp sgt i32 %27, 3
  store i1 0, i1* %26
  br i1 %28, label %29, label %35

29:
  %30 = load i1*, i1** @global.b
  %31 = load i32, i32* %2
  %32 = sub i32 %31, 2
  %33 = getelementptr i1, i1* %30, i32 %32
  %34 = load i1, i1* %33
  store i1 %34, i1* %26
  br label %35

35:
  %36 = load i1, i1* %26
  br i1 %36, label %37, label %48

37:
  %38 = load i32, i32* @global.resultCount
  %39 = add i32 %38, 1
  store i32 %39, i32* @global.resultCount
  %40 = load i32, i32* %2
  %41 = sub i32 %40, 2
  %42 = call i8* @_toString(i32 %41)
  %43 = getelementptr [2 x i8], [2 x i8]* @.strconst.0, i32 0, i32 0
  %44 = call i8* @_string_add(i8* %42, i8* %43)
  %45 = load i32, i32* %2
  %46 = call i8* @_toString(i32 %45)
  %47 = call i8* @_string_add(i8* %44, i8* %46)
  call void @_println(i8* %47)
  br label %48

48:
  br label %49

49:
  %50 = load i32, i32* %2
  %51 = load i32, i32* %25
  %52 = mul i32 %50, %51
  %53 = load i32, i32* @global.N
  %54 = icmp sle i32 %52, %53
  br i1 %54, label %55, label %63

55:
  %56 = load i1*, i1** @global.b
  %57 = load i32, i32* %2
  %58 = load i32, i32* %25
  %59 = mul i32 %57, %58
  %60 = getelementptr i1, i1* %56, i32 %59
  store i1 0, i1* %60
  %61 = load i32, i32* %25
  %62 = add i32 %61, 1
  store i32 %62, i32* %25
  br label %49

63:
  br label %64

64:
  br label %65

65:
  %66 = load i32, i32* %2
  %67 = add i32 %66, 1
  store i32 %67, i32* %2
  br label %15

68:
  %69 = getelementptr [8 x i8], [8 x i8]* @.strconst.1, i32 0, i32 0
  %70 = load i32, i32* @global.resultCount
  %71 = call i8* @_toString(i32 %70)
  %72 = call i8* @_string_add(i8* %69, i8* %71)
  call void @_println(i8* %72)
  store i32 0, i32* %1
  br label %73

73:
  %74 = load i32, i32* %1
  ret i32 %74
}
















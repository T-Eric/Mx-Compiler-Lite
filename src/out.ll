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

@global.a = global i32* null
@.strconst.0 = private unnamed_addr constant [1 x i8] c"\00"




define void @_init() {
  %1 = mul i32 4, 4
  %2 = add i32 %1, 4
  %3 = call i8* @malloc(i32 %2)
  %4 = bitcast i8* %3 to i32*
  store i32 4, i32* %4
  %5 = getelementptr i32, i32* %4, i32 1
  %6 = bitcast i32* %5 to i32*
  store i32* %6, i32** @global.a
  ret void
}

define i32 @main() {
  call void @_init()
  %1 = alloca i32
  %2 = alloca i32**
  %3 = alloca i32, i32 2
  store i32 4, i32* %3
  %4 = getelementptr i32, i32* %3, i32 1
  store i32 0, i32* %4
  %5 = call i8* @_new_array(i32 4, i32 2, i32* %3)
  %6 = bitcast i8* %5 to i32**
  store i32** %6, i32*** %2
  %7 = alloca i32
  %8 = load i32**, i32*** %2
  %9 = getelementptr i32*, i32** %8, i32 0
  %10 = load i32*, i32** @global.a
  store i32* %10, i32** %9
  %11 = load i32**, i32*** %2
  %12 = getelementptr i32*, i32** %11, i32 1
  %13 = load i32*, i32** @global.a
  store i32* %13, i32** %12
  %14 = load i32**, i32*** %2
  %15 = getelementptr i32*, i32** %14, i32 2
  %16 = load i32*, i32** @global.a
  store i32* %16, i32** %15
  %17 = load i32**, i32*** %2
  %18 = getelementptr i32*, i32** %17, i32 3
  %19 = load i32*, i32** @global.a
  store i32* %19, i32** %18
  %20 = load i32**, i32*** %2
  %21 = bitcast i32** %20 to i32*
  %22 = getelementptr i32, i32* %21, i32 -1
  %23 = load i32, i32* %22
  %24 = call i8* @_toString(i32 %23)
  call void @_println(i8* %24)
  store i32 0, i32* %7
  br label %25

25:
  %26 = load i32, i32* %7
  %27 = load i32**, i32*** %2
  %28 = getelementptr i32*, i32** %27, i32 0
  %29 = load i32*, i32** %28
  %30 = bitcast i32* %29 to i32*
  %31 = getelementptr i32, i32* %30, i32 -1
  %32 = load i32, i32* %31
  %33 = icmp slt i32 %26, %32
  br i1 %33, label %34, label %44

34:
  %35 = load i32**, i32*** %2
  %36 = getelementptr i32*, i32** %35, i32 0
  %37 = load i32*, i32** %36
  %38 = load i32, i32* %7
  %39 = getelementptr i32, i32* %37, i32 %38
  %40 = call i32 @_getInt()
  store i32 %40, i32* %39
  br label %41

41:
  %42 = load i32, i32* %7
  %43 = add i32 %42, 1
  store i32 %43, i32* %7
  br label %25

44:
  store i32 0, i32* %7
  br label %45

45:
  %46 = load i32, i32* %7
  %47 = load i32**, i32*** %2
  %48 = getelementptr i32*, i32** %47, i32 1
  %49 = load i32*, i32** %48
  %50 = bitcast i32* %49 to i32*
  %51 = getelementptr i32, i32* %50, i32 -1
  %52 = load i32, i32* %51
  %53 = icmp slt i32 %46, %52
  br i1 %53, label %54, label %65

54:
  %55 = load i32**, i32*** %2
  %56 = getelementptr i32*, i32** %55, i32 1
  %57 = load i32*, i32** %56
  %58 = load i32, i32* %7
  %59 = getelementptr i32, i32* %57, i32 %58
  %60 = load i32, i32* %59
  %61 = call i8* @_toString(i32 %60)
  call void @_print(i8* %61)
  br label %62

62:
  %63 = load i32, i32* %7
  %64 = add i32 %63, 1
  store i32 %64, i32* %7
  br label %45

65:
  %66 = getelementptr [1 x i8], [1 x i8]* @.strconst.0, i32 0, i32 0
  call void @_println(i8* %66)
  store i32 0, i32* %7
  br label %67

67:
  %68 = load i32, i32* %7
  %69 = load i32**, i32*** %2
  %70 = getelementptr i32*, i32** %69, i32 2
  %71 = load i32*, i32** %70
  %72 = bitcast i32* %71 to i32*
  %73 = getelementptr i32, i32* %72, i32 -1
  %74 = load i32, i32* %73
  %75 = icmp slt i32 %68, %74
  br i1 %75, label %76, label %85

76:
  %77 = load i32**, i32*** %2
  %78 = getelementptr i32*, i32** %77, i32 2
  %79 = load i32*, i32** %78
  %80 = load i32, i32* %7
  %81 = getelementptr i32, i32* %79, i32 %80
  store i32 0, i32* %81
  br label %82

82:
  %83 = load i32, i32* %7
  %84 = add i32 %83, 1
  store i32 %84, i32* %7
  br label %67

85:
  store i32 0, i32* %7
  br label %86

86:
  %87 = load i32, i32* %7
  %88 = load i32**, i32*** %2
  %89 = getelementptr i32*, i32** %88, i32 3
  %90 = load i32*, i32** %89
  %91 = bitcast i32* %90 to i32*
  %92 = getelementptr i32, i32* %91, i32 -1
  %93 = load i32, i32* %92
  %94 = icmp slt i32 %87, %93
  br i1 %94, label %95, label %106

95:
  %96 = load i32**, i32*** %2
  %97 = getelementptr i32*, i32** %96, i32 3
  %98 = load i32*, i32** %97
  %99 = load i32, i32* %7
  %100 = getelementptr i32, i32* %98, i32 %99
  %101 = load i32, i32* %100
  %102 = call i8* @_toString(i32 %101)
  call void @_print(i8* %102)
  br label %103

103:
  %104 = load i32, i32* %7
  %105 = add i32 %104, 1
  store i32 %105, i32* %7
  br label %86

106:
  store i32 0, i32* %1
  br label %107

107:
  %108 = load i32, i32* %1
  ret i32 %108
}
















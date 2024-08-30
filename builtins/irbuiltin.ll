@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@.str.1 = private unnamed_addr constant [4 x i8] c"%s\0A\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
@.str.4 = private unnamed_addr constant [5 x i8] c"true\00", align 1
@.str.5 = private unnamed_addr constant [6 x i8] c"false\00", align 1

define dso_local void @_print(ptr noundef %str) #0 {
entry:
  %str.addr = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
  %0 = load ptr, ptr %str.addr, align 4
  %call = call i32 (ptr, ...) @printf(ptr noundef @.str, ptr noundef %0)
  ret void
}

declare dso_local i32 @printf(ptr noundef, ...) #1

define dso_local void @_println(ptr noundef %str) #0 {
entry:
  %str.addr = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
  %0 = load ptr, ptr %str.addr, align 4
  %call = call i32 (ptr, ...) @printf(ptr noundef @.str.1, ptr noundef %0)
  ret void
}

define dso_local void @_printInt(i32 noundef %n) #0 {
entry:
  %n.addr = alloca i32, align 4
  store i32 %n, ptr %n.addr, align 4
  %0 = load i32, ptr %n.addr, align 4
  %call = call i32 (ptr, ...) @printf(ptr noundef @.str.2, i32 noundef %0)
  ret void
}

define dso_local void @_printlnInt(i32 noundef %n) #0 {
entry:
  %n.addr = alloca i32, align 4
  store i32 %n, ptr %n.addr, align 4
  %0 = load i32, ptr %n.addr, align 4
  %call = call i32 (ptr, ...) @printf(ptr noundef @.str.3, i32 noundef %0)
  ret void
}

define dso_local ptr @_getString() #0 {
entry:
  %buffer = alloca ptr, align 4
  %length = alloca i32, align 4
  %call = call noalias ptr @malloc(i32 noundef 260) #5
  store ptr %call, ptr %buffer, align 4
  %0 = load ptr, ptr %buffer, align 4
  %arrayidx = getelementptr inbounds i8, ptr %0, i32 4
  %call1 = call i32 (ptr, ...) @scanf(ptr noundef @.str, ptr noundef %arrayidx)
  %1 = load ptr, ptr %buffer, align 4
  %arrayidx2 = getelementptr inbounds i8, ptr %1, i32 4
  %call3 = call i32 @strlen(ptr noundef %arrayidx2) #6
  store i32 %call3, ptr %length, align 4
  %2 = load i32, ptr %length, align 4
  %3 = load ptr, ptr %buffer, align 4
  store i32 %2, ptr %3, align 4
  %4 = load ptr, ptr %buffer, align 4
  %arrayidx4 = getelementptr inbounds i8, ptr %4, i32 4
  ret ptr %arrayidx4
}

declare dso_local noalias ptr @malloc(i32 noundef) #2

declare dso_local i32 @scanf(ptr noundef, ...) #1

declare dso_local i32 @strlen(ptr noundef) #3

define dso_local i32 @_getInt() #0 {
entry:
  %n = alloca i32, align 4
  %call = call i32 (ptr, ...) @scanf(ptr noundef @.str.2, ptr noundef %n)
  %0 = load i32, ptr %n, align 4
  ret i32 %0
}

define dso_local ptr @_toString(i32 noundef %i) #0 {
entry:
  %i.addr = alloca i32, align 4
  %buffer = alloca ptr, align 4
  %length = alloca i32, align 4
  store i32 %i, ptr %i.addr, align 4
  %call = call noalias ptr @malloc(i32 noundef 260) #5
  store ptr %call, ptr %buffer, align 4
  %0 = load ptr, ptr %buffer, align 4
  %arrayidx = getelementptr inbounds i8, ptr %0, i32 4
  %1 = load i32, ptr %i.addr, align 4
  %call1 = call i32 (ptr, ptr, ...) @sprintf(ptr noundef %arrayidx, ptr noundef @.str.2, i32 noundef %1) #6
  store i32 %call1, ptr %length, align 4
  %2 = load i32, ptr %length, align 4
  %3 = load ptr, ptr %buffer, align 4
  store i32 %2, ptr %3, align 4
  %4 = load ptr, ptr %buffer, align 4
  %arrayidx2 = getelementptr inbounds i8, ptr %4, i32 4
  ret ptr %arrayidx2
}

declare dso_local i32 @sprintf(ptr noundef, ptr noundef, ...) #3

define dso_local ptr @_string_substring(ptr noundef %str, i32 noundef %left, i32 noundef %right) #0 {
entry:
  %str.addr = alloca ptr, align 4
  %left.addr = alloca i32, align 4
  %right.addr = alloca i32, align 4
  %length = alloca i32, align 4
  %buffer = alloca ptr, align 4
  store ptr %str, ptr %str.addr, align 4
  store i32 %left, ptr %left.addr, align 4
  store i32 %right, ptr %right.addr, align 4
  %0 = load i32, ptr %right.addr, align 4
  %1 = load i32, ptr %left.addr, align 4
  %sub = sub nsw i32 %0, %1
  store i32 %sub, ptr %length, align 4
  %2 = load i32, ptr %length, align 4
  %add = add nsw i32 %2, 4
  %call = call noalias ptr @malloc(i32 noundef %add) #5
  store ptr %call, ptr %buffer, align 4
  %3 = load i32, ptr %length, align 4
  %4 = load ptr, ptr %buffer, align 4
  store i32 %3, ptr %4, align 4
  %5 = load ptr, ptr %buffer, align 4
  %arrayidx = getelementptr inbounds i8, ptr %5, i32 4
  %6 = load ptr, ptr %str.addr, align 4
  %7 = load i32, ptr %left.addr, align 4
  %arrayidx1 = getelementptr inbounds i8, ptr %6, i32 %7
  %8 = load i32, ptr %length, align 4
  call void @llvm.memcpy.p0.p0.i32(ptr align 1 %arrayidx, ptr align 1 %arrayidx1, i32 %8, i1 false)
  %9 = load ptr, ptr %buffer, align 4
  %10 = load i32, ptr %length, align 4
  %add2 = add nsw i32 %10, 4
  %arrayidx3 = getelementptr inbounds i8, ptr %9, i32 %add2
  store i8 0, ptr %arrayidx3, align 1
  %11 = load ptr, ptr %buffer, align 4
  %arrayidx4 = getelementptr inbounds i8, ptr %11, i32 4
  ret ptr %arrayidx4
}

declare void @llvm.memcpy.p0.p0.i32(ptr noalias nocapture writeonly, ptr noalias nocapture readonly, i32, i1 immarg) #4

define dso_local i32 @_string_parseInt(ptr noundef %str) #0 {
entry:
  %retval = alloca i32, align 4
  %str.addr = alloca ptr, align 4
  %curChar = alloca i8, align 1
  %negative = alloca i8, align 1
  %result = alloca i32, align 4
  %digit = alloca i32, align 4
  store ptr %str, ptr %str.addr, align 4
  %0 = load ptr, ptr %str.addr, align 4
  %1 = load i8, ptr %0, align 1
  store i8 %1, ptr %curChar, align 1
  %2 = load i8, ptr %curChar, align 1
  %conv = zext i8 %2 to i32
  %cmp = icmp eq i32 %conv, 0
  br i1 %cmp, label %if.then, label %if.end

if.then:
  store i32 0, ptr %retval, align 4
  br label %return

if.end:
  store i8 0, ptr %negative, align 1
  %3 = load i8, ptr %curChar, align 1
  %conv2 = zext i8 %3 to i32
  %cmp3 = icmp eq i32 %conv2, 45
  br i1 %cmp3, label %if.then5, label %if.end6

if.then5:
  store i8 1, ptr %negative, align 1
  %4 = load ptr, ptr %str.addr, align 4
  %incdec.ptr = getelementptr inbounds i8, ptr %4, i32 1
  store ptr %incdec.ptr, ptr %str.addr, align 4
  %5 = load ptr, ptr %str.addr, align 4
  %6 = load i8, ptr %5, align 1
  store i8 %6, ptr %curChar, align 1
  br label %if.end6

if.end6:
  store i32 0, ptr %result, align 4
  br label %while.cond

while.cond:
  %7 = load i8, ptr %curChar, align 1
  %conv7 = zext i8 %7 to i32
  %cmp8 = icmp sge i32 %conv7, 48
  br i1 %cmp8, label %land.rhs, label %land.end

land.rhs:
  %8 = load i8, ptr %curChar, align 1
  %conv10 = zext i8 %8 to i32
  %cmp11 = icmp sle i32 %conv10, 57
  br label %land.end

land.end:
  %9 = phi i1 [ false, %while.cond ], [ %cmp11, %land.rhs ]
  br i1 %9, label %while.body, label %while.end

while.body:
  %10 = load i8, ptr %curChar, align 1
  %conv13 = zext i8 %10 to i32
  %sub = sub nsw i32 %conv13, 48
  store i32 %sub, ptr %digit, align 4
  %11 = load i32, ptr %result, align 4
  %mul = mul nsw i32 %11, 10
  %12 = load i32, ptr %digit, align 4
  %add = add nsw i32 %mul, %12
  store i32 %add, ptr %result, align 4
  %13 = load ptr, ptr %str.addr, align 4
  %incdec.ptr14 = getelementptr inbounds i8, ptr %13, i32 1
  store ptr %incdec.ptr14, ptr %str.addr, align 4
  %14 = load ptr, ptr %str.addr, align 4
  %15 = load i8, ptr %14, align 1
  store i8 %15, ptr %curChar, align 1
  br label %while.cond

while.end:
  %16 = load i8, ptr %negative, align 1
  %loadedv = trunc i8 %16 to i1
  br i1 %loadedv, label %if.then15, label %if.end17

if.then15:
  %17 = load i32, ptr %result, align 4
  %sub16 = sub nsw i32 0, %17
  store i32 %sub16, ptr %result, align 4
  br label %if.end17

if.end17:
  %18 = load i32, ptr %result, align 4
  store i32 %18, ptr %retval, align 4
  br label %return

return:
  %19 = load i32, ptr %retval, align 4
  ret i32 %19
}

define dso_local i32 @_string_ord(ptr noundef %str, i32 noundef %pos) #0 {
entry:
  %str.addr = alloca ptr, align 4
  %pos.addr = alloca i32, align 4
  store ptr %str, ptr %str.addr, align 4
  store i32 %pos, ptr %pos.addr, align 4
  %0 = load ptr, ptr %str.addr, align 4
  %1 = load i32, ptr %pos.addr, align 4
  %arrayidx = getelementptr inbounds i8, ptr %0, i32 %1
  %2 = load i8, ptr %arrayidx, align 1
  %conv = zext i8 %2 to i32
  ret i32 %conv
}

define dso_local ptr @_string_add(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  %length = alloca i32, align 4
  %str = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %call = call i32 @strlen(ptr noundef %0) #6
  %1 = load ptr, ptr %rhs.addr, align 4
  %call1 = call i32 @strlen(ptr noundef %1) #6
  %add = add i32 %call, %call1
  store i32 %add, ptr %length, align 4
  %2 = load i32, ptr %length, align 4
  %add2 = add nsw i32 %2, 1
  %add3 = add i32 %add2, 4
  %call4 = call noalias ptr @malloc(i32 noundef %add3) #5
  store ptr %call4, ptr %str, align 4
  %3 = load i32, ptr %length, align 4
  %4 = load ptr, ptr %str, align 4
  store i32 %3, ptr %4, align 4
  %5 = load ptr, ptr %str, align 4
  %arrayidx = getelementptr inbounds i8, ptr %5, i32 4
  store ptr %arrayidx, ptr %str, align 4
  %6 = load ptr, ptr %str, align 4
  %7 = load ptr, ptr %lhs.addr, align 4
  %call5 = call ptr @strcpy(ptr noundef %6, ptr noundef %7) #6
  %8 = load ptr, ptr %str, align 4
  %9 = load ptr, ptr %rhs.addr, align 4
  %call6 = call ptr @strcat(ptr noundef %8, ptr noundef %9) #6
  %10 = load ptr, ptr %str, align 4
  ret ptr %10
}

declare dso_local ptr @strcpy(ptr noundef, ptr noundef) #3

declare dso_local ptr @strcat(ptr noundef, ptr noundef) #3

define dso_local zeroext i1 @_string_eq(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp eq i32 %call, 0
  ret i1 %cmp
}

declare dso_local i32 @strcmp(ptr noundef, ptr noundef) #3

define dso_local zeroext i1 @_string_ne(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp ne i32 %call, 0
  ret i1 %cmp
}

define dso_local zeroext i1 @_string_le(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp slt i32 %call, 0
  ret i1 %cmp
}

define dso_local zeroext i1 @_string_leq(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp sle i32 %call, 0
  ret i1 %cmp
}

define dso_local zeroext i1 @_string_ge(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp sgt i32 %call, 0
  ret i1 %cmp
}

define dso_local zeroext i1 @_string_geq(ptr noundef %lhs, ptr noundef %rhs) #0 {
entry:
  %lhs.addr = alloca ptr, align 4
  %rhs.addr = alloca ptr, align 4
  store ptr %lhs, ptr %lhs.addr, align 4
  store ptr %rhs, ptr %rhs.addr, align 4
  %0 = load ptr, ptr %lhs.addr, align 4
  %1 = load ptr, ptr %rhs.addr, align 4
  %call = call i32 @strcmp(ptr noundef %0, ptr noundef %1) #6
  %cmp = icmp sge i32 %call, 0
  ret i1 %cmp
}

define dso_local ptr @_boolToString(i1 noundef zeroext %i) #0 {
entry:
  %i.addr = alloca i8, align 1
  %true_str = alloca ptr, align 4
  %false_str = alloca ptr, align 4
  %str_to_copy = alloca ptr, align 4
  %len = alloca i32, align 4
  %buffer = alloca ptr, align 4
  %storedv = zext i1 %i to i8
  store i8 %storedv, ptr %i.addr, align 1
  store ptr @.str.4, ptr %true_str, align 4
  store ptr @.str.5, ptr %false_str, align 4
  %0 = load i8, ptr %i.addr, align 1
  %loadedv = trunc i8 %0 to i1
  br i1 %loadedv, label %if.then, label %if.else

if.then:
  %1 = load ptr, ptr %true_str, align 4
  store ptr %1, ptr %str_to_copy, align 4
  %2 = load ptr, ptr %true_str, align 4
  %call = call i32 @strlen(ptr noundef %2) #6
  store i32 %call, ptr %len, align 4
  br label %if.end

if.else:
  %3 = load ptr, ptr %false_str, align 4
  store ptr %3, ptr %str_to_copy, align 4
  %4 = load ptr, ptr %false_str, align 4
  %call1 = call i32 @strlen(ptr noundef %4) #6
  store i32 %call1, ptr %len, align 4
  br label %if.end

if.end:
  %5 = load i32, ptr %len, align 4
  %add = add i32 %5, 1
  %add2 = add i32 %add, 4
  %call3 = call noalias ptr @malloc(i32 noundef %add2) #5
  store ptr %call3, ptr %buffer, align 4
  %6 = load i32, ptr %len, align 4
  %7 = load ptr, ptr %buffer, align 4
  store i32 %6, ptr %7, align 4
  %8 = load ptr, ptr %buffer, align 4
  %add.ptr = getelementptr inbounds i8, ptr %8, i32 4
  %9 = load ptr, ptr %str_to_copy, align 4
  %10 = load i32, ptr %len, align 4
  call void @llvm.memcpy.p0.p0.i32(ptr align 1 %add.ptr, ptr align 1 %9, i32 %10, i1 false)
  %11 = load ptr, ptr %buffer, align 4
  %12 = load i32, ptr %len, align 4
  %add4 = add i32 4, %12
  %sub = sub i32 %add4, 1
  %arrayidx = getelementptr inbounds i8, ptr %11, i32 %sub
  store i8 0, ptr %arrayidx, align 1
  %13 = load ptr, ptr %buffer, align 4
  %add.ptr5 = getelementptr inbounds i8, ptr %13, i32 4
  ret ptr %add.ptr5
}

define dso_local ptr @_new_array(i32 noundef %size, i32 noundef %dimension, ptr noundef %arraySize) #0 {
entry:
  %retval = alloca ptr, align 4
  %size.addr = alloca i32, align 4
  %dimension.addr = alloca i32, align 4
  %arraySize.addr = alloca ptr, align 4
  %ptr = alloca ptr, align 4
  %i = alloca i32, align 4
  store i32 %size, ptr %size.addr, align 4
  store i32 %dimension, ptr %dimension.addr, align 4
  store ptr %arraySize, ptr %arraySize.addr, align 4
  %0 = load ptr, ptr %arraySize.addr, align 4
  %1 = load i32, ptr %0, align 4
  %cmp = icmp eq i32 %1, 0
  br i1 %cmp, label %if.then, label %if.end

if.then:
  store ptr null, ptr %retval, align 4
  br label %return

if.end:
  %2 = load i32, ptr %dimension.addr, align 4
  %cmp1 = icmp eq i32 %2, 1
  br i1 %cmp1, label %if.then2, label %if.end4

if.then2:
  %3 = load i32, ptr %size.addr, align 4
  %4 = load ptr, ptr %arraySize.addr, align 4
  %5 = load i32, ptr %4, align 4
  %mul = mul nsw i32 %3, %5
  %add = add i32 %mul, 4
  %call = call noalias ptr @malloc(i32 noundef %add) #5
  %add.ptr = getelementptr inbounds i8, ptr %call, i32 4
  store ptr %add.ptr, ptr %ptr, align 4
  %6 = load ptr, ptr %arraySize.addr, align 4
  %7 = load i32, ptr %6, align 4
  %8 = load ptr, ptr %ptr, align 4
  %add.ptr3 = getelementptr inbounds i8, ptr %8, i32 -4
  store i32 %7, ptr %add.ptr3, align 4
  br label %if.end4

if.end4:
  %9 = load i32, ptr %dimension.addr, align 4
  %cmp5 = icmp sgt i32 %9, 1
  br i1 %cmp5, label %if.then6, label %if.end15

if.then6:
  %10 = load ptr, ptr %arraySize.addr, align 4
  %11 = load i32, ptr %10, align 4
  %mul7 = mul i32 4, %11
  %add8 = add i32 %mul7, 4
  %call9 = call noalias ptr @malloc(i32 noundef %add8) #5
  %add.ptr10 = getelementptr inbounds i8, ptr %call9, i32 4
  store ptr %add.ptr10, ptr %ptr, align 4
  %12 = load ptr, ptr %arraySize.addr, align 4
  %13 = load i32, ptr %12, align 4
  %14 = load ptr, ptr %ptr, align 4
  %add.ptr11 = getelementptr inbounds i8, ptr %14, i32 -4
  store i32 %13, ptr %add.ptr11, align 4
  store i32 0, ptr %i, align 4
  br label %for.cond

for.cond:
  %15 = load i32, ptr %i, align 4
  %16 = load ptr, ptr %arraySize.addr, align 4
  %17 = load i32, ptr %16, align 4
  %cmp12 = icmp slt i32 %15, %17
  br i1 %cmp12, label %for.body, label %for.end

for.body:
  %18 = load i32, ptr %size.addr, align 4
  %19 = load i32, ptr %dimension.addr, align 4
  %sub = sub nsw i32 %19, 1
  %20 = load ptr, ptr %arraySize.addr, align 4
  %add.ptr13 = getelementptr inbounds i32, ptr %20, i32 1
  %call14 = call ptr @_new_array(i32 noundef %18, i32 noundef %sub, ptr noundef %add.ptr13)
  %21 = load ptr, ptr %ptr, align 4
  %22 = load i32, ptr %i, align 4
  %arrayidx = getelementptr inbounds ptr, ptr %21, i32 %22
  store ptr %call14, ptr %arrayidx, align 4
  br label %for.inc

for.inc:
  %23 = load i32, ptr %i, align 4
  %inc = add nsw i32 %23, 1
  store i32 %inc, ptr %i, align 4
  br label %for.cond

for.end:
  br label %if.end15

if.end15:
  %24 = load ptr, ptr %ptr, align 4
  store ptr %24, ptr %retval, align 4
  br label %return

return:
  %25 = load ptr, ptr %retval, align 4
  ret ptr %25
}

attributes #0 = { noinline nounwind optnone "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smctr,-experimental-smmpm,-experimental-smnpm,-experimental-ssctr,-experimental-ssnpm,-experimental-sspm,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-experimental-zvbc32e,-experimental-zvkgs,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssqosid,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smctr,-experimental-smmpm,-experimental-smnpm,-experimental-ssctr,-experimental-ssnpm,-experimental-sspm,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-experimental-zvbc32e,-experimental-zvkgs,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssqosid,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #2 = { allocsize(0) "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smctr,-experimental-smmpm,-experimental-smnpm,-experimental-ssctr,-experimental-ssnpm,-experimental-sspm,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-experimental-zvbc32e,-experimental-zvkgs,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssqosid,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #3 = { nounwind "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="generic-rv32" "target-features"="+32bit,+a,+c,+d,+f,+m,+relax,+zicsr,+zifencei,+zmmul,-b,-e,-experimental-smctr,-experimental-smmpm,-experimental-smnpm,-experimental-ssctr,-experimental-ssnpm,-experimental-sspm,-experimental-supm,-experimental-zacas,-experimental-zalasr,-experimental-zicfilp,-experimental-zicfiss,-experimental-zvbc32e,-experimental-zvkgs,-h,-shcounterenw,-shgatpa,-shtvala,-shvsatpa,-shvstvala,-shvstvecd,-smaia,-smcdeleg,-smcsrind,-smepmp,-smstateen,-ssaia,-ssccfg,-ssccptr,-sscofpmf,-sscounterenw,-sscsrind,-ssqosid,-ssstateen,-ssstrict,-sstc,-sstvala,-sstvecd,-ssu64xl,-svade,-svadu,-svbare,-svinval,-svnapot,-svpbmt,-v,-xcvalu,-xcvbi,-xcvbitmanip,-xcvelw,-xcvmac,-xcvmem,-xcvsimd,-xsfcease,-xsfvcp,-xsfvfnrclipxfqf,-xsfvfwmaccqqq,-xsfvqmaccdod,-xsfvqmaccqoq,-xsifivecdiscarddlone,-xsifivecflushdlone,-xtheadba,-xtheadbb,-xtheadbs,-xtheadcmo,-xtheadcondmov,-xtheadfmemidx,-xtheadmac,-xtheadmemidx,-xtheadmempair,-xtheadsync,-xtheadvdot,-xventanacondops,-xwchc,-za128rs,-za64rs,-zaamo,-zabha,-zalrsc,-zama16b,-zawrs,-zba,-zbb,-zbc,-zbkb,-zbkc,-zbkx,-zbs,-zca,-zcb,-zcd,-zce,-zcf,-zcmop,-zcmp,-zcmt,-zdinx,-zfa,-zfbfmin,-zfh,-zfhmin,-zfinx,-zhinx,-zhinxmin,-zic64b,-zicbom,-zicbop,-zicboz,-ziccamoa,-ziccif,-zicclsm,-ziccrse,-zicntr,-zicond,-zihintntl,-zihintpause,-zihpm,-zimop,-zk,-zkn,-zknd,-zkne,-zknh,-zkr,-zks,-zksed,-zksh,-zkt,-ztso,-zvbb,-zvbc,-zve32f,-zve32x,-zve64d,-zve64f,-zve64x,-zvfbfmin,-zvfbfwma,-zvfh,-zvfhmin,-zvkb,-zvkg,-zvkn,-zvknc,-zvkned,-zvkng,-zvknha,-zvknhb,-zvks,-zvksc,-zvksed,-zvksg,-zvksh,-zvkt,-zvl1024b,-zvl128b,-zvl16384b,-zvl2048b,-zvl256b,-zvl32768b,-zvl32b,-zvl4096b,-zvl512b,-zvl64b,-zvl65536b,-zvl8192b" }
attributes #4 = { nocallback nofree nounwind willreturn memory(argmem: readwrite) }
attributes #5 = { allocsize(0) }
attributes #6 = { nounwind }
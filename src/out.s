	.text

	.globl	class.A.A
	.type	class.A.A,@function
class.A.A:
	addi    sp, sp, -64
	sw      ra, 60(sp)
	lw      ra, 60(sp)
	addi    sp, sp, 64
	ret		# ret void
.lfunc_end0:
	.size	class.A.A, .lfunc_end0-class.A.A

	.globl	_init
	.type	_init,@function
_init:
	addi    sp, sp, -64
	sw      ra, 60(sp)
	li      t1, 114
	lui     t3, %hi(global.n)		# store reg t1 to global @global.n
	sw      t1, %lo(global.n)(t3)
	li      t1, 514
	lui     t3, %hi(global.m)		# store reg t1 to global @global.m
	sw      t1, %lo(global.m)(t3)
	lw      ra, 60(sp)
	addi    sp, sp, 64
	ret		# ret void
.lfunc_end1:
	.size	_init, .lfunc_end1-_init

	.globl	global.fun
	.type	global.fun,@function
global.fun:
	addi    sp, sp, -64
	sw      ra, 60(sp)
	sw      s1, 56(sp)
	li      t1, 10
	li      t2, 20
	sub     t0, t1, t2
	sgtz    a2, t0		# %7 = icmp sgt i32 10, 20
	bnez    a2, global.fun.9		# br i1 %7, label %9, label %10
	j       global.fun.10
global.fun.9:
	li      t0, 10
	mv      a3, t0
	j       global.fun.11		# br label %11
global.fun.10:
	li      t0, 20
	mv      a3, t0
	j       global.fun.11		# br label %11
global.fun.11:
	mv      a4, a3
	li      t1, 100
	li      t2, 200
	add     a5, t1, t2		# %14 = add i32 100, 200
	li      t2, 300
	add     a6, a5, t2		# %15 = add i32 %14, 300
	sub     t0, a0, a1
	sgtz    a7, t0		# %18 = icmp sgt i32 %0, %1
	bnez    a7, global.fun.20		# br i1 %18, label %20, label %24
	j       global.fun.24
global.fun.20:
	sub     t4, a6, a1		# %23 = sub i32 %15, %1
	mv      t5, t4
	j       global.fun.28		# br label %28
global.fun.24:
	add     t6, a6, a1		# %27 = add i32 %15, %1
	mv      t5, t6
	j       global.fun.28		# br label %28
global.fun.28:
	mv      s1, t5
	j       global.fun.30		# br label %30
global.fun.30:
	mv      a0, s1
	lw      s1, 56(sp)
	lw      ra, 60(sp)
	addi    sp, sp, 64
	ret		# ret i32 %19
.lfunc_end2:
	.size	global.fun, .lfunc_end2-global.fun

	.globl	main
	.type	main,@function
main:
	addi    sp, sp, -96
	sw      s5, 48(sp)
	sw      s10, 52(sp)
	sw      s3, 56(sp)
	sw      s8, 60(sp)
	sw      s4, 64(sp)
	sw      s11, 68(sp)
	sw      s9, 72(sp)
	sw      s2, 76(sp)
	sw      ra, 92(sp)
	sw      s7, 80(sp)
	sw      s6, 84(sp)
	sw      s1, 88(sp)
	call    _init		# call void @_init()
	li      a0, 8
	call    malloc		# %5 = call i8* @malloc(i32 8)
	mv      a1, a0
	li      t0, 0
	mv      a2, t0
	li      t0, 0
	mv      a3, t0
	li      t0, 0
	mv      a4, t0
	j       main.8		# br label %8
main.8:
	mv      a5, a4
	mv      a6, a3
	mv      a7, a2
	lui     t3, %hi(global.n)		# load global @global.n to reg t0
	lw      t0, %lo(global.n)(t3)
	lw      t4, 0(t0)		# %10 = load i32, i32* @global.n
	sub     t0, a5, t4
	sltz    t5, t0		# %11 = icmp slt i32* %7, %10
	bnez    t5, main.12		# br i1 %11, label %12, label %50
	j       main.50
main.12:
	lui     t3, %hi(global.n)		# load global @global.n to reg t0
	lw      t0, %lo(global.n)(t3)
	lw      t6, 0(t0)		# %13 = load i32, i32* @global.n
	li      t2, 1
	add     s1, t6, t2		# %14 = add i32 %13, 1
	lui     t3, %hi(global.n)		# store reg s1 to global @global.n
	sw      s1, %lo(global.n)(t3)
	lui     t3, %hi(global.m)		# load global @global.m to reg t0
	lw      t0, %lo(global.m)(t3)
	lw      s2, 0(t0)		# %15 = load i32, i32* @global.m
	li      t2, 1
	sub     s3, s2, t2		# %16 = sub i32 %15, 1
	lui     t3, %hi(global.m)		# store reg s3 to global @global.m
	sw      s3, %lo(global.m)(t3)
	lui     t3, %hi(.L.strconst.0)		# load global @.strconst.0 to reg t0
	addi    t0, t3, %lo(.L.strconst.0)
	mv      s4, t0
	sw      a0, 44(sp)		# 
	mv      a0, s4
	call    _println		# call void @_println(i8* %17)
	lw      a0, 44(sp)		# 
	sw      a0, 44(sp)		# 
	mv      a0, a5
	call    _printlnInt		# call void @_printlnInt(i32* %7)
	lw      a0, 44(sp)		# 
	add     s5, a6, a7		# %21 = add i32 %3, %2
	lui     t3, %hi(global.n)		# load global @global.n to reg t0
	lw      t0, %lo(global.n)(t3)
	lw      s6, 0(t0)		# %22 = load i32, i32* @global.n
	add     s7, s5, s6		# %23 = add i32 %21, %22
	add     s8, a7, a5		# %26 = add i32 %2, %7
	lui     t3, %hi(global.m)		# load global @global.m to reg t0
	lw      t0, %lo(global.m)(t3)
	lw      s9, 0(t0)		# %27 = load i32, i32* @global.m
	add     s10, s8, s9		# %28 = add i32 %26, %27
	addi    s11, a1, 0		# %30 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
	addi    a0, a1, 0		# %32 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
	lw      a1, 0(a0)		# %33 = load i32, i32* %32
	addi    t4, a1, 0		# %35 = getelementptr %class.A, %class.A* %6, i32 0, i32 0
	lw      t5, 0(t4)		# %36 = load i32, i32* %35
	sw      a3, 32(sp)		# 
	sw      a1, 40(sp)		# 
	sw      a2, 36(sp)		# 
	sw      a5, 24(sp)		# 
	sw      t4, 12(sp)		# 
	sw      a7, 16(sp)		# 
	sw      a4, 28(sp)		# 
	sw      a6, 20(sp)		# 
	sw      t5, 8(sp)		# 
	sw      a0, 44(sp)		# 
	mv      a0, t5
	mv      a1, a5
	call    global.fun		# %38 = call i32 @global.fun(i32 %36, i32* %7)
	mv      t6, a0
	lw      a3, 32(sp)		# 
	lw      a1, 40(sp)		# 
	lw      a2, 36(sp)		# 
	lw      a5, 24(sp)		# 
	lw      t4, 12(sp)		# 
	lw      a7, 16(sp)		# 
	lw      a4, 28(sp)		# 
	lw      a6, 20(sp)		# 
	lw      t5, 8(sp)		# 
	lw      a0, 44(sp)		# 
	add     s1, a1, t6		# %39 = add i32 %33, %38
	sw      s1, 0(s11)		# 
	addi    s2, a1, 4		# %41 = getelementptr %class.A, %class.A* %6, i32 0, i32 1
	addi    s3, a1, 4		# %43 = getelementptr %class.A, %class.A* %6, i32 0, i32 1
	lw      s4, 0(s3)		# %44 = load i1, i1* %43
	li      t2, 1
	xor     a6, s4, t2		# %45 = xor i1 %44, 1
	sw      a6, 0(s2)		# 
	j       main.46		# br label %46
main.46:
	li      t2, 2
	add     s5, a5, t2		# %48 = add i32 %7, 2
	mv      a2, s10
	mv      a3, s7
	mv      a4, s5
	j       main.8		# br label %8
main.50:
	li      a0, 0
	lw      s1, 88(sp)
	lw      s6, 84(sp)
	lw      s7, 80(sp)
	lw      ra, 92(sp)
	lw      s2, 76(sp)
	lw      s9, 72(sp)
	lw      s11, 68(sp)
	lw      s4, 64(sp)
	lw      s8, 60(sp)
	lw      s3, 56(sp)
	lw      s10, 52(sp)
	lw      s5, 48(sp)
	addi    sp, sp, 96
	ret		# ret i32 0
.lfunc_end3:
	.size	main, .lfunc_end3-main

	.data
	.type	global.n,@object
	.p2align	2
	.globl	global.n
global.n:
	.word	0
	.size	global.n, 4
		# @global.n = global i32 0
	.type	global.m,@object
	.p2align	2
	.globl	global.m
global.m:
	.word	0
	.size	global.m, 4
		# @global.m = global i32 0
	.rodata
	.type	.L.strconst.0,@object
.L.strconst.0:
	.asciz	"i="
	.size	.L.strconst.0, 3
		# @.strconst.0 = private unnamed_addr constant [3 x i8] c"i=\00"


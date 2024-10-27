	.text

	.globl	_init
	.type	_init,@function
_init:
	addi    sp, sp, -48
	sw      ra, 40(sp)
	lw      ra, 40(sp)
	addi    sp, sp, 48
	ret		# ret void
.lfunc_end0:
	.size	_init, .lfunc_end0-_init

	.globl	global.tak
	.type	global.tak,@function
global.tak:
	addi    sp, sp, -48
	sw      ra, 40(sp)
	sw      s3, 28(sp)
	sw      s1, 32(sp)
	sw      s2, 36(sp)
	sub     t0, a1, a0
	sltz    a3, t0		# %10 = icmp slt i32 %1, %0
	bnez    a3, global.tak.11		# br i1 %10, label %11, label %29
	j       global.tak.29		# br i1 %10, label %11, label %29
global.tak.11:
	li      t2, 1
	sub     a4, a0, t2		# %13 = sub i32 %0, 1
	sw      a2, 16(sp)
	sw      t4, -8(sp)
	sw      a4, 8(sp)
	sw      a6, 0(sp)
	sw      a3, 12(sp)
	sw      t6, -16(sp)
	sw      a1, 20(sp)
	sw      t5, -12(sp)
	sw      a0, 24(sp)
	sw      a5, 4(sp)
	sw      a7, -4(sp)
	mv      a0, a4
	mv      a2, a1
	mv      a4, a2
	call    global.tak		# %16 = call i32 @global.tak(i32 %13, i32 %1, i32 %2)
	mv      a5, a0
	lw      a2, 16(sp)
	lw      t4, -8(sp)
	lw      a4, 8(sp)
	lw      a6, 0(sp)
	lw      a3, 12(sp)
	lw      t6, -16(sp)
	lw      a1, 20(sp)
	lw      t5, -12(sp)
	lw      a0, 24(sp)
	lw      a5, 4(sp)
	lw      a7, -4(sp)
	li      t2, 1
	sub     a6, a1, t2		# %18 = sub i32 %1, 1
	sw      a2, 16(sp)
	sw      t4, -8(sp)
	sw      a4, 8(sp)
	sw      a6, 0(sp)
	sw      a3, 12(sp)
	sw      t6, -16(sp)
	sw      a1, 20(sp)
	sw      t5, -12(sp)
	sw      a0, 24(sp)
	sw      a5, 4(sp)
	sw      a7, -4(sp)
	mv      a0, a6
	mv      a4, a0
	call    global.tak		# %21 = call i32 @global.tak(i32 %18, i32 %2, i32 %0)
	mv      a7, a0
	lw      a2, 16(sp)
	lw      t4, -8(sp)
	lw      a4, 8(sp)
	lw      a6, 0(sp)
	lw      a3, 12(sp)
	lw      t6, -16(sp)
	lw      a1, 20(sp)
	lw      t5, -12(sp)
	lw      a0, 24(sp)
	lw      a5, 4(sp)
	lw      a7, -4(sp)
	li      t2, 1
	sub     t4, a2, t2		# %23 = sub i32 %2, 1
	sw      a2, 16(sp)
	sw      t4, -8(sp)
	sw      a4, 8(sp)
	sw      a6, 0(sp)
	sw      a3, 12(sp)
	sw      t6, -16(sp)
	sw      a1, 20(sp)
	sw      t5, -12(sp)
	sw      a0, 24(sp)
	sw      a5, 4(sp)
	sw      a7, -4(sp)
	mv      a0, t4
	mv      a2, a0
	mv      a4, a1
	call    global.tak		# %26 = call i32 @global.tak(i32 %23, i32 %0, i32 %1)
	mv      t5, a0
	lw      a2, 16(sp)
	lw      t4, -8(sp)
	lw      a4, 8(sp)
	lw      a6, 0(sp)
	lw      a3, 12(sp)
	lw      t6, -16(sp)
	lw      a1, 20(sp)
	lw      t5, -12(sp)
	lw      a0, 24(sp)
	lw      a5, 4(sp)
	lw      a7, -4(sp)
	sw      a2, 16(sp)
	sw      t4, -8(sp)
	sw      a4, 8(sp)
	sw      a6, 0(sp)
	sw      a3, 12(sp)
	sw      t6, -16(sp)
	sw      a1, 20(sp)
	sw      t5, -12(sp)
	sw      a0, 24(sp)
	sw      a5, 4(sp)
	sw      a7, -4(sp)
	mv      a0, a5
	mv      a2, a7
	mv      a4, t5
	call    global.tak		# %27 = call i32 @global.tak(i32 %16, i32 %21, i32 %26)
	mv      t6, a0
	lw      a2, 16(sp)
	lw      t4, -8(sp)
	lw      a4, 8(sp)
	lw      a6, 0(sp)
	lw      a3, 12(sp)
	lw      t6, -16(sp)
	lw      a1, 20(sp)
	lw      t5, -12(sp)
	lw      a0, 24(sp)
	lw      a5, 4(sp)
	lw      a7, -4(sp)
	li      t1, 1
	add     s1, t1, t6		# %28 = add i32 1, %27
	mv      s2, s1
	j       global.tak.32		# br label %32
global.tak.29:
	mv      s2, a2
	j       global.tak.32		# br label %32
global.tak.32:
	mv      s3, s2
	mv      a0, s3
	lw      s2, 36(sp)
	lw      s1, 32(sp)
	lw      s3, 28(sp)
	lw      ra, 40(sp)
	addi    sp, sp, 48
	ret		# ret i32 %7
.lfunc_end1:
	.size	global.tak, .lfunc_end1-global.tak

	.globl	main
	.type	main,@function
main:
	addi    sp, sp, -48
	sw      ra, 40(sp)
	call    _init		# call void @_init()
	sw      a2, 28(sp)
	sw      a1, 32(sp)
	sw      a0, 36(sp)
	call    _getInt		# %5 = call i32 @_getInt()
	lw      a2, 28(sp)
	lw      a1, 32(sp)
	lw      a0, 36(sp)
	sw      a2, 28(sp)
	sw      a1, 32(sp)
	sw      a0, 36(sp)
	call    _getInt		# %6 = call i32 @_getInt()
	mv      a1, a0
	lw      a2, 28(sp)
	lw      a1, 32(sp)
	lw      a0, 36(sp)
	sw      a2, 28(sp)
	sw      a1, 32(sp)
	sw      a0, 36(sp)
	call    _getInt		# %7 = call i32 @_getInt()
	mv      a2, a0
	lw      a2, 28(sp)
	lw      a1, 32(sp)
	lw      a0, 36(sp)
	sw      a2, 28(sp)
	sw      t4, 4(sp)
	sw      a4, 20(sp)
	sw      a6, 12(sp)
	sw      a3, 24(sp)
	sw      t6, -4(sp)
	sw      a1, 32(sp)
	sw      t5, 0(sp)
	sw      a0, 36(sp)
	sw      a5, 16(sp)
	sw      a7, 8(sp)
	mv      a2, a1
	mv      a4, a2
	call    global.tak		# %11 = call i32 @global.tak(i32 %5, i32 %6, i32 %7)
	mv      a3, a0
	lw      a2, 28(sp)
	lw      t4, 4(sp)
	lw      a4, 20(sp)
	lw      a6, 12(sp)
	lw      a3, 24(sp)
	lw      t6, -4(sp)
	lw      a1, 32(sp)
	lw      t5, 0(sp)
	lw      a0, 36(sp)
	lw      a5, 16(sp)
	lw      a7, 8(sp)
	sw      a2, 28(sp)
	sw      a1, 32(sp)
	sw      a0, 36(sp)
	mv      a0, a3
	call    _toString		# %12 = call i8* @_toString(i32 %11)
	mv      a4, a0
	lw      a2, 28(sp)
	lw      a1, 32(sp)
	lw      a0, 36(sp)
	sw      a2, 28(sp)
	sw      a1, 32(sp)
	sw      a0, 36(sp)
	mv      a0, a4
	call    _println		# call void @_println(i8* %12)
	lw      a2, 28(sp)
	lw      a1, 32(sp)
	lw      a0, 36(sp)
	j       main.13		# br label %13
main.13:
	li      a0, 0
	lw      ra, 40(sp)
	addi    sp, sp, 48
	ret		# ret i32 0
.lfunc_end2:
	.size	main, .lfunc_end2-main

	.data
	.rodata


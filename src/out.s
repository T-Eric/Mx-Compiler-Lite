	.section text
	.globl	_init
	.type	_init,@function
_init:
	addi    sp, sp, 0
	addi    sp, sp, 0
	ret
.Lfunc_end0:
	.size	_init, .Lfunc_end0-_init

	.globl	global.test
	.type	global.test,@function
global.test:
	addi    sp, sp, 32
	lw      a0, 4(sp)
	sw      a0, 0(sp)
	call    _getInt
	sw      a0, 8(sp)
	lw      a0, 8(sp)
	sw      a0, 0(sp)
	lw      a0, 0(sp)
	sw      a0, 12(sp)
	lw      a1, 12(sp)
	li      2, a2
	div     a0, a1, a2
	lw      a0, 16(sp)
	lw      a0, 16(sp)
	call    _printlnInt
	j       global.test.5
global.test.5:
	addi    sp, sp, -32
	ret
.Lfunc_end1:
	.size	global.test, .Lfunc_end1-global.test

	.globl	main
	.type	main,@function
main:
	addi    sp, sp, 16
	call    _init
	lw      a0, 4(sp)
	sw      a0, 0(sp)
	call    global.test
	call    global.test
	j       main.2
main.2:
	lw      a0, 0(sp)
	sw      a0, 8(sp)
	lw      a0, 8(sp)
	addi    sp, sp, -16
	ret
.Lfunc_end2:
	.size	main, .Lfunc_end2-main

	.section data
	.section rodata


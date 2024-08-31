        .text
        .attribute      4, 16
        .attribute      5, "rv32i2p1"
        .file   "example.ll"
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _print                          # -- Begin function _print
        .p2align        1
        .type   _print,@function
_print:                                 # @_print
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        lw      a1, -12(s0)
        lui     a0, %hi(.L.str)
        addi    a0, a0, %lo(.L.str)
        call    printf
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end0:
        .size   _print, .Lfunc_end0-_print
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _println                        # -- Begin function _println
        .p2align        1
        .type   _println,@function
_println:                               # @_println
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        lw      a1, -12(s0)
        lui     a0, %hi(.L.str.1)
        addi    a0, a0, %lo(.L.str.1)
        call    printf
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end1:
        .size   _println, .Lfunc_end1-_println
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _printInt                       # -- Begin function _printInt
        .p2align        1
        .type   _printInt,@function
_printInt:                              # @_printInt
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        lw      a1, -12(s0)
        lui     a0, %hi(.L.str.2)
        addi    a0, a0, %lo(.L.str.2)
        call    printf
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end2:
        .size   _printInt, .Lfunc_end2-_printInt
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _printlnInt                     # -- Begin function _printlnInt
        .p2align        1
        .type   _printlnInt,@function
_printlnInt:                            # @_printlnInt
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        lw      a1, -12(s0)
        lui     a0, %hi(.L.str.3)
        addi    a0, a0, %lo(.L.str.3)
        call    printf
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end3:
        .size   _printlnInt, .Lfunc_end3-_printlnInt
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _getString                      # -- Begin function _getString
        .p2align        1
        .type   _getString,@function
_getString:                             # @_getString
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        li      a0, 260
        call    malloc
        sw      a0, -12(s0)
        lw      a0, -12(s0)
        addi    a1, a0, 4
        lui     a0, %hi(.L.str)
        addi    a0, a0, %lo(.L.str)
        call    scanf
        lw      a0, -12(s0)
        addi    a0, a0, 4
        call    strlen
        sw      a0, -16(s0)
        lw      a0, -16(s0)
        lw      a1, -12(s0)
        sw      a0, 0(a1)
        lw      a0, -12(s0)
        addi    a0, a0, 4
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end4:
        .size   _getString, .Lfunc_end4-_getString
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _getInt                         # -- Begin function _getInt
        .p2align        1
        .type   _getInt,@function
_getInt:                                # @_getInt
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        lui     a0, %hi(.L.str.2)
        addi    a0, a0, %lo(.L.str.2)
        addi    a1, s0, -12
        call    scanf
        lw      a0, -12(s0)
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end5:
        .size   _getInt, .Lfunc_end5-_getInt
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _toString                       # -- Begin function _toString
        .p2align        1
        .type   _toString,@function
_toString:                              # @_toString
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sw      a0, -12(s0)
        li      a0, 260
        call    malloc
        sw      a0, -16(s0)
        lw      a0, -16(s0)
        addi    a0, a0, 4
        lw      a2, -12(s0)
        lui     a1, %hi(.L.str.2)
        addi    a1, a1, %lo(.L.str.2)
        call    sprintf
        sw      a0, -20(s0)
        lw      a0, -20(s0)
        lw      a1, -16(s0)
        sw      a0, 0(a1)
        lw      a0, -16(s0)
        addi    a0, a0, 4
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end6:
        .size   _toString, .Lfunc_end6-_toString
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_substring               # -- Begin function _string_substring
        .p2align        1
        .type   _string_substring,@function
_string_substring:                      # @_string_substring
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        sw      a2, -20(s0)
        lw      a0, -20(s0)
        lw      a1, -16(s0)
        sub     a0, a0, a1
        sw      a0, -24(s0)
        lw      a0, -24(s0)
        addi    a0, a0, 4
        call    malloc
        sw      a0, -28(s0)
        lw      a0, -24(s0)
        lw      a1, -28(s0)
        sw      a0, 0(a1)
        lw      a0, -28(s0)
        addi    a0, a0, 4
        lw      a1, -12(s0)
        lw      a2, -16(s0)
        add     a1, a1, a2
        lw      a2, -24(s0)
        call    memcpy
        lw      a1, -28(s0)
        lw      a0, -24(s0)
        add     a1, a1, a0
        li      a0, 0
        sb      a0, 4(a1)
        lw      a0, -28(s0)
        addi    a0, a0, 4
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end7:
        .size   _string_substring, .Lfunc_end7-_string_substring
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_parseInt                # -- Begin function _string_parseInt
        .p2align        1
        .type   _string_parseInt,@function
_string_parseInt:                       # @_string_parseInt
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sw      a0, -16(s0)
        lw      a0, -16(s0)
        lbu     a0, 0(a0)
        sb      a0, -17(s0)
        lbu     a0, -17(s0)
        bnez    a0, .LBB8_2
        j       .LBB8_1
.LBB8_1:                                # %if.then
        li      a0, 0
        sw      a0, -12(s0)
        j       .LBB8_12
.LBB8_2:                                # %if.end
        li      a0, 0
        sb      a0, -18(s0)
        lbu     a0, -17(s0)
        li      a1, 45
        bne     a0, a1, .LBB8_4
        j       .LBB8_3
.LBB8_3:                                # %if.then5
        li      a0, 1
        sb      a0, -18(s0)
        lw      a0, -16(s0)
        addi    a0, a0, 1
        sw      a0, -16(s0)
        lw      a0, -16(s0)
        lbu     a0, 0(a0)
        sb      a0, -17(s0)
        j       .LBB8_4
.LBB8_4:                                # %if.end6
        li      a0, 0
        sw      a0, -24(s0)
        j       .LBB8_5
.LBB8_5:                                # %while.cond
        lbu     a0, -17(s0)
        li      a2, 0
        li      a1, 48
        sw      a2, -32(s0)                     # 4-byte Folded Spill
        blt     a0, a1, .LBB8_7
        j       .LBB8_6
.LBB8_6:                                # %land.rhs
        lbu     a0, -17(s0)
        slti    a0, a0, 58
        sw      a0, -32(s0)                     # 4-byte Folded Spill
        j       .LBB8_7
.LBB8_7:                                # %land.end
        lw      a0, -32(s0)                     # 4-byte Folded Reload
        andi    a0, a0, 1
        beqz    a0, .LBB8_9
        j       .LBB8_8
.LBB8_8:                                # %while.body
        lbu     a0, -17(s0)
        addi    a0, a0, -48
        sw      a0, -28(s0)
        lw      a0, -24(s0)
        li      a1, 10
        mul     a0, a0, a1
        lw      a1, -28(s0)
        add     a0, a0, a1
        sw      a0, -24(s0)
        lw      a0, -16(s0)
        addi    a0, a0, 1
        sw      a0, -16(s0)
        lw      a0, -16(s0)
        lbu     a0, 0(a0)
        sb      a0, -17(s0)
        j       .LBB8_5
.LBB8_9:                                # %while.end
        lbu     a0, -18(s0)
        andi    a0, a0, 1
        beqz    a0, .LBB8_11
        j       .LBB8_10
.LBB8_10:                               # %if.then15
        lw      a1, -24(s0)
        li      a0, 0
        sub     a0, a0, a1
        sw      a0, -24(s0)
        j       .LBB8_11
.LBB8_11:                               # %if.end17
        lw      a0, -24(s0)
        sw      a0, -12(s0)
        j       .LBB8_12
.LBB8_12:                               # %return
        lw      a0, -12(s0)
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end8:
        .size   _string_parseInt, .Lfunc_end8-_string_parseInt
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_ord                     # -- Begin function _string_ord
        .p2align        1
        .type   _string_ord,@function
_string_ord:                            # @_string_ord
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        add     a0, a0, a1
        lbu     a0, 0(a0)
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end9:
        .size   _string_ord, .Lfunc_end9-_string_ord
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_add                     # -- Begin function _string_add
        .p2align        1
        .type   _string_add,@function
_string_add:                            # @_string_add
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        call    strlen
        sw      a0, -28(s0)                     # 4-byte Folded Spill
        lw      a0, -16(s0)
        call    strlen
        mv      a1, a0
        lw      a0, -28(s0)                     # 4-byte Folded Reload
        add     a0, a0, a1
        sw      a0, -20(s0)
        lw      a0, -20(s0)
        addi    a0, a0, 5
        call    malloc
        sw      a0, -24(s0)
        lw      a0, -20(s0)
        lw      a1, -24(s0)
        sw      a0, 0(a1)
        lw      a0, -24(s0)
        addi    a0, a0, 4
        sw      a0, -24(s0)
        lw      a0, -24(s0)
        lw      a1, -12(s0)
        call    strcpy
        lw      a0, -24(s0)
        lw      a1, -16(s0)
        call    strcat
        lw      a0, -24(s0)
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end10:
        .size   _string_add, .Lfunc_end10-_string_add
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_eq                      # -- Begin function _string_eq
        .p2align        1
        .type   _string_eq,@function
_string_eq:                             # @_string_eq
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        seqz    a0, a0
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end11:
        .size   _string_eq, .Lfunc_end11-_string_eq
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_ne                      # -- Begin function _string_ne
        .p2align        1
        .type   _string_ne,@function
_string_ne:                             # @_string_ne
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        snez    a0, a0
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end12:
        .size   _string_ne, .Lfunc_end12-_string_ne
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_le                      # -- Begin function _string_le
        .p2align        1
        .type   _string_le,@function
_string_le:                             # @_string_le
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        srli    a0, a0, 31
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end13:
        .size   _string_le, .Lfunc_end13-_string_le
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_leq                     # -- Begin function _string_leq
        .p2align        1
        .type   _string_leq,@function
_string_leq:                            # @_string_leq
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        slti    a0, a0, 1
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end14:
        .size   _string_leq, .Lfunc_end14-_string_leq
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_ge                      # -- Begin function _string_ge
        .p2align        1
        .type   _string_ge,@function
_string_ge:                             # @_string_ge
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        mv      a1, a0
        li      a0, 0
        slt     a0, a0, a1
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end15:
        .size   _string_ge, .Lfunc_end15-_string_ge
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _string_geq                     # -- Begin function _string_geq
        .p2align        1
        .type   _string_geq,@function
_string_geq:                            # @_string_geq
        addi    sp, sp, -16
        sw      ra, 12(sp)                      # 4-byte Folded Spill
        sw      s0, 8(sp)                       # 4-byte Folded Spill
        addi    s0, sp, 16
        sw      a0, -12(s0)
        sw      a1, -16(s0)
        lw      a0, -12(s0)
        lw      a1, -16(s0)
        call    strcmp
        not     a0, a0
        srli    a0, a0, 31
        lw      ra, 12(sp)                      # 4-byte Folded Reload
        lw      s0, 8(sp)                       # 4-byte Folded Reload
        addi    sp, sp, 16
        ret
.Lfunc_end16:
        .size   _string_geq, .Lfunc_end16-_string_geq
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _boolToString                   # -- Begin function _boolToString
        .p2align        1
        .type   _boolToString,@function
_boolToString:                          # @_boolToString
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sb      a0, -9(s0)
        lui     a0, %hi(.L.str.4)
        addi    a0, a0, %lo(.L.str.4)
        sw      a0, -16(s0)
        lui     a0, %hi(.L.str.5)
        addi    a0, a0, %lo(.L.str.5)
        sw      a0, -20(s0)
        lbu     a0, -9(s0)
        andi    a0, a0, 1
        beqz    a0, .LBB17_2
        j       .LBB17_1
.LBB17_1:                               # %if.then
        lw      a0, -16(s0)
        sw      a0, -24(s0)
        lw      a0, -16(s0)
        call    strlen
        sw      a0, -28(s0)
        j       .LBB17_3
.LBB17_2:                               # %if.else
        lw      a0, -20(s0)
        sw      a0, -24(s0)
        lw      a0, -20(s0)
        call    strlen
        sw      a0, -28(s0)
        j       .LBB17_3
.LBB17_3:                               # %if.end
        lw      a0, -28(s0)
        addi    a0, a0, 5
        call    malloc
        sw      a0, -32(s0)
        lw      a0, -28(s0)
        lw      a1, -32(s0)
        sw      a0, 0(a1)
        lw      a0, -32(s0)
        addi    a0, a0, 4
        lw      a1, -24(s0)
        lw      a2, -28(s0)
        call    memcpy
        lw      a1, -32(s0)
        lw      a0, -28(s0)
        add     a1, a1, a0
        li      a0, 0
        sb      a0, 3(a1)
        lw      a0, -32(s0)
        addi    a0, a0, 4
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end17:
        .size   _boolToString, .Lfunc_end17-_boolToString
        .option pop
        .option push
        .option arch, +a, +c, +d, +f, +m, +zicsr, +zifencei, +zmmul
        .globl  _new_array                      # -- Begin function _new_array
        .p2align        1
        .type   _new_array,@function
_new_array:                             # @_new_array
        addi    sp, sp, -32
        sw      ra, 28(sp)                      # 4-byte Folded Spill
        sw      s0, 24(sp)                      # 4-byte Folded Spill
        addi    s0, sp, 32
        sw      a0, -16(s0)
        sw      a1, -20(s0)
        sw      a2, -24(s0)
        lw      a0, -24(s0)
        lw      a0, 0(a0)
        bnez    a0, .LBB18_2
        j       .LBB18_1
.LBB18_1:                               # %if.then
        li      a0, 0
        sw      a0, -12(s0)
        j       .LBB18_11
.LBB18_2:                               # %if.end
        lw      a0, -20(s0)
        li      a1, 1
        bne     a0, a1, .LBB18_4
        j       .LBB18_3
.LBB18_3:                               # %if.then2
        lw      a0, -16(s0)
        lw      a1, -24(s0)
        lw      a1, 0(a1)
        mul     a0, a0, a1
        addi    a0, a0, 4
        call    malloc
        addi    a0, a0, 4
        sw      a0, -28(s0)
        lw      a0, -24(s0)
        lw      a0, 0(a0)
        lw      a1, -28(s0)
        sw      a0, -4(a1)
        j       .LBB18_4
.LBB18_4:                               # %if.end4
        lw      a0, -20(s0)
        li      a1, 2
        blt     a0, a1, .LBB18_10
        j       .LBB18_5
.LBB18_5:                               # %if.then6
        lw      a0, -24(s0)
        lw      a0, 0(a0)
        slli    a0, a0, 2
        addi    a0, a0, 4
        call    malloc
        addi    a0, a0, 4
        sw      a0, -28(s0)
        lw      a0, -24(s0)
        lw      a0, 0(a0)
        lw      a1, -28(s0)
        sw      a0, -4(a1)
        li      a0, 0
        sw      a0, -32(s0)
        j       .LBB18_6
.LBB18_6:                               # %for.cond
        lw      a0, -32(s0)
        lw      a1, -24(s0)
        lw      a1, 0(a1)
        bge     a0, a1, .LBB18_9
        j       .LBB18_7
.LBB18_7:                               # %for.body
        lw      a0, -16(s0)
        lw      a1, -20(s0)
        addi    a1, a1, -1
        lw      a2, -24(s0)
        addi    a2, a2, 4
        call    _new_array
        lw      a1, -28(s0)
        lw      a2, -32(s0)
        slli    a2, a2, 2
        add     a1, a1, a2
        sw      a0, 0(a1)
        j       .LBB18_8
.LBB18_8:                               # %for.inc
        lw      a0, -32(s0)
        addi    a0, a0, 1
        sw      a0, -32(s0)
        j       .LBB18_6
.LBB18_9:                               # %for.end
        j       .LBB18_10
.LBB18_10:                              # %if.end15
        lw      a0, -28(s0)
        sw      a0, -12(s0)
        j       .LBB18_11
.LBB18_11:                              # %return
        lw      a0, -12(s0)
        lw      ra, 28(sp)                      # 4-byte Folded Reload
        lw      s0, 24(sp)                      # 4-byte Folded Reload
        addi    sp, sp, 32
        ret
.Lfunc_end18:
        .size   _new_array, .Lfunc_end18-_new_array
        .option pop
        .type   .L.str,@object                  # @.str
        .section        .rodata.str1.1,"aMS",@progbits,1
.L.str:
        .asciz  "%s"
        .size   .L.str, 3

        .type   .L.str.1,@object                # @.str.1
.L.str.1:
        .asciz  "%s\n"
        .size   .L.str.1, 4

        .type   .L.str.2,@object                # @.str.2
.L.str.2:
        .asciz  "%d"
        .size   .L.str.2, 3

        .type   .L.str.3,@object                # @.str.3
.L.str.3:
        .asciz  "%d\n"
        .size   .L.str.3, 4

        .type   .L.str.4,@object                # @.str.4
.L.str.4:
        .asciz  "true"
        .size   .L.str.4, 5

        .type   .L.str.5,@object                # @.str.5
.L.str.5:
        .asciz  "false"
        .size   .L.str.5, 6

        .section        ".note.GNU-stack","",@progbits


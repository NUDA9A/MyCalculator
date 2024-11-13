package com.example.mycalculator.parser

import com.example.mycalculator.parser.exceptions.OverflowException

class CheckedNegate(a: AbstractExpression) : AbstractExpression(a) {
    override fun evaluate(): Double {
        val a1 = a.evaluate()
        if (a1.toInt() == Int.MIN_VALUE) {
            throw OverflowException()
        }
        return -a1
    }
}
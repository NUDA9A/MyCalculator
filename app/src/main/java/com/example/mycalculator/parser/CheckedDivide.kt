package com.example.mycalculator.parser

import com.example.mycalculator.parser.exceptions.DivisionByZeroException
import com.example.mycalculator.parser.exceptions.OverflowException

class CheckedDivide(a: AbstractExpression, b: AbstractExpression) : AbstractExpression(a, b) {
    override fun evaluate(): Double {
        val a1 = a.evaluate()
        val b1 = b.evaluate()
        if (b1 == zero) {
            throw DivisionByZeroException()
        } else if (a1.toInt() == Int.MIN_VALUE && b1.toInt() == -1) {
            throw OverflowException()
        }
        return a1 / b1
    }
}
package com.example.mycalculator.parser

import com.example.mycalculator.parser.exceptions.OverflowException

class CheckedAdd(a: AbstractExpression, b: AbstractExpression) : AbstractExpression(a, b) {
    override fun evaluate(): Double {
        val a1 = a.evaluate()
        val b1 = b.evaluate()
        if (a1.toInt() > 0 && b1.toInt() > 0) {
            if (Int.MAX_VALUE - b1.toInt() < a1.toInt()) {
                throw OverflowException()
            }
        } else if (a1.toInt() < 0 && b1.toInt() < 0) {
            if (Int.MIN_VALUE - b1.toInt() > a1.toInt()) {
                throw OverflowException()
            }
        }
        return a1 + b1
    }
}
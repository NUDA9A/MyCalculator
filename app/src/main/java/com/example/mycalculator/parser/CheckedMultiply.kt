package com.example.mycalculator.parser

import com.example.mycalculator.parser.exceptions.OverflowException

class CheckedMultiply(a: AbstractExpression, b: AbstractExpression) : AbstractExpression(a, b) {
    override fun evaluate(): Double {
        val a1 = a.evaluate()
        val b1 = b.evaluate()
        if (a1 == zero || b1 == zero) {
            return zero
        } else if (a1 > 0 && b1 > 0) {
            if (Int.MAX_VALUE / b1.toInt() < a1.toInt()) {
                throw OverflowException()
            }
        } else if (a1 < 0 && b1 < 0) {
            if (Int.MAX_VALUE / b1.toInt() > a1.toInt()) {
                throw OverflowException()
            }
        } else if (a1 > 0) {
            if (Int.MIN_VALUE / a1.toInt() > b1.toInt()) {
                throw OverflowException()
            }
        } else {
            if (Int.MIN_VALUE / b1.toInt() > a1.toInt()) {
                throw OverflowException()
            }
        }
        return a1 * b1;
    }
}
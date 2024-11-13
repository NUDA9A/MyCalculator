package com.example.mycalculator.parser

class Const(constant: Double) : AbstractExpression(constant) {
    override fun evaluate(): Double {
        return this.constant.toDouble()
    }
}
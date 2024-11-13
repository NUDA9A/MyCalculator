package com.example.mycalculator.parser

import kotlin.properties.Delegates

abstract class AbstractExpression : Expression {
    protected var constant by Delegates.notNull<Double>()
    protected lateinit var a: AbstractExpression
    protected lateinit var b: AbstractExpression
    val zero: Double = 0.0

    constructor(constant: Double) {
        this.constant = constant
    }

    constructor(a: AbstractExpression, b: AbstractExpression) {
        this.a = a
        this.b = b
    }

    constructor(a: AbstractExpression) {
        this.a = a
    }

    abstract override fun evaluate(): Double
}
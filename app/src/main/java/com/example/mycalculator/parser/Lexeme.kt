package com.example.mycalculator.parser

class Lexeme {
    var type: LexType
    var value: String

    constructor(type: LexType, value: String) {
        this.type = type
        this.value = value
    }

    constructor(type: LexType, value: Char) {
        this.type = type
        this.value = value.toString()
    }
}
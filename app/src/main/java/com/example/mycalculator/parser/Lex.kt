package com.example.mycalculator.parser

class Lex(private val lexemes: List<Lexeme>) {
    private var pos = 0

    val lex: Lexeme
        get() = lexemes[pos++]

    fun back() {
        pos--
    }
}
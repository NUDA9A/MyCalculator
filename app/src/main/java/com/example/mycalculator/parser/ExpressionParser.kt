package com.example.mycalculator.parser

import com.example.mycalculator.parser.exceptions.CorrectBracketSequencesException
import com.example.mycalculator.parser.exceptions.MissingExpressionException
import com.example.mycalculator.parser.exceptions.MissingOperandException
import com.example.mycalculator.parser.exceptions.OverflowException


class ExpressionParser {
    private val l: Map<String, LexType> = java.util.Map.of(
        "(", LexType.LB,
        ")", LexType.RB,
        "*", LexType.MUL,
        "/", LexType.DIV,
        "+", LexType.PLUS,
        "-", LexType.MINUS
    )

    @Throws(Exception::class)
    fun parse(expression: String): Expression {
        val lexemes = lexAnalyze(expression)
        val lex = Lex(lexemes)
        return expr(lex)
    }

    private fun parseNum(sb: StringBuilder, i: Int, s: String): Int {
        var j = i
        while (j < s.length && (Character.isDigit(s[j]) || s[j] == '.')) {
            sb.append(s[j++])
        }
        return j - 1
    }

    @Throws(Exception::class)
    private fun lexAnalyze(expression: String): List<Lexeme> {
        var op = 0
        var bracket = 0
        var isFirst = false
        var lexType: LexType? = null
        val lexemes: MutableList<Lexeme> = ArrayList()
        var i = 0
        while (i < expression.length) {
            val c = expression[i]
            if (Character.isDigit(c)) {
                val num = StringBuilder()
                if (lexType == LexType.NUMBER) {
                    throw MissingOperandException()
                }
                i = parseNum(num, i, expression)
                lexemes.add(Lexeme(LexType.NUMBER, num.toString()))
                lexType = LexType.NUMBER
                op = 0
                if (!isFirst) {
                    isFirst = true
                }
                i++
                continue
            }

            for (key in l.keys) {
                if (c.toString() == key) {
                    if (key == "-") {
                        if (i == expression.length - 1) {
                            throw MissingExpressionException()
                        } else if (Character.isDigit(expression[i + 1]) && (op > 0 || !isFirst)) {
                            val num = StringBuilder()
                            num.append(key)
                            if (lexType == LexType.NUMBER) {
                                throw MissingOperandException()
                            }
                            i = parseNum(num, i + 1, expression)
                            lexemes.add(Lexeme(LexType.NUMBER, num.toString()))
                            lexType = LexType.NUMBER
                            op = 0
                            if (!isFirst) {
                                isFirst = true
                            }
                            break
                        }
                    }
                    lexemes.add(Lexeme(l[key]!!, key))
                    if (key != "(" && key != ")") {
                        lexType = l[key]
                    }
                    op++
                    if (key == "(") {
                        bracket++
                        op--
                    }
                    if (key == ")") {
                        bracket--
                        op--
                    }
                    if (bracket < 0) {
                        throw CorrectBracketSequencesException()
                    }
                    break
                }
            }
            i++
        }
        if (bracket != 0) {
            throw CorrectBracketSequencesException()
        }
        lexemes.add(Lexeme(LexType.EOF, ""))
        return lexemes
    }

    @Throws(Exception::class)
    private fun factor(lexemes: Lex): AbstractExpression {
        val lexeme = lexemes.lex
        return when (lexeme.type) {
            LexType.NUMBER -> {
                val num: Double
                try {
                    num = lexeme.value.toDouble()
                } catch (nfe: NumberFormatException) {
                    throw OverflowException()
                }
                Const(num)
            }

            LexType.MINUS -> CheckedNegate(factor(lexemes))
            LexType.LB -> {
                val result = expr(lexemes)
                lexemes.lex
                result
            }
            else -> throw MissingExpressionException()
        }
    }

    @Throws(Exception::class)
    private fun expr(lexemes: Lex): AbstractExpression {
        return plusMinus(lexemes)
    }

    @Throws(Exception::class)
    private fun multDiv(lexemes: Lex): AbstractExpression {
        var value = factor(lexemes)
        while (true) {
            val lexeme = lexemes.lex
            value = when (lexeme.type) {
                LexType.MUL -> CheckedMultiply(value, factor(lexemes))
                LexType.DIV -> CheckedDivide(value, factor(lexemes))
                else -> {
                    lexemes.back()
                    return value
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun plusMinus(lexemes: Lex): AbstractExpression {
        var value = multDiv(lexemes)
        while (true) {
            val lexeme = lexemes.lex
            value = when (lexeme.type) {
                LexType.PLUS -> CheckedAdd(value, multDiv(lexemes))
                LexType.MINUS -> CheckedSubtract(value, multDiv(lexemes))
                else -> {
                    lexemes.back()
                    return value
                }
            }
        }
    }
}
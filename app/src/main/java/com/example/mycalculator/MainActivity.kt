package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 最後の計算に使うための変数を定義
        val operands = setOf("+", "-", "×", "÷")
        var operand = "+"
        var answer: Double = 0.0
        var value: String = ""



        // ボタンをクリックした時の処理
        one.setOnClickListener {
            addText("1")
        }
        two.setOnClickListener {
            addText("2")
        }
        three.setOnClickListener {
            addText("3")
        }
        four.setOnClickListener {
            addText("4")
        }
        five.setOnClickListener {
            addText("5")
        }
        six.setOnClickListener {
            addText("6")
        }
        seven.setOnClickListener {
            addText("7")
        }
        eight.setOnClickListener {
            addText("8")
        }
        nine.setOnClickListener {
            addText("9")
        }
        zero.setOnClickListener {
            addText("0")
        }
        doubleZero.setOnClickListener {
            addText("00")
        }

        percent.setOnClickListener {
            if (formula.text.isNotEmpty() && lastIsNumber()) {
                addText("%")
            }
        }

        division.setOnClickListener {
            operatorControl("÷", "+", "×")
        }

        multi.setOnClickListener {
            operatorControl( "×","÷", "+")
        }

        minus.setOnClickListener {
            if (formula.text.isNotEmpty() && lastChar() == "+") {
                formula.text = formula.text.removeSuffix(lastChar())
                addText("-")
            } else if (formula.text.isNotEmpty() && lastChar() != "-") {
                addText("-")
            } else if (formula.text.isEmpty()) {
                addText("-")
            }
        }

        plus.setOnClickListener {
            operatorControl("+", "×", "÷")
        }

        dot.setOnClickListener {
            addText(".")
        }

        // clearがクリックされた時の処理
        clear.setOnClickListener {
            formula.text = ""
        }

        //deleteがクリックされた時の処理
        delete.setOnClickListener {
            if (formula.text.isNotEmpty()) {
                formula.text = formula.text.dropLast(1)
            }
        }

        equal.setOnClickListener {
            for (i in formula.text.indices) {
                // 演算子を発見
                if (operands.contains(formula.text[i].toString())) {
                    answer = calculator(operand, answer, value)
                    value = ""
                    operand = formula.text[i].toString()

                } else {
                    value += formula.text[i].toString()
                    if ( i == formula.text.length-1) {
                        println("FINISH")
                        answer = calculator(operand, answer, value)
                    }
                }
            }
            formula.text = answer.toString()
            // 計算に必要なパラメータを初期化
            operand = "+"
            answer = 0.0
            value = ""

        }



    }

    // 数字や演算子がクリックされた時にformulaTextを更新する関数
    private fun addText(x: String) {
        // すでに入力されている文字列を取得
        var original_text = formula.text.toString()
        // クリックされた文字を入力済みの文字列に追加
        var new_text = original_text + x
        // 表示されている文字列を更新
        formula.text = new_text
    }

    // 直前の入力文字（formulaTextのラスト１文字）が数値0~9であるかを確認する関数
    private fun lastIsNumber(): Boolean {
        // 正規表現
        val regex = "^[0-9]"
        val p: Pattern = Pattern.compile(regex)
        // 直前の入力文字を取得
        val lastChar = formula.text[formula.text.length - 1]
        val m: Matcher = p.matcher(lastChar.toString())
        return m.find()
    }

    // 直前の入力文字（formulaTextのラスト１文字）が数値0~9、あるいは%であるかを確認する関数
    private fun lastIsNumOrPercent(): Boolean {
        // 正規表現
        val regex = "^[0-9%]"
        val p: Pattern = Pattern.compile(regex)
        // 直前の入力文字を取得
        val lastChar = formula.text[formula.text.length - 1]
        val m: Matcher = p.matcher(lastChar.toString())
        return m.find()
    }

    // 直前の入力文字（formulaTextのラスト１文字）を取得
    private fun lastChar(): String {
        // 直前の入力文字を取得
        val lastChar = formula.text[formula.text.length - 1].toString()
        return lastChar
    }

    //
    private  fun operatorControl(target: String, x: String, y: String) {
        // 直前の入力文字が➖（マイナス）だった場合、マイナスを消す
        if (formula.text.length == 1 && formula.text == "-") {
            formula.text = ""
            addText(target)
        } else if (formula.text.isNotEmpty() && lastChar() == "-") {
            formula.text = formula.text.removeSuffix(lastChar())
            addText(target)
        } else if (formula.text.isNotEmpty() && lastIsNumOrPercent()) {
            addText(target)
        } else if (lastChar() == x || lastChar() == y) {
            //formula.text = formula.text.toString().replace(lastChar(), "÷")
            formula.text = formula.text.removeSuffix(lastChar())
            addText(target)
        }
    }

    // "0"、"00"の入力を指示する関数
    /*
    private fun zeroOperation() {
        if (formula.text.isNotEmpty() && lastChar() != "%" && lastChar() != "+"
                                      && lastChar() != "-" && lastChar() != "×"
                                      && lastChar() != "÷") {

        }
    }

     */

    // イコール"＝"を押して計算結果を表示するための関数
    private fun calculator(operand: String, temp: Double, value: String): Double {

        var valueForCal = value

        if (value[value.length - 1].toString() == "%") {
            var tempValue = value.removeSuffix("%")
            valueForCal = (tempValue.toDouble() / 100).toString()
        }

        var answer = temp

        when (operand) {
            "+" -> answer += valueForCal.toDouble()
            "-" -> answer -= valueForCal.toDouble()
            "×" -> answer *= valueForCal.toDouble()
            "÷" -> answer /= valueForCal.toDouble()
        }
        return answer
    }
}

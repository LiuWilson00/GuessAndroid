package com.roy.guess

import android.util.Log
import kotlin.random.Random

class SecretNumber {

    var secret: Int = randomNewNumber()
    var count = 0
    fun randomNewNumber(): Int {
        return Random.nextInt(10) + 1

    }

    fun validate(number: Int): Int {
        count++
        return number - secret
    }

    fun reset() {
        secret = randomNewNumber()
        count = 0
    }
}


fun main() {
    val TAG = "SecretNumber"
    var secretNumber = SecretNumber()
    Log.d(TAG, "secret:" + secretNumber.secret)
}
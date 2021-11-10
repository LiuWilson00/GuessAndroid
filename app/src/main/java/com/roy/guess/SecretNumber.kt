package com.roy.guess

import kotlin.random.Random

class SecretNumber {
    val secret: Int = Random.nextInt(10) + 1
    var count = 0
    fun validate(number: Int): Int {
        count++
        return number - secret
    }
}


fun main() {
    var secretNumber = SecretNumber()
    println(secretNumber.secret)
    println(secretNumber.validate(2))
}
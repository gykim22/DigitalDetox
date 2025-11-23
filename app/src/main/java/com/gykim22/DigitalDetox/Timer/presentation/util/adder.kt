package com.gykim22.DigitalDetox.Timer.presentation.util

import android.util.Log.i
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

/**
 * 공부시간과 휴식시간을 합쳐 18시간 이상인 지 체크하는 함수입니다.
 * @author Kim Giyun
 */
fun adder(formattedString1: String, formattedString2: String): Boolean {
    try {
        val seconds1 = parseToSecond(formattedString1)
        val seconds2 = parseToSecond(formattedString2)

        /* 18시간 이상일 시 true 반환 */
        return seconds1 + seconds2 >= 64800

    } catch (e: Exception) {
        println("Error processing time string: ${e.message}")

        /* 에러일 시 안전하게 false 리턴. */
        return false
    }
}

/**
 * 정규화된 시간을 초 단위로 변환하는 함수입니다.
 * @author Kim Giyun
 */
fun parseToSecond(formattedString: String): Int {
    val str = formattedString.split(":")

    /**
     *  MM:SS -> str.size == 2
     *  HH:MM:SS -> str.size == 3
     *  */
    return when (str.size) {
        2 -> {
            str[0].toInt() * 60 + str[1].toInt()
        }
        3 -> {
            str[0].toInt() * 3600 + str[1].toInt() * 60 + str[2].toInt()
        }
        else -> throw IllegalArgumentException("잘못된 양식입니다.")
    }
}

/**
 * 밀리초단위를 시분초로 변환하는 함수입니다.
 * @author Kim Giyun
 */
fun parseToHMS(rawTime: Long): String {
    val sec = TimeUnit.MILLISECONDS.toSeconds(rawTime)

    val hour = TimeUnit.SECONDS.toHours(sec)
    val minute = TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.SECONDS.toHours(sec) * 60
    val second = TimeUnit.SECONDS.toSeconds(sec) - TimeUnit.SECONDS.toMinutes(sec) * 60

    return when {
        hour > 0 -> String.format("%02d시간 %02d분 %02d초", hour, minute, second)
        minute > 0 -> String.format("%02d분 %02d초", minute, second)
        else -> String.format("%02d초", second)
    }
}
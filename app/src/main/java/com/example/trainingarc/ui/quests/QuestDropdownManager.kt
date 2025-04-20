package com.example.trainingarc.ui.quests

import android.os.CountDownTimer
import com.example.trainingarc.R
import java.util.Calendar
import java.util.concurrent.TimeUnit


data class QuestDropdownManager (
    val dailyQuests: List<String>,
    val weeklyQuests: List<String>
) {
    val allQuests: List<String>
        get() = dailyQuests + weeklyQuests
//    private fun startCountdownTimer(millisUntilFinished: Long) {
//        countdownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
//                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
//                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
//                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
//
//                _binding?.let { binding ->
//                    binding.timerText.text = String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds)
//
//                    val color = when {
//                        days >= 4 -> getColorCompat(R.color.timer_sky_blue)
//                        days == 2L -> getColorCompat(R.color.timer_warning_yellow)
//                        days == 1L -> getColorCompat(R.color.timer_warning_orange)
//                        days == 0L && hours > 12L -> getColorCompat(R.color.timer_warning_red)
//                        else -> getColorCompat(R.color.timer_dark_red)
//                    }
//
//                    binding.timerText.setTextColor(color)
//                }
//            }
//
//            override fun onFinish() {
//                _binding?.let { binding ->
//                    binding.timerText.text = "0:00:00:00"
//                    binding.timerText.setTextColor(getColorCompat(R.color.timer_dark_red))
//                }
//            }
//        }.start()
//    }
}

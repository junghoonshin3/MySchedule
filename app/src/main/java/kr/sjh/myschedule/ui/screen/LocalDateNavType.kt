package kr.sjh.myschedule.ui.screen

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import java.time.LocalDate

class LocalDateNavType : NavType<LocalDate>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): LocalDate? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getSerializable(key, LocalDate::class.java)
        } else {
            bundle.getSerializable(key) as LocalDate
        }
    }

    override fun parseValue(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    override fun put(bundle: Bundle, key: String, value: LocalDate) {
        bundle.putSerializable(key, value)
    }
}
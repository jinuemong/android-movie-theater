package woowacourse.movie.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import woowacourse.movie.R
import java.io.Serializable
import java.text.DecimalFormat
import java.time.LocalDate

object MovieUtils {
    fun convertPeriodFormat(period: List<LocalDate>): String {
        val start = period.first().toString()
        val end = period.last().toString()
        return "%s~%s".format(start, end)
    }

    fun convertScreeningInfoFormat(screeningTimeCount: Int): String = "${screeningTimeCount}개의 상영 시간"

    fun convertAmountFormat(
        context: Context,
        amount: Int,
    ): String = DecimalFormat(context.getString(R.string.all_price)).format(amount)

    fun makeToast(
        context: Context,
        message: String,
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

    fun <T : Serializable> Bundle.bundleSerializable(
        key: String,
        clazz: Class<T>,
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializable(key, clazz)
        } else {
            this.getSerializable(key) as T?
        }
    }

    fun <T : Serializable> Intent.intentSerializable(
        key: String,
        clazz: Class<T>,
    ): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }

    @SuppressLint("ResourceType")
    fun FragmentManager.navigateToBottomMenu(
        @LayoutRes
        fragmentLayoutResource: Int,
        nextFragment: Fragment,
    ) {
        commit {
            setReorderingAllowed(true)
            replace(fragmentLayoutResource, nextFragment)
        }
    }

    @SuppressLint("ResourceType")
    fun FragmentManager.navigateToFragment(
        @LayoutRes
        fragmentLayoutResource: Int,
        nextFragment: Fragment,
    ) {
        commit {
            add(fragmentLayoutResource, nextFragment)
            addToBackStack(null)
        }
    }
}

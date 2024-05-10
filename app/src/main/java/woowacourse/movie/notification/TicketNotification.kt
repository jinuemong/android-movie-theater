package woowacourse.movie.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import woowacourse.movie.R
import woowacourse.movie.model.movie.ScreeningDateTime
import woowacourse.movie.notification.TicketNotificationReceiver.Companion.MOVIE_TITLE
import woowacourse.movie.view.reservation.ReservationDetailActivity.Companion.RESERVATION_TICKET_ID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TicketNotification {
    private const val NOTIFICATION_ID = "notificationId"
    private const val DEFAULT_NOTIFICATION_ID = 0
    const val PENDING_REQUEST_CODE = 0
    private const val ALARM_MINUTE = -30

    fun setNotification(
        context: Context,
        ticketId: Long,
        movieTitle: String,
        screeningDateTime: ScreeningDateTime,
    ) {
        val screeningTime =
            makeScreeningTime(
                context = context,
                screeningDateTime = screeningDateTime,
            )
        if (!isValidScreeningTime(screeningTime)) return

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val pendingIntent =
            makePendingIntent(
                context = context,
                movieTitle = movieTitle,
                ticketId = ticketId,
            )

        val calendar =
            Calendar.getInstance().apply {
                timeInMillis = screeningTime
                add(Calendar.MINUTE, ALARM_MINUTE)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent,
                )
            }
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent,
            )
        }
    }

    private fun makeScreeningTime(
        context: Context,
        screeningDateTime: ScreeningDateTime,
    ): Long {
        return SimpleDateFormat(
            context.getString(R.string.ticket_date_pattern_format),
            Locale.getDefault(),
        ).parse(
            context.getString(R.string.ticket_date_format).format(
                screeningDateTime.date,
                screeningDateTime.time,
            ),
        )?.time ?: -1L
    }

    private fun isValidScreeningTime(screeningTime: Long): Boolean {
        val currentDateTime = Calendar.getInstance().timeInMillis
        return screeningTime != -1L && currentDateTime < screeningTime
    }

    private fun makePendingIntent(
        context: Context,
        movieTitle: String,
        ticketId: Long,
    ): PendingIntent {
        val intent = Intent(context, TicketNotificationReceiver::class.java)
        intent.putExtra(MOVIE_TITLE, movieTitle)
        intent.putExtra(RESERVATION_TICKET_ID, ticketId)

        return PendingIntent.getBroadcast(
            context,
            PENDING_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )
    }

    fun getNextNotificationTicketId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(NOTIFICATION_ID, MODE_PRIVATE)
        val currentId = getTicketId(sharedPreferences)
        val nextId = currentId + 1
        if (isTicketIdValidate(nextId)) {
            saveTicketId(nextId, sharedPreferences)
        } else {
            resetTicketId(sharedPreferences)
        }
        return nextId
    }

    private fun resetTicketId(sharedPreferences: SharedPreferences) {
        saveTicketId(DEFAULT_NOTIFICATION_ID, sharedPreferences)
    }

    private fun isTicketIdValidate(ticketId: Int): Boolean {
        return ticketId < Int.MAX_VALUE - 1
    }

    private fun getTicketId(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(NOTIFICATION_ID, DEFAULT_NOTIFICATION_ID)
    }

    private fun saveTicketId(
        nextId: Int,
        sharedPreferences: SharedPreferences,
    ) {
        sharedPreferences.edit().putInt(NOTIFICATION_ID, nextId).apply()
    }
}

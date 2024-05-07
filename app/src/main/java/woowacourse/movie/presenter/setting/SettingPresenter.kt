package woowacourse.movie.presenter.setting

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import woowacourse.movie.notification.TicketNotification
import woowacourse.movie.repository.ReservationTicketRepository

class SettingPresenter(
    private val view: SettingContract.View,
    private val repository: ReservationTicketRepository,
) : SettingContract.Presenter {
    override fun loadSavedSetting(isPushSetting: Boolean) {
        view.showSavedSetting(isPushSetting)
    }

    override suspend fun settingAlarm(
        context: Context,
        isPushSetting: Boolean,
    ) {
        view.saveSetting(isPushSetting)
        withContext(Dispatchers.IO) {
            val tickets = repository.loadReservationTickets()
            if (isPushSetting) {
                tickets.forEach { reservationTicket ->
                    withContext(Dispatchers.Main) {
                        TicketNotification.setNotification(
                            context = context,
                            ticketId = reservationTicket.ticketId,
                            movieTitle = reservationTicket.movieTitle,
                            screeningDateTime = reservationTicket.screeningDateTime,
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    TicketNotification.cancelNotification(context)
                }
            }
        }
    }
}

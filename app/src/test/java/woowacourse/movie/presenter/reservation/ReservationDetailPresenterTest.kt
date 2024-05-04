package woowacourse.movie.presenter.reservation

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.movie.db.screening.ScreeningDao
import woowacourse.movie.db.theater.TheaterDao

@ExtendWith(MockKExtension::class)
class ReservationDetailPresenterTest {
    @MockK
    private lateinit var view: ReservationDetailContract.View
    private lateinit var presenter: ReservationDetailContract.Presenter

    @BeforeEach
    fun setUp() {
        presenter = ReservationDetailPresenter(view, ScreeningDao(), TheaterDao())
    }

    @Test
    fun `현재 영화 정보를 불러온다`() {
        every { view.showMovieInformation(any()) } just runs
        presenter.loadMovie(0)
        verify { view.showMovieInformation(any()) }
    }

    @Test
    fun `상영 기간을 불러온다`() {
        every { view.showScreeningPeriod(any()) } just runs
        presenter.loadScreeningPeriod(0)
        verify { view.showScreeningPeriod(any()) }
    }

    @Test
    fun `상영 시간을 불러온다`() {
        every { view.showScreeningTimes(any(), any()) } just runs
        presenter.loadScreeningTimes(0, "2024-03-01")
        verify { view.showScreeningTimes(any(), any()) }
    }

    @Test
    fun `예약 인원이 1인 상태에서 마이너스 버튼을 누르면 토스트를 띄워서 유저에게 알린다`() {
        every { view.showResultToast() } just runs
        presenter.decreaseHeadCount(1)
        verify { view.showResultToast() }
    }

    @Test
    fun `예약 인원이 2인 상태에 마이너스 버튼을 누르면 예약 인원은 1이 된다`() {
        every { view.changeHeadCount(any()) } just runs
        presenter.decreaseHeadCount(2)
        verify { view.changeHeadCount(1) }
    }

    @Test
    fun `예약 인원이 20인 상태에서 플러스 버튼을 누르면 토스트를 띄어서 유저에게 알린다`() {
        every { view.showResultToast() } just runs
        presenter.increaseHeadCount(20)
        verify { view.showResultToast() }
    }

    @Test
    fun `예약 인원이 2인 상태에서 플러스 버튼을 누르면 예약 인원은 3이 된다`() {
        every { view.changeHeadCount(any()) } just runs
        presenter.increaseHeadCount(2)
        verify { view.changeHeadCount(3) }
    }

    @Test
    fun `영화 정보가 존재하지 않는다면 토스트를 띄어서 유저에게 알린다`() {
        every { view.showIdErrorToast() } just runs
        presenter.checkIdValidation(-1, 0)
        verify { view.showIdErrorToast() }
    }

    @Test
    fun `극장 정보가 존재하지 않는다면 토스트를 띄어서 유저에게 알린다`() {
        every { view.showIdErrorToast() } just runs
        presenter.checkIdValidation(0, -1)
        verify { view.showIdErrorToast() }
    }
}

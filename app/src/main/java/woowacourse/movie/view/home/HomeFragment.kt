package woowacourse.movie.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import woowacourse.movie.R
import woowacourse.movie.databinding.FragmentHomeBinding
import woowacourse.movie.db.advertisement.AdvertisementDao
import woowacourse.movie.db.screening.ScreeningDao
import woowacourse.movie.presenter.home.ReservationHomeContract
import woowacourse.movie.presenter.home.ReservationHomePresenter
import woowacourse.movie.utils.MovieUtils.navigateToFragment
import woowacourse.movie.view.home.adapter.MovieCatalogAdapter
import woowacourse.movie.view.theater.TheaterSelectionFragment

class HomeFragment : Fragment(), ReservationHomeContract.View {
    private val presenter = ReservationHomePresenter(this)
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initMovieRecyclerView()
    }

    override fun navigateToDetail(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, movieId)
        setFragmentResult(BUNDLE_MOVIE_ID, bundle)
        parentFragmentManager.navigateToFragment(
            R.layout.fragment_home,
            TheaterSelectionFragment(),
        )
    }

    private fun initMovieRecyclerView() {
        val movieCatalogAdapter =
            MovieCatalogAdapter(
                ScreeningDao().findAll(),
                AdvertisementDao().findAll(),
            ) { movie ->
                presenter.loadMovie(movie)
            }
        binding.recyclerViewReservationHome.apply {
            adapter = movieCatalogAdapter
        }
    }

    companion object {
        const val MOVIE_ID = "movieId"
        const val BUNDLE_MOVIE_ID = "bundleMovieId"
    }
}

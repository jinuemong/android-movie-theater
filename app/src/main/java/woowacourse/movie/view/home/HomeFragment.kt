package woowacourse.movie.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import woowacourse.movie.R
import woowacourse.movie.databinding.FragmentHomeBinding
import woowacourse.movie.db.advertisement.AdvertisementDao
import woowacourse.movie.db.screening.ScreeningDao
import woowacourse.movie.presenter.home.ReservationHomeContract
import woowacourse.movie.presenter.home.ReservationHomePresenter
import woowacourse.movie.view.home.adapter.MovieCatalogAdapter
import woowacourse.movie.view.theater.TheaterSelectionFragment

class HomeFragment : Fragment(), ReservationHomeContract.View {
    private val presenter = ReservationHomePresenter(this)
    private lateinit var binding: FragmentHomeBinding
    private lateinit var movieCatalogAdapter: MovieCatalogAdapter

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
        loadMovieData()
    }

    @SuppressLint("ResourceType")
    override fun navigateToDetail(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, movieId)
        val bottomSheetDialogFragment = TheaterSelectionFragment()
        bottomSheetDialogFragment.arguments = bundle
        bottomSheetDialogFragment
            .show(childFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun initMovieRecyclerView() {
        movieCatalogAdapter =
            MovieCatalogAdapter { movie ->
                presenter.loadMovie(movie)
            }
        binding.recyclerViewHome.apply {
            adapter = movieCatalogAdapter
        }
    }

    private fun loadMovieData()  {
        movieCatalogAdapter.updateMovies(ScreeningDao().findAll())
        movieCatalogAdapter.updateAds(AdvertisementDao().findAll())
    }

    companion object {
        const val MOVIE_ID = "movieId"
    }
}

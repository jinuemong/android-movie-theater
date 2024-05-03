package woowacourse.movie.view.theater.adapter

import androidx.recyclerview.widget.RecyclerView
import woowacourse.movie.databinding.ItemTheaterBinding
import woowacourse.movie.model.theater.Theater
import woowacourse.movie.utils.MovieUtils.convertScreeningInfoFormat

class TheaterSelectionViewHolder(
    private val binding: ItemTheaterBinding,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.theaterViewHolder = this
    }

    fun bind(
        item: Theater,
        theaterId: OnSelectTheater,
    ) {
        with(binding) {
            name.text = item.name
            val screeningTimeCount =
                item.screeningTimes.weekDay.size + item.screeningTimes.weekEnd.size
            screeningInfo.text = convertScreeningInfoFormat(
                screeningTimeCount,
                root.context,
            )
            constraintLayoutItemTheater.setOnClickListener {
                theaterId(item.theaterId)
            }
        }
    }
}

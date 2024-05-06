package woowacourse.movie.view.setting

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import woowacourse.movie.R
import woowacourse.movie.databinding.FragmentSettingBinding
import woowacourse.movie.presenter.setting.SettingContract
import woowacourse.movie.presenter.setting.SettingPresenter
import woowacourse.movie.presenter.theater.TheaterSelectionPresenter

class SettingFragment : Fragment(), SettingContract.View {
    private val presenter = SettingPresenter(this)
    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadSavedSetting(getPushSetting())
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showSavedSetting(isPushSetting: Boolean) {
        binding.switchButton.isChecked = isPushSetting
    }

    private fun getPushSetting(): Boolean {
        val sharedPreference = context?.getSharedPreferences(PUSH_SETTING, MODE_PRIVATE)
        return sharedPreference?.getBoolean(PUSH_SETTING, false) ?: false
    }

    private fun initView(){
        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            setPushSetting(isChecked)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun setPushSetting(currentPushSetting: Boolean){
        val sharedPreference = context?.getSharedPreferences(PUSH_SETTING, MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putBoolean(PUSH_SETTING,currentPushSetting)?.apply()
    }

    companion object{
        private const val PUSH_SETTING = "pushSetting"
    }
}

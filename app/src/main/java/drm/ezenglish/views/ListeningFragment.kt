package drm.ezenglish.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import drm.ezenglish.App
import drm.ezenglish.R
import drm.ezenglish.activities.MainActivity
import drm.ezenglish.databinding.FragmentListeningBinding
import drm.ezenglish.viewmodels.ListeningViewModel

class ListeningFragment : Fragment() {

    private lateinit var viewModel: ListeningViewModel
    private lateinit var binding: FragmentListeningBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainActivity = activity as MainActivity
        viewModel = ListeningViewModel(mainActivity.application as App, mainActivity.storage, mainActivity.dbReference)
        binding.viewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listening, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.questions.value = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.cleanUp()
    }
}
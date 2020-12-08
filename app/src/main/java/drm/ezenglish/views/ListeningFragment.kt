package drm.ezenglish.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import drm.ezenglish.App
import drm.ezenglish.R
import drm.ezenglish.activities.MainActivity
import drm.ezenglish.adapters.QuestionListeningAdapter
import drm.ezenglish.databinding.FragmentListeningBinding
import drm.ezenglish.viewmodels.ListeningViewModel

class ListeningFragment : Fragment() {

    private lateinit var viewModel: ListeningViewModel
    private lateinit var binding: FragmentListeningBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainActivity = activity as MainActivity
        val resourceId = mainActivity.getResourceAudio()
        viewModel = ListeningViewModel(mainActivity.application as App, resourceId)
        binding.viewModel = viewModel
        bindAdapter(mainActivity.dbReference, resourceId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listening, container, false)
        binding.lifecycleOwner = this
        binding.rvListeningQuestions.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            val divider = DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        viewModel.cleanUp()
    }

    private fun bindAdapter(dbReference: DatabaseReference, resourceId: Int) {
        when (resourceId) {
            R.raw.books -> viewModel.getQuestionsFromFirebase(dbReference,"books") {
                viewModel.questions.value = it
                binding.rvListeningQuestions.adapter = QuestionListeningAdapter(this, viewModel.questions.value!!)
            }
            R.raw.family -> viewModel.getQuestionsFromFirebase(dbReference,"family") {
                viewModel.questions.value = it
                binding.rvListeningQuestions.adapter = QuestionListeningAdapter(this, viewModel.questions.value!!)
            }
            R.raw.restaurant -> viewModel.getQuestionsFromFirebase(dbReference,"restaurant") {
                viewModel.questions.value = it
                binding.rvListeningQuestions.adapter = QuestionListeningAdapter(this, viewModel.questions.value!!)
            }
        }
    }
}
package drm.ezenglish.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import drm.ezenglish.R
import drm.ezenglish.databinding.RecyclerviewItemQuestionBinding
import drm.ezenglish.entities.ListeningQuestion
import drm.ezenglish.viewmodels.ListeningQuestionViewModel

class QuestionListeningAdapter(private val lifecycleOwner: LifecycleOwner, private val questions: List<ListeningQuestion>)
    : RecyclerView.Adapter<QuestionListeningAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(private val binding: RecyclerviewItemQuestionBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = ListeningQuestionViewModel()
        }

        fun bind(question: ListeningQuestion) {
            binding.apply {
                viewModel?.question?.value = question
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerviewItemQuestionBinding>(
            LayoutInflater.from(parent.context), R.layout.recyclerview_item_question, parent, false)
        binding.lifecycleOwner = lifecycleOwner
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount() = questions.size
}
package drm.ezenglish.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import drm.ezenglish.App
import drm.ezenglish.R
import drm.ezenglish.databinding.RecyclerviewItemQuestionBinding
import drm.ezenglish.entities.Question
import drm.ezenglish.viewmodels.ListeningQuestionViewModel

class QuestionAdapter(private val app: App, private val questions: List<Question>)
    : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(app: App, private val binding: RecyclerviewItemQuestionBinding)
        : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            binding.viewModel = ListeningQuestionViewModel(app)
        }

        fun bind(question: Question) {
            binding.apply {
                viewModel?.question?.value = question
                executePendingBindings()
            }
        }

        override fun getLifecycle() = lifecycleRegistry
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerviewItemQuestionBinding>(
            LayoutInflater.from(parent.context), R.layout.recyclerview_item_question, parent, false
        )
        val holder = QuestionViewHolder(app, binding)
        binding.lifecycleOwner = holder
        return holder
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount() = questions.size

    override fun getItemViewType(position: Int) = position
}
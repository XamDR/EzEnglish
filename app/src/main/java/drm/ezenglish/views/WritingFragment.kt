package drm.ezenglish.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import drm.ezenglish.App
import drm.ezenglish.R
import drm.ezenglish.activities.MainActivity
import drm.ezenglish.adapters.TopicAdapter
import drm.ezenglish.databinding.FragmentWritingBinding
import drm.ezenglish.viewmodels.WritingViewModel

class WritingFragment : Fragment() {

    private lateinit var viewModel: WritingViewModel
    private lateinit var binding: FragmentWritingBinding
    private val mimeType = "text/html; charset=utf-8"
    private val encoding = "UTF-8"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainActivity = activity as MainActivity
        viewModel = WritingViewModel(mainActivity.application as App)
        binding.viewModel = viewModel
        bindAdapter(mainActivity)
        binding.topics.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadHtmlData(mainActivity, position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
        loadHtmlData(mainActivity, 0)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_writing, container, false)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.setBackgroundColor(Color.argb(0, 0, 198, 82))
        binding.webView.settings.javaScriptEnabled = true
        return binding.root
    }

    private fun bindAdapter(mainActivity: MainActivity) {
        viewModel.getTopicsFromFirebase(mainActivity.dbReference) {
            val adapter = TopicAdapter(mainActivity, android.R.layout.simple_spinner_dropdown_item, it)
            binding.topics.adapter = adapter
        }
    }

    private fun loadHtmlData(activity: MainActivity, position: Int) {
        val topics = activity.resources.getStringArray(R.array.topics)
        viewModel.getSentenceFromFirebase(activity.dbReference, topics[position]) {
            val htmlData = viewModel.render(it).trimStart('\n')
            binding.webView.loadDataWithBaseURL(null, htmlData, mimeType, encoding, null)
        }
    }
}
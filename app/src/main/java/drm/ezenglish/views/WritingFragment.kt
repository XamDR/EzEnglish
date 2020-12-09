package drm.ezenglish.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import drm.ezenglish.R
import drm.ezenglish.activities.MainActivity
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
        viewModel = WritingViewModel()
        binding.viewModel = viewModel
        viewModel.getSentenceFromFirebase(mainActivity.dbReference) {
            val htmlData = viewModel.render(it).trimStart('\n')
            binding.webView.loadDataWithBaseURL(null, htmlData, mimeType, encoding, null)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_writing, container, false)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.setBackgroundColor(Color.argb(0, 0, 198, 82))
        binding.webView.settings.javaScriptEnabled = true
        return binding.root
    }
}
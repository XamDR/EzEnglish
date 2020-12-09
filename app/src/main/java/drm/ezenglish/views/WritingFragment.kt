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
import drm.ezenglish.databinding.FragmentWritingBinding
import drm.ezenglish.entities.Quiz
import drm.ezenglish.entities.SelectionRange

class WritingFragment : Fragment() {

    private lateinit var binding: FragmentWritingBinding
    private val mimeType = "text/html; charset=utf-8"
    private val encoding = "UTF-8"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_writing, container, false)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.setBackgroundColor(Color.argb(0, 0, 198, 82))
        binding.webView.settings.javaScriptEnabled = true
        val quiz = Quiz("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        listOf(SelectionRange(6, 5), SelectionRange(30, 7)))
        val htmlData = quiz.render().trimStart('\n')
        binding.webView.loadDataWithBaseURL(null, htmlData, mimeType, encoding, null)
        return binding.root
    }
}
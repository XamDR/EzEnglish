package drm.ezenglish.views

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import drm.ezenglish.App
import drm.ezenglish.R
import drm.ezenglish.databinding.FragmentSpeakingBinding
import drm.ezenglish.viewmodels.SpeakingViewModel

class SpeakingFragment : Fragment() {

    private lateinit var viewModel: SpeakingViewModel
    private lateinit var binding: FragmentSpeakingBinding
    private val REQUEST_RECORD_AUDIO_PERMISSION = 100

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = SpeakingViewModel(requireActivity().application as App)
        binding.viewModel = viewModel
        viewModel.permissionToRecordAudio.observe(viewLifecycleOwner, {
            if (!it) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
                ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speaking, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            viewModel.permissionToRecordAudio.value = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }

        if (viewModel.permissionToRecordAudio.value!!) {
            viewModel.recordSpeech()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.cleanUp()
    }
}
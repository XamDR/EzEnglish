package drm.ezenglish.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import drm.ezenglish.R
import java.util.*
import kotlin.concurrent.timerTask

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logo = findViewById<ImageView>(R.id.logo)
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.subtitle)
        val footerText = findViewById<TextView>(R.id.footerText)

        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        logo.startAnimation(animation)
        title.startAnimation(animation)
        subtitle.startAnimation(animation)
        footerText.startAnimation(animation)

        Timer().schedule(timerTask {
            Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(this)
            }
        }, 3000)
    }
}
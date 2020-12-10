package drm.ezenglish.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import drm.ezenglish.R
import drm.ezenglish.util.UserPreferences
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

	lateinit var dbReference: DatabaseReference
	private lateinit var firebaseDatabase: FirebaseDatabase
	private lateinit var appBarConfiguration: AppBarConfiguration
	private var checkedTheme by Delegates.notNull<Int>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val toolbar: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(toolbar)

		val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
		val navView: NavigationView = findViewById(R.id.nav_view)
		val navController = findNavController(R.id.nav_host_fragment)

		appBarConfiguration = AppBarConfiguration(
			setOf(R.id.nav_home, R.id.nav_listening, R.id.nav_speaking, R.id.nav_writing),
			drawerLayout
		)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView.setupWithNavController(navController)

		initFirebase()
		checkedTheme = checkTheme()
	}

	private fun initFirebase() {
		FirebaseApp.initializeApp(application)
		firebaseDatabase = FirebaseDatabase.getInstance()
		dbReference = firebaseDatabase.reference
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.action_about -> showAboutDialog()
			R.id.action_settings -> showSettingsDialog()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment)
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}

	fun getResourceAudio(): Int {
		val audios = resources.obtainTypedArray(R.array.audios)
		try {
			val choice = (Math.random() * audios.length()).toInt()
			return audios.getResourceId(choice, R.raw.family)
		}
		finally {
		    audios.recycle()
		}
	}

	private fun showAboutDialog() {
		AlertDialog.Builder(this, R.style.DialogTheme).apply {
			setTitle(R.string.app_name)
			setMessage(R.string.developer)
			setPositiveButton(R.string.ok_button) { dialog, _ -> dialog.dismiss() }
			create().apply { show() }
		}
	}

	private fun showSettingsDialog() {
		val builder = AlertDialog.Builder(this)
		builder.setTitle(getString(R.string.change_theme))
		val styles = arrayOf("Claro", "Oscuro", "Definido por Android")
		val checkedItem = checkedTheme

		builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->
			when (which) {
				0 -> {
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
					UserPreferences(this).appTheme = 0
				}
				1 -> {
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
					UserPreferences(this).appTheme = 1
				}
				2 -> {
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
					UserPreferences(this).appTheme = 2
				}
			}
			checkedTheme = which
			delegate.applyDayNight()
			dialog.dismiss()
		}
		val dialog = builder.create()
		dialog.show()
	}

	private fun checkTheme(): Int {
		val theme = UserPreferences(this).appTheme
		when (theme) {
			0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
		}
		delegate.applyDayNight()
		return theme
	}
}
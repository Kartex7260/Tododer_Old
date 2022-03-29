package kartex.tododer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LaunchActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)

		finish()
		val intent = Intent(this, MainActivity::class.java)
		startActivity(intent)
	}
}
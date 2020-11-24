package dev.idkwuu.akeyboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    
    // Tap 8 times to enable secret modes!
    private var tapCount = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enableKeyboard = findViewById<Button>(R.id.enable)
        enableKeyboard.setOnClickListener {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }

        val licenses = findViewById<Button>(R.id.licenses)
        licenses.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/idkwuu/TheAKeyboard/blob/senpai/ACKNOWLEDGEMENTS.md")))
        }

        val preferences = getSharedPreferences("dev.idkwuu.theakeyboard.prefs", Context.MODE_PRIVATE)
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setOnClickListener {
            if (preferences.getBoolean("secret_mode", false)) {
                // Secret mode was already enabled
                AlertDialog.Builder(this)
                    .setTitle(R.string.disable_secret_mode)
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        // Save secret_mode to SharedPreferences
                        preferences.edit {
                            this.putBoolean("secret_mode", false)
                        }
                    }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                // Tap 8 times to enable secret mode!
                if (tapCount < 7) {
                    tapCount++
                } else {
                    tapCount = 0
                    AlertDialog.Builder(this)
                        .setTitle(R.string.enable_secret_mode)
                        .setPositiveButton(R.string.yes) { dialog, _ ->
                            // Save secret_mode to SharedPreferences
                            preferences.edit {
                                this.putBoolean("secret_mode", true)
                            }
                            // Show toast
                            Toast.makeText(this, R.string.secret_mode_was_enabled, Toast.LENGTH_LONG).show()
                        }
                        .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                        .show()

                }
            }
        }
    }
}
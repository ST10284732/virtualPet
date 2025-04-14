package com.example.virtualpet

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Stats
    private var happiness = 100
    private var health = 100
    private var energy = 100

    // Views
    private lateinit var happinessBar: ProgressBar
    private lateinit var healthBar: ProgressBar
    private lateinit var energyBar: ProgressBar
    private lateinit var feedButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var sleepButton: ImageButton
    private lateinit var petImage: ImageView

    // Timer
    private val handler = Handler(Looper.getMainLooper())
    private val statDecreaseRunnable = object : Runnable {
        override fun run() {
            decreaseStats()
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Link Views
        petImage = findViewById(R.id.imgPet)
        happinessBar = findViewById(R.id.happinessBar)
        healthBar = findViewById(R.id.healthBar)
        energyBar = findViewById(R.id.energyBar)
        feedButton = findViewById(R.id.btnFeed)
        playButton = findViewById(R.id.btnPlay)
        sleepButton = findViewById(R.id.btnSleep)

        updateAllBars()

        // Button Clicks
        feedButton.setOnClickListener {
            health += 10
            energy -= 5
            checkStats()
        }

        playButton.setOnClickListener {
            happiness += 10
            energy -= 10
            checkStats()
        }

        sleepButton.setOnClickListener {
            energy += 15
            happiness -= 5
            checkStats()
        }

        // Start the timer
        handler.post(statDecreaseRunnable)
    }

    private fun decreaseStats() {
        happiness -= 5
        health -= 3
        energy -= 2
        checkStats()
    }

    private fun checkStats() {
        // Clamp values
        happiness = happiness.coerceIn(0, 100)
        health = health.coerceIn(0, 100)
        energy = energy.coerceIn(0, 100)

        updateAllBars()

        if (happiness == 0 || health == 0 || energy == 0) {
            Toast.makeText(this, "Game Over! Your pet needs better care.", Toast.LENGTH_LONG).show()
            handler.removeCallbacks(statDecreaseRunnable)
        }
    }

    private fun updateProgressBar(bar: ProgressBar, value: Int) {
        bar.progress = value

        val color = when {
            value <= 25 -> android.R.color.holo_red_dark
            value <= 75 -> android.R.color.holo_orange_light
            else -> android.R.color.holo_green_light
        }

        bar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(this, color))
    }

    private fun updateAllBars() {
        updateProgressBar(happinessBar, happiness)
        updateProgressBar(healthBar, health)
        updateProgressBar(energyBar, energy)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(statDecreaseRunnable)
    }
}
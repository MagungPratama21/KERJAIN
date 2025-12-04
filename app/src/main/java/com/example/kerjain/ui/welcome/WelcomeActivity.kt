package com.example.kerjain

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kerjain.databinding.ActivityWelcomeBinding
import kotlin.apply
import kotlin.collections.indices
import kotlin.jvm.java
import kotlin.ranges.until

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var adapter: WelcomeAdapter
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val images = listOf(
            R.drawable.splash1,
            R.drawable.splash2,
            R.drawable.splash3
        )

        adapter = WelcomeAdapter(images)
        binding.viewPager.adapter = adapter

        setupIndicators(images.size)
        setCurrentIndicator(0)

        val autoSlide = object : Runnable {
            override fun run() {
                val nextItem = (binding.viewPager.currentItem + 1) % images.size
                binding.viewPager.currentItem = nextItem
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(autoSlide, 3000)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
            }
        })

        binding.btnLanjutkan.setOnClickListener {
            val intent = Intent(this, TipePekerjaanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(8, 0, 8, 0)
        binding.indicatorLayout.removeAllViews()

        for (i in indicators.indices) {
            indicators[i] = ImageView(this).apply {
                setImageResource(R.drawable.indicator_inactive)
                layoutParams = params
            }
            binding.indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == index)
                imageView.setImageResource(R.drawable.indicator_active)
            else
                imageView.setImageResource(R.drawable.indicator_inactive)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}

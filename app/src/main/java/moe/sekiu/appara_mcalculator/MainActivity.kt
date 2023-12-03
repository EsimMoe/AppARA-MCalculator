package moe.sekiu.appara_mcalculator

import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest
import moe.sekiu.appara_mcalculator.databinding.ActivityMainBinding
import moe.sekiu.appara_mcalculator.kit.ByteArrayConverter
import moe.sekiu.appara_mcalculator.kit.color
import moe.sekiu.appara_mcalculator.kit.plus
import moe.sekiu.appara_mcalculator.kit.spannable


class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calc.setOnClickListener {
            val packageName = "${binding.packageNameInput.text}"
            runCatching { packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES) }
                .onFailure { th ->
                    if (th is NameNotFoundException)
                    {
                        binding.ARAMOutput.setText(" ")
                        Snackbar.make(binding.root, "App $packageName not found", Snackbar.LENGTH_SHORT).gravityTop().show()
                    }
                }.onSuccess { info ->
                    val md = MessageDigest.getInstance("SHA")
                    val md256 = MessageDigest.getInstance("SHA-256")
                    val araM = mutableListOf<String>()
                    for (signature in info.signatures)
                    {
                        araM.add(ByteArrayConverter.byteArrayToHexString(md.digest(signature.toByteArray())).uppercase())
                        araM.add(ByteArrayConverter.byteArrayToHexString(md256.digest(signature.toByteArray())).uppercase())
                    }
                    binding.ARAMOutput.setText(spannable { araM.mapIndexed { index, it -> color(getNextColor(), "$it${if (index == araM.lastIndex) "" else "\n"}") }.reduce { x, y -> x + y } })
                }
        }
    }

    private val colors = arrayOf(
        0xFF0000FF.toInt(),  // BLUE
        0xFFFF00FF.toInt(),  // MAGENTA
        0xFF00FF00.toInt(),  // GREEN
        0xFFFFFF00.toInt(),  // YELLOW
        0xFF444444.toInt(),  // DKGRAY
        0xFFFF0000.toInt(),  // RED
        0xFF00FFFF.toInt(),  // CYAN
    )

    private var index = 0

    private fun getNextColor() : Int {
        val color = colors[index]
        index = (index + 1) % colors.size
        return color
    }

    fun Snackbar.gravityTop() = apply { this.view.layoutParams = (this.view.layoutParams as FrameLayout.LayoutParams).apply { gravity = Gravity.TOP } }
}
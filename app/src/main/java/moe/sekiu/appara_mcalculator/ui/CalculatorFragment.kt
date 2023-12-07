package moe.sekiu.appara_mcalculator.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.ui.fragment.base.BaseViewModelFragment
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest
import moe.sekiu.appara_mcalculator.R
import moe.sekiu.appara_mcalculator.databinding.FragmentCalculatorBinding
import moe.sekiu.appara_mcalculator.kit.ByteArrayConverter
import moe.sekiu.appara_mcalculator.kit.color
import moe.sekiu.appara_mcalculator.kit.plus
import moe.sekiu.appara_mcalculator.kit.spannable
import moe.sekiu.appara_mcalculator.viewmodel.CalculatorViewModel

class CalculatorFragment : BaseViewModelFragment<CalculatorViewModel, FragmentCalculatorBinding>()
{
    override val viewModel by hiltNavGraphViewModels<CalculatorViewModel>(R.id.calculatorFragment)

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.selectAppButton.setOnClickListener {
            findNavController().navigate(CalculatorFragmentDirections.actionCalculatorFragmentToChooseAppFragment())
        }
        binding.calc.setOnClickListener {
            val packageName = "${binding.packageNameInput.text}"
            runCatching { requireActivity().packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES) }
                .onFailure { th ->
                    if (th is PackageManager.NameNotFoundException)
                    {
                        binding.ARAMOutput.setText(R.string.blank)
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

    override fun onViewStateRestored(savedInstanceState : Bundle?)
    {
        super.onViewStateRestored(savedInstanceState)
        binding.packageNameInput.setText(viewModel.packageName)
    }

    override fun inflateBinding(inflater : LayoutInflater, container : ViewGroup?) : FragmentCalculatorBinding
    {
        return FragmentCalculatorBinding.inflate(inflater, container, false)
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
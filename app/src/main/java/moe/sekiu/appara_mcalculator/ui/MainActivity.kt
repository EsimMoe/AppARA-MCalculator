package moe.sekiu.appara_mcalculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import moe.sekiu.appara_mcalculator.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.scott.coroutineproofofconcept


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

import java.lang.Error

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders
            .of(this, MainViewModel.FACTORY(NetworkService())).get(MainViewModel::class.java)

        viewModel.getSeveralTokensWithDelay(data ="what")


        viewModel.token.observe(this, Observer { value ->
            value?.let {
                main_activity_text_view.text = it
            }
        })

    }
}

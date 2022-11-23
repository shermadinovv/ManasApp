package com.example.onlinestore.act

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.onlinestore.databinding.ActivityEditAdsBinding
import com.example.onlinestore.diaologs.DialogSpinnerHelper
import com.example.onlinestore.utils.CityHelper

class EditAdsAct : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        val listCountry = CityHelper.getAllCountries(this)

        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listCountry)

    }
}
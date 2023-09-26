package com.example.onlinestore.act

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.example.onlinestore.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.onlinestore.adapters.ImageAdapter
import com.example.onlinestore.data.Ad
import com.example.onlinestore.database.DbManager
import com.example.onlinestore.databinding.ActivityEditAdsBinding
import com.example.onlinestore.diaologs.DialogSpinnerHelper
import com.example.onlinestore.frag.FragmentCloseInterface
import com.example.onlinestore.frag.ImageListFrag
import com.example.onlinestore.utils.CityHelper

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    lateinit var rootElement : ActivityEditAdsBinding
    var chooseImageFrag : ImageListFrag? = null
    private var dialog = DialogSpinnerHelper()
    lateinit var imageAdapter : ImageAdapter
    private val dbManager = DbManager()
    var editImagePos = 0
    private var isEditState = false
    //private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    private fun init(){
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        if(rootElement.tvCity.text.toString() != getString(R.string.select_city)){
            rootElement.tvCity.text = getString(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvCountry.text.toString()
        if(selectedCountry != getString(R.string.select_country)){
            val listCity = CityHelper.getAllCities(selectedCountry,this)
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity)
        }
        else{
            Toast.makeText(this, R.string.none_selected_country, Toast.LENGTH_LONG).show()
        }
    }

    fun onClickGetImages(view: View){
        rootElement.scroolViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, ImageListFrag(this))
        fm.commit()
    }

    fun onClickSelectCat(view: View){
        val listCity = resources.getStringArray(R.array.category).toMutableList() as ArrayList
        dialog.showSpinnerDialog(this, listCity, rootElement.tvCat)
    }

    fun onClickPublish(view: View){
        dbManager.publishAd(fillAd())
    }

    private fun fillAd(): Ad{
        val ad: Ad
        rootElement.apply {
            ad = Ad(tvCountry.text.toString(),
                tvCity.text.toString(),
                edTel.text.toString(),
                edIndex.text.toString(),
                checkBoxWithSend.isChecked.toString(),
                tvCat.text.toString(),
                edPrice.text.toString(),
                edDes.text.toString(),
                dbManager.db.push().key)
        }
        return ad
    }


    override fun onFragClose(list: ArrayList<Bitmap>) {
        rootElement.scroolViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    fun openChooseImageFrag(newList : ArrayList<Uri>?){
        chooseImageFrag = ImageListFrag(this)
        if(newList != null) chooseImageFrag?.resizeSelectedImages(newList, true, this)
        rootElement.scroolViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()
    }
}

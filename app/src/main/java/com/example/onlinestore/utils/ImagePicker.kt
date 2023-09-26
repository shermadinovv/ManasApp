package com.example.onlinestore.utils

import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.onlinestore.R
import com.example.onlinestore.act.EditAdsAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGES = 998

    fun getOptions(imageCounter : Int): Options {
        val options = Options().apply{
            ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = 3                                                   //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            mode = Mode.Picture                                         //Option to select only pictures or videos or both
            flash = Flash.Auto                                          //Option to select flash type
            //preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }
        return options
    }

    fun getMultiImages(edAct: EditAdsAct , imageCounter: Int){
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)){ result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectImages(edAct, result.data)
                }
                else -> {}
            }

        }
    }



    fun addImages(edAct: EditAdsAct, imageCounter: Int) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    edAct.chooseImageFrag?.updateAdapter(result.data as ArrayList<Uri>, edAct)
                }
                else -> {}
            }

        }
    }

    fun getSingleImage(edAct: EditAdsAct) {
        val f = edAct.chooseImageFrag
        edAct.addPixToActivity(R.id.place_holder, getOptions(1)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    edAct.chooseImageFrag = f
                    openChooseImageFrag(edAct, f!!)
                    singleImage(edAct, result.data[0])
                }
                else -> {}
            }

        }
    }

    private fun openChooseImageFrag(edAct: EditAdsAct, f: Fragment){
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, f).commit()
    }

    private fun closePixFrag(edAct: EditAdsAct){
        val flist = edAct.supportFragmentManager.fragments
        flist.forEach {
            if(it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    fun getMultiSelectImages(edAct: EditAdsAct, uris: List<Uri>){
        if(uris.size > 1 && edAct.chooseImageFrag == null){
            edAct.openChooseImageFrag(uris as ArrayList<Uri>)
        }
        else if(uris.size == 1 && edAct.chooseImageFrag == null){
            CoroutineScope(Dispatchers.Main).launch {
                //edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                val bitMapArray = ImageManager.imageResize(uris as ArrayList<Uri>, edAct) as ArrayList<Bitmap>
                //edAct.rootElement.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.update(bitMapArray)
                closePixFrag(edAct)
            }
        }
    }

    private fun singleImage(edAct: EditAdsAct, uri: Uri){
        edAct.chooseImageFrag?.setSingleImage(uri, edAct.editImagePos)
    }
}
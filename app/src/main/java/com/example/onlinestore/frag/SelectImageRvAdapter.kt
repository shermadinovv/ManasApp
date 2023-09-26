package com.example.onlinestore.frag

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageSwitcher
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.onlinestore.R
import com.example.onlinestore.act.EditAdsAct
import com.example.onlinestore.databinding.SelectImageFragItemBinding
import com.example.onlinestore.utils.AdapterCallback
import com.example.onlinestore.utils.ImageManager
import com.example.onlinestore.utils.ImagePicker
import com.example.onlinestore.utils.ItemTouchMoveCallback

class  SelectImageRvAdapter(val adapterCallback: AdapterCallback) : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(view, parent.context , this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(private val viewBinding: SelectImageFragItemBinding, val context : Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(viewBinding.root){

        fun setData(bitmap: Bitmap){
            viewBinding.imEditImage.setOnClickListener{
                ImagePicker.getSingleImage(context as EditAdsAct)
                context.editImagePos = adapterPosition
            }

            viewBinding.imDelete.setOnClickListener{
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for(n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallback.onItemDelete()
            }

            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(viewBinding.imageContent, bitmap) //// imageView
            viewBinding.imageContent.setImageBitmap(bitmap)
        }
    }

    fun updateAdapter(newList : List<Bitmap>, needClear : Boolean){
        if(needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }

}
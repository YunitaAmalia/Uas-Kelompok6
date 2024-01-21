package com.kelompok6.deezerapi.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kelompok6.deezerapi.ArtistsFragmentDirections
import com.kelompok6.deezerapi.databinding.AlbumItemBinding
import com.kelompok6.deezerapi.models.album.Data


import com.bumptech.glide.Glide

class ArtistAlbumAdapter : RecyclerView.Adapter<ArtistAlbumAdapter.MyArtist>() {

    var liveData : List<Data>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyArtist {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyArtist(binding)
    }


    override fun onBindViewHolder(holder: MyArtist, position: Int) {
        holder.bind(liveData!![position])
        val musicImage = holder.binding.linearLayoutAlbum
        musicImage.setOnClickListener {
            val action = ArtistsFragmentDirections.actionArtistsFragmentToMusicFragment(liveData!![position].album.id)
            println("music id ${liveData!![position].album.id}")
            holder.itemView.findNavController().navigate(action)

        }
    }

    override fun getItemCount(): Int {
        if(liveData == null){
            return 0
        }else{
            return liveData!!.size

        }
    }

    inner class MyArtist(val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            println("$data")
            binding.txtName.text = data.title
            Glide.with(binding.imgAlbum)
                .load(data.album.cover_xl)
                .into(binding.imgAlbum)

        }
    }
    fun setListsAlbum(liveData: List<Data>?) {
            this.liveData = liveData
            notifyDataSetChanged()

    }
}
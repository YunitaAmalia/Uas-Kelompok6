package com.kelompok6.deezerapi.Adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.kelompok6.deezerapi.Room.DataDao
import com.kelompok6.deezerapi.Room.RoomDataBase
import com.kelompok6.deezerapi.databinding.MusicItemBinding
import com.kelompok6.deezerapi.models.album.favoriteMusic
import com.bumptech.glide.Glide


class MusicAdapter : RecyclerView.Adapter<MusicAdapter.MyArtist>() {
    private lateinit var db : RoomDataBase
    private lateinit var dataDao: DataDao
    var liveData : List<com.kelompok6.deezerapi.models.album.Data>? = null

    var mediaPlayer: MediaPlayer? = null
    var currentMedia: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyArtist {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyArtist(binding)
    }


    override fun onBindViewHolder(holder: MyArtist, position: Int) {
        holder.bind(liveData!![position])
        db = Room.databaseBuilder(holder.binding.root.context.applicationContext,
            RoomDataBase::class.java,"Music")
            .allowMainThreadQueries()
            .build()
        dataDao = db.dataDao()

        val dataExists = dataDao.checkIfDataExists(liveData!![position].id.toInt()) > 0

        if (dataExists == true) {
            // menampilkan data gambar jika ada dalam database
            holder.binding.imageView3.visibility = ViewGroup.VISIBLE
            holder.binding.favoriteImg.visibility = ViewGroup.GONE
        } else {
            // menyembunyikan data gambar jika tidak ada dalam database
            holder.binding.imageView3.visibility = ViewGroup.GONE
            holder.binding.favoriteImg.visibility = ViewGroup.VISIBLE
        }


        val favoriImg = holder.binding.favoriteImg

        favoriImg.setOnClickListener {
            val data = favoriteMusic(

                liveData!!.get(position).duration,
                liveData!!.get(position).id.toInt(),
                liveData!!.get(position).md5_image,
                liveData!!.get(position).preview,
                liveData!!.get(position).title,

            )
            dataDao.insert(data)
            notifyDataSetChanged()
            Toast.makeText(holder.itemView.context, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        if(liveData == null){
            return 0
        }else{
            return liveData!!.size
        }
    }

    inner class MyArtist(val binding: MusicItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: com.kelompok6.deezerapi.models.album.Data) {

            binding.titleFavoriteMusic.text = data.title

            val minutes = data.duration / 60
            val seconds = data.duration % 60
            val durationText = String.format("%d:%02d", minutes, seconds)
            binding.durationFavoriteMusic.text = "Durasi : " + durationText


            binding.imgPlayMusic.setOnClickListener {
                val mediaList = data.preview

                if (mediaPlayer?.isPlaying == true && currentMedia == mediaList) {
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                    mediaPlayer?.release()
                    mediaPlayer = null
                    currentMedia = null
                } else {
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setOnCompletionListener {
                        currentMedia = null
                        mediaPlayer = null
                    }
                    mediaPlayer?.setDataSource(mediaList)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    currentMedia = mediaList
                }
            }
            Glide.with(binding.imageView)
                .load("https://e-cdns-images.dzcdn.net/images/cover/" + data.md5_image +"/500x500-000000-80-0-0.jpg")
                .into(binding.imageView)

        }
    }
    fun setMusicList(liveData: List<com.kelompok6.deezerapi.models.album.Data>?) {
            this.liveData = liveData
            notifyDataSetChanged()
    }
}
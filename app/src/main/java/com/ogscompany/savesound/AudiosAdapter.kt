package com.ogscompany.savesound

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class AudiosAdapter(private val files : MutableList<String>, private val filePathForSave : String) : RecyclerView.Adapter<AudiosAdapter.ViewHolder>() {

    private var player: MediaPlayer? = null
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.fileName)
        val playButton : Button = itemView.findViewById(R.id.playAudioButton)
        val stopButton : Button = itemView.findViewById(R.id.stopAudioButton)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.audios_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileName.text = files[position]
        holder.playButton.setOnClickListener {
            player = MediaPlayer().apply {
                setDataSource(filePathForSave + files[position])
                prepare()
                start()
            }
            holder.playButton.isInvisible = true
            holder.stopButton.isVisible = true
        }
        holder.stopButton.setOnClickListener {
            player?.release()
            player = null
            holder.stopButton.isInvisible = true
            holder.playButton.isVisible = true
        }
    }
    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        val file = File(filePathForSave, files[viewHolder.adapterPosition])
        file.delete()
        files.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }
}

package edu.uoc.pac4.ui.streams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac4.R
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.databinding.ItemStreamBinding
import java.text.NumberFormat

/**
 * Created by alex on 07/09/2020.
 * Updated by david on 01/01/21
 * Streams Adapter Binding
 */

class StreamsAdapter : ListAdapter<Stream, StreamsAdapter.StreamViewHolder>(streamsDiffCallback) {

    // Binding variable for this activity
    private lateinit var binding: ItemStreamBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        binding = ItemStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StreamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class StreamViewHolder(binding: ItemStreamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(stream: Stream) {

            // Set Stream Info
            binding.title.text = stream.userName
            binding.description.text = stream.title

            // Set Stream Image
            stream.thumbnailUrl
                ?.replace("{width}", "1014")
                ?.replace("{height}", "396")
                ?.let {
                    Glide.with(itemView)
                        .load(it)
                        .centerCrop()
                        .into(binding.imageView)
                }
            // Set Stream Views
            val formattedViews = NumberFormat.getInstance().format(stream.viewerCount)
            binding.viewsText.text = itemView.context.resources
                .getQuantityString(R.plurals.viewers_text, stream.viewerCount, formattedViews)
        }
    }

    companion object {
        private val streamsDiffCallback = object : DiffUtil.ItemCallback<Stream>() {

            override fun areItemsTheSame(oldItem: Stream, newItem: Stream): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stream, newItem: Stream): Boolean {
                return oldItem == newItem
            }
        }
    }
}
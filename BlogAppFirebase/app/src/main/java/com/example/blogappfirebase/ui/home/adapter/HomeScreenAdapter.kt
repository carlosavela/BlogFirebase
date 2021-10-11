package com.example.blogappfirebase.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blogappfirebase.core.BaseViewHolder
import com.example.blogappfirebase.core.TimeUtils
import com.example.blogappfirebase.core.hideIt
import com.example.blogappfirebase.data.model.Post
import com.example.blogappfirebase.databinding.PostItemViewBinding


class HomeScreenAdapter(
    private val listPost: List<Post>,
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        // inflaar la vista que va a tener cada post
        val itemBinding =
            PostItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        // Asignar todos los elementos a la vista
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(listPost[position])
        }
    }

    override fun getItemCount(): Int = listPost.size

    private inner class HomeScreenViewHolder(
        val binding: PostItemViewBinding,
        val context: Context
    ) : BaseViewHolder<Post>(binding.root) {

        override fun bind(item: Post) {

            Glide.with(context)
                .load(item.post_image)
                .centerCrop()
                .into(binding.postImage)

            Glide.with(context)
                .load(item.profile_picture)
                .centerCrop()
                .into(binding.imgProfilePicture)

            binding.txvProfileName.text = item.profile_name
            val createdAt = (item.created_at?.time?.div(1000L))?.let {
                TimeUtils.getTimeAgo(it.toInt())
            }
            binding.postTimestamp.text = createdAt
            if (item.post_description.isEmpty()) {
                binding.postDescription.hideIt()
            } else {
                binding.postDescription.text = item.post_description
            }
        }
    }
}

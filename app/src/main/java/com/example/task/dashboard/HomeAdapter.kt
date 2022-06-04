package com.example.task.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task.addnotes.AddNotesActivity
import com.example.task.databinding.ItemListBinding
import com.example.task.detail.DetailActivity
import com.example.task.model.Notes

class HomeAdapter(val context:Context):RecyclerView.Adapter<HomeAdapter.homeViewHolder>() {
    private var onItemClickListener:((detail: Notes)->Unit)? = null

    fun setOnItemClickListener(onItemClickListener:((detail: Notes)->Unit)){
        this.onItemClickListener = onItemClickListener
    }
    var deliveryDetails: List<Notes> = listOf()

        set(value) {
            notifyItemRangeRemoved(1, deliveryDetails.size)
            field = value
            notifyItemRangeInserted(1, deliveryDetails.size)
        }


    inner class homeViewHolder(val binding: ItemListBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(deliveryDetails: Notes){
            binding.listitem=deliveryDetails

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeViewHolder {
        val view=ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return homeViewHolder(view)
    }

    override fun onBindViewHolder(holder: homeViewHolder, position: Int) {
        val data=deliveryDetails[position]
        holder.bind(data)
        Glide.with(context)
            .load(data.imageUrl)
            .into(holder.binding.edtImage)
        holder.itemView.setOnClickListener {
            val intent=Intent(context,DetailActivity::class.java)
            intent.putExtra("title",data.title)
            intent.putExtra("des",data.description)
            intent.putExtra("image",data.imageUrl)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return deliveryDetails.size
    }

}
package com.example.smartpedalboard.placeholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartpedalboard.R

class ProfileAdapter:RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private var pfList: ArrayList<ProfileModel> = ArrayList()
    private var onClickItem:((ProfileModel) -> Unit)? = null

    class ProfileViewHolder(var view: View): RecyclerView.ViewHolder(view)
    {
    private var name = view.findViewById<TextView>(R.id.itemName)
    private var effect1= view.findViewById<TextView>(R.id.itemEffect1)
    private var effect2= view.findViewById<TextView>(R.id.itemEffect2)
    fun bindView(pf: ProfileModel){
        name.text = pf.name
        effect1.text = pf.effect1.toString()
        effect2.text = pf.effect2.toString()
    }
    }
    fun setOnClickItem(callback: (ProfileModel)-> Unit)
    {
        this.onClickItem = callback
    }
    fun addItems(items: ArrayList<ProfileModel>)
    {
        this.pfList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProfileViewHolder (
       LayoutInflater.from(parent.context).inflate(R.layout.layout,parent,false)
    )

    override fun getItemCount(): Int {
       return pfList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val pfd = pfList[position]
        holder.bindView(pfd)
        holder.itemView.setOnClickListener {(onClickItem?.invoke(pfd))  }
    }
}
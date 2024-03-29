package com.grevi.masakapa.util

import androidx.recyclerview.widget.DiffUtil

class DiffUtils<O, N>(private val oldLIst : List<O> ,private val newList : List<N>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldLIst.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItemPosition == newItemPosition
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldLIst[oldItemPosition] == newList[newItemPosition]
}
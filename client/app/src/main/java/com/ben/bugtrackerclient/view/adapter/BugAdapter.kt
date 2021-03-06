package com.ben.bugtrackerclient.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.databinding.BugItemBinding
import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.DataItem
import com.ben.bugtrackerclient.model.Priority
import com.ben.bugtrackerclient.model.RowType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BugAdapter: ListAdapter<DataItem, RecyclerView.ViewHolder>(BugDiffCallback()) {

    companion object {
        var onItemClickListener: ((Int) -> Unit)? = null
        var onEditItemClickListener: ((Bug) -> Unit)? = null
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun onAddHeaderAndItems(bugList: List<Bug>) {
        adapterScope.launch {
            val items = bugList.groupBy { it.priority }.flatMap {
                listOf(
                    DataItem.HeaderItem(Priority.values()[it.key!! - 1].name),
                    *(it.value.map { bug ->
                        (DataItem.BugItem(bug))
                    }.toTypedArray())
                )
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(val bugRow = getItem(position)) {
           is DataItem.HeaderItem -> (holder as TextViewHolder).onBind(bugRow)
           is DataItem.BugItem -> (holder as BugViewHolder).onBind(bugRow, position)
       }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).rowType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(RowType.values()[viewType]) {
            RowType.Header -> TextViewHolder.from(parent)
            RowType.Bug -> BugViewHolder.from(parent)
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.rv_header, parent, false)
                return TextViewHolder(view)
            }
        }

        private val headerText: TextView = view.findViewById(R.id.text_header)
        fun onBind(headerItem: DataItem.HeaderItem) {
            headerText.text = headerItem.name
        }
    }

    class BugViewHolder private constructor(private val binding: BugItemBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): BugViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = BugItemBinding.inflate(layoutInflater, parent, false)
                return BugViewHolder(view)
            }
        }

        @SuppressLint("SetTextI18n")
        fun onBind(bugItem: DataItem.BugItem, position: Int) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
            val outputFormat = SimpleDateFormat("MM/DD/YYYY", Locale.FRANCE)

            binding.deleteBug.setOnClickListener {
                bugItem.id?.toInt()?.let { id ->
                    onItemClickListener?.invoke(id)
                }
            }

            binding.bugEdit.setOnClickListener {
                onEditItemClickListener?.invoke(bugItem.bug)
            }

            binding.bugName.text = bugItem.bug.name
            binding.bugDesc.text = bugItem.bug.details
            binding.bugVersion.text = "Version: ${bugItem.bug.version}"
            binding.bugCreator.text = "Created by ${bugItem.bug.creator?.username}"
            binding.bugCreatedAt.text = "Created: ${outputFormat.format(inputFormat.parse(bugItem.bug.createdAt ?: "") ?: "")}"
            binding.bugUpdatedAt.text = "Updated: ${outputFormat.format(inputFormat.parse(bugItem.bug.updatedAt ?: "") ?: "")}"
        }
    }
}
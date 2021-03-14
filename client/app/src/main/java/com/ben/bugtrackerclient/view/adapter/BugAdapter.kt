package com.ben.bugtrackerclient.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.DataItem
import com.ben.bugtrackerclient.model.Priority
import com.ben.bugtrackerclient.model.RowType
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException
import java.text.SimpleDateFormat
import java.util.*

class BugAdapter: ListAdapter<DataItem, RecyclerView.ViewHolder>(BugDiffCallback()) {

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
           is DataItem.BugItem -> (holder as BugViewHolder).onBind(bugRow)
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

    class BugViewHolder(v: View): RecyclerView.ViewHolder(v) {
        companion object {
            fun from(parent: ViewGroup): BugViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.bug_item, parent, false)
                return BugViewHolder(view)
            }
        }

        private val bugName: MaterialTextView = v.findViewById(R.id.bug_name)
        private val bugDesc: MaterialTextView = v.findViewById(R.id.bug_desc)
        private val bugCreatedAt: MaterialTextView = v.findViewById(R.id.bug_createdAt)
        private val bugUpdatedAt: MaterialTextView = v.findViewById(R.id.bug_updatedAt)
        private val bugVersion: MaterialTextView = v.findViewById(R.id.bug_version)

        @SuppressLint("SetTextI18n")
        fun onBind(bugItem: DataItem.BugItem) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE)
            val outputFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.FRANCE)
            bugName.text = bugItem.bug.name
            bugDesc.text = bugItem.bug.details
            bugVersion.text = "Version: ${bugItem.bug.version}"
            bugCreatedAt.text = outputFormat.format(inputFormat.parse(bugItem.bug.createdAt ?: "") ?: "")
            bugUpdatedAt.text = outputFormat.format(inputFormat.parse(bugItem.bug.updatedAt ?: "") ?: "")
        }
    }
}
package com.ben.bugtrackerclient.model

enum class RowType { Bug, Header }

sealed class DataItem(val rowType: RowType) {
    abstract val id: Long?

    data class BugItem(val bug: Bug): DataItem(RowType.Bug) {
        override val id: Long?
            get() = bug.id?.toLong()
    }

    data class HeaderItem(val name: String): DataItem(RowType.Header) {
        override val id: Long
            get() = Long.MIN_VALUE
    }
}

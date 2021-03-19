package hr.mfllipovic.github.entities

import com.google.gson.annotations.SerializedName

data class SearchResults(
    @SerializedName("total_count")
    val totalCount: Number,
    val incompleteResults: Boolean,
    val items: List<Repository>
)
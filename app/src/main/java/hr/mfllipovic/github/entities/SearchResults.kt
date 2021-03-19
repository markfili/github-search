package hr.mfllipovic.github.entities

data class SearchResults(
    val totalCount: Number,
    val incompleteResults: Boolean,
    val items: List<Repository>
)
package hr.mfllipovic.github.entities

enum class SortByParam {
    none,
    stars,
    forks,
    updated
}

enum class OrderParam {
    none,
    asc,
    desc
}

data class SearchFilter(val query: String, val sort: SortByParam, val order: OrderParam) {

    fun toMap(): Map<String, String> {
        val map = mutableMapOf(
            "q" to query
        )
        if (sort != SortByParam.none) map["sort"] = sort.name
        if (order != OrderParam.none) map["order"] = order.name
        return map
    }
}
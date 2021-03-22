package hr.mfllipovic.github.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import hr.mfllipovic.github.BR

enum class SortByParam {
    match,
    stars,
    forks,
    updated
}

enum class OrderParam {
    original,
    asc,
    desc
}

class SearchFilter : BaseObservable() {
    @get:Bindable
    var query: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.query)
        }

    @get:Bindable
    var sort: SortByParam = SortByParam.match
        set(value) {
            field = value
            notifyPropertyChanged(BR.sort)
        }

    @get:Bindable
    var order: OrderParam = OrderParam.original
        set(value) {
            field = value
            notifyPropertyChanged(BR.order)
        }

    fun toMap(): Map<String, String> {
        val map = mutableMapOf(
            "q" to query
        )
        if (sort != SortByParam.match) map["sort"] = sort.name
        if (order != OrderParam.original) map["order"] = order.name
        return map
    }

    override fun toString(): String {
        return "SearchFilter [ query $query, sort ${sort.name}, order ${order.name} ]"
    }
}
package hr.mfllipovic.github.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import hr.mfllipovic.github.BR

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

class SearchFilter : BaseObservable() {
    @get:Bindable
    var query: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.query)
        }

    @get:Bindable
    var sort: SortByParam = SortByParam.none
        set(value) {
            field = value
            notifyPropertyChanged(BR.sort)
        }

    @get:Bindable
    var order: OrderParam = OrderParam.none
        set(value) {
            field = value
            notifyPropertyChanged(BR.order)
        }

    fun toMap(): Map<String, String> {
        val map = mutableMapOf(
            "q" to query
        )
        if (sort != SortByParam.none) map["sort"] = sort.name
        if (order != OrderParam.none) map["order"] = order.name
        return map
    }
}
package hr.mfllipovic.github.screens.search.utils

import com.airbnb.epoxy.EpoxyDataBindingPattern
import hr.mfllipovic.github.R

/**
 * Config class used by Epoxy to generate data binding for prefixed layouts.
 */
@EpoxyDataBindingPattern(rClass = R::class, layoutPrefix="search_result")
interface SearchEpoxyDataBindingConfig
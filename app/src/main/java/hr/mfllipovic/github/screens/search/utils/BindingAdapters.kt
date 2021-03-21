package hr.mfllipovic.github.screens.search.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapters {
    companion object {

        @JvmStatic
        @BindingAdapter("networkCircularImgSrc")
        fun ImageView.setNetworkImage(networkCircularImgSrc: String?) {
            if (networkCircularImgSrc != null) {
                Glide.with(this.context)
                    .load(networkCircularImgSrc)
                    .circleCrop()
                    .into(this)
            }
        }
    }
}
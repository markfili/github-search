package hr.mfllipovic.github.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(@SerializedName("avatar_url") val avatarUrl: String) : Parcelable


package hr.mfllipovic.github.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("watchers_count") val watchersCount: Int,
    @SerializedName("forks_count") val forksCount: Int,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    val owner: Owner
) : Parcelable

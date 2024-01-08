package ua.com.honchar.arstudy.domain.repository.model

import ua.com.honchar.arstudy.R

data class Language(
    val id: Int,
    val code: String
) {

    fun getLangResId(): Int? = when (code) {
        "uk-UA" -> R.string.ukrainian
        "uk" -> R.string.ukrainian
        "en" -> R.string.english
        else -> null
    }
}

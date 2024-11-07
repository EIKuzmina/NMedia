package ru.netology.nmedia.util

    fun formatNumber (count: Int) : String {
        if (count in (1_000..9_999)) return (count / 100 /10.0).toString() + "K"
        if (count in (10_000..999_999)) return (count / 1_000).toString() + "K"
        if (count >= 1_000_000) return (count / 10_0000 / 10.0).toString() + "M"
        return count.toString()
    }

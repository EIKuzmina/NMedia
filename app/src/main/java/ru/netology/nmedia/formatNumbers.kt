package ru.netology.nmedia

class formatNumbers {
    fun formatNumber (count: Int) : String {
        if (count in (1_000..999_999)) return (count / 100 /10.0).toString() + "K"
        if (count >= 1_000_000) return (count / 10_0000 / 10.0).toString() + "M"
        return count.toString()
    }
}
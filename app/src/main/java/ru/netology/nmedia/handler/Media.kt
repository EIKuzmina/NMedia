package ru.netology.nmedia.handler

import java.io.File

data class Media(val id: String)

data class MediaUpload(val file: File)

data class PushToken(val token: String)

data class PushMessage(val recipientId: Int?, val content: String)
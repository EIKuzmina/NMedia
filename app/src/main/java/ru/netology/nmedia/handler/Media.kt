package ru.netology.nmedia.handler

import java.io.File

data class Media(val id: String)

data class MediaUpload(val file: File)
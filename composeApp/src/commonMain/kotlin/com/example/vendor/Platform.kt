package com.example.vendor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
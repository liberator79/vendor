package com.vendor.home.domain

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpened(): Boolean {
    return this == CustomDrawerState.Opened
}

fun CustomDrawerState.opposite() : CustomDrawerState{
    return when(this){
        CustomDrawerState.Opened -> CustomDrawerState.Closed
        CustomDrawerState.Closed -> CustomDrawerState.Opened
    }
}
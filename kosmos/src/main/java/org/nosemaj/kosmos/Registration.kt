package org.nosemaj.kosmos

sealed class Registration {
    object UnconfirmedRegistration : Registration()
    object ConfirmedRegistration : Registration()
}

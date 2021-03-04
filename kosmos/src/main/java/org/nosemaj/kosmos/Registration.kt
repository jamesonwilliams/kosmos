package org.nosemaj.kosmos

sealed class Registration {
    class UnconfirmedRegistration(
        deliveryMedium: String,
        attributeName: String,
        destination: String
    ) : Registration()
    class ConfirmedRegistration(
        username: String,
        userId: String
    ) : Registration()
}

package uj.roomme.services

internal object Endpoints {

    const val USER_BASE = "/user"
    const val USER_POST = "$USER_BASE/test"
    const val USER_GET_FLATS = "$USER_BASE/{userId}/flats"
    const val FLAT_BASE = "/flat"
    const val FLAT_GET_FULL = "$FLAT_BASE/{flatId}/full"
}
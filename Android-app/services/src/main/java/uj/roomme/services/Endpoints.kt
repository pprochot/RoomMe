package uj.roomme.services

internal object Endpoints {

    const val USER_BASE = "/user"
    const val USER_POST = "$USER_BASE/test"
    const val USER_GET_FLATS = "$USER_BASE/{userId}/flats"
    const val FLAT_BASE = "/flat"
    const val FLAT_GET_FULL = "$FLAT_BASE/{flatId}/full"
    const val CREATE_NEW_SHOPPING_LIST = "$FLAT_BASE{flatId}/shopping-lists"
    const val GET_SHOPPING_LISTS = "$FLAT_BASE{flatId}/shopping-lists"
    const val ADD_SHOPPING_LIST_PRODUCTS = "$FLAT_BASE{flatId}/shopping-lists/{listId}/products"
}
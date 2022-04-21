package uj.roomme.app.fragments.shoppinglist.model

import uj.roomme.domain.product.ProductModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import java.time.OffsetDateTime

data class ShoppingListModel(
    val id: Int, val flatId: Int,
    var completorId: Int?, var completorName: String?,
    val name: String, val description: String?,
    val creationDate: OffsetDateTime, var completionDate: OffsetDateTime,
    val products: MutableList<ProductModel>
) {

    companion object {
        fun fromServiceModel(model: ShoppingListGetModel): ShoppingListModel {
            return ShoppingListModel(
                model.id, model.flatId,
                model.completorId, model.completorName,
                model.name, model.description,
                model.creationDate, model.completionDate,
                model.products.toMutableList()
            )
        }
    }
}
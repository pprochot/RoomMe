package uj.roomme.domain.shoppinglist

data class ReceiptFileModel (
    val fileName: String,
    val extension: String,
    val fileContent: String,
    val mimeType: String
)
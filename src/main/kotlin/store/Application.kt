package store

import store.controller.ConvenienceStoreController
import store.model.ProductManager
import store.model.PromotionManager

fun main() {
    val productManager = ProductManager()
    val promotionManager = PromotionManager()

    val convenienceStoreController = ConvenienceStoreController(productManager, promotionManager)

    convenienceStoreController.start()
}

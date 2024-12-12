package store

import store.controller.ConvenienceStoreController
import store.model.ProductManager
import store.model.PromotionManager
import store.view.OutputView

fun main() {
    val productManager = ProductManager()
    val promotionManager = PromotionManager()
    val outputView = OutputView()
    val convenienceStoreController = ConvenienceStoreController(productManager, promotionManager, outputView)

    convenienceStoreController.start()
}

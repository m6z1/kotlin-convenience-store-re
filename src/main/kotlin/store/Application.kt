package store

import store.controller.ConvenienceStoreController
import store.model.ProductManager
import store.model.PromotionManager
import store.view.InputView
import store.view.OutputView

fun main() {
    val productManager = ProductManager()
    val promotionManager = PromotionManager()
    val inputView = InputView()
    val outputView = OutputView()
    val convenienceStoreController = ConvenienceStoreController(productManager, promotionManager, inputView, outputView)

    convenienceStoreController.start()
}

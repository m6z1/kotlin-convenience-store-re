package store

import store.controller.ConvenienceStoreController
import store.model.ProductManager
import store.model.PromotionManager
import store.model.Receipt
import store.view.InputView
import store.view.OutputView

fun main() {
    val productManager = ProductManager()
    val promotionManager = PromotionManager()
    val inputView = InputView()
    val outputView = OutputView()
    val receipt = Receipt()
    val convenienceStoreController = ConvenienceStoreController(productManager, promotionManager, inputView, outputView, receipt)

    convenienceStoreController.start()
}

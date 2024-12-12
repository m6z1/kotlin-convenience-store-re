package store.controller

import store.model.ProductManager
import store.model.PromotionManager
import store.view.OutputView

class ConvenienceStoreController(
    private val productManager: ProductManager,
    private val promotionManager: PromotionManager,
    private val outputView: OutputView,
) {

    fun start() {
        val products = productManager.getProducts()
        outputView.printWelcome(products)
    }
}
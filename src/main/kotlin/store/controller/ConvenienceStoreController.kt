package store.controller

import store.model.ProductManager
import store.model.ProductToBuy
import store.model.PromotionManager
import store.view.InputView
import store.view.OutputView

class ConvenienceStoreController(
    private val productManager: ProductManager,
    private val promotionManager: PromotionManager,
    private val inputView: InputView,
    private val outputView: OutputView,
) {

    fun start() {
        val products = productManager.getProducts()
        outputView.printWelcome(products)

        while (true) {
            try {
                val productsToBuy = inputView.readProductsToBuy()
                productsToBuy.forEach { productManager.validateToBuy(it) }
                return executePayingLogic(productsToBuy)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun executePayingLogic(productsToBuy: List<ProductToBuy>) {

    }
}
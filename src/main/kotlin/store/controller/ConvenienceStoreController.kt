package store.controller

import store.model.ProductManager
import store.model.PromotionManager

class ConvenienceStoreController(
    private val productManager: ProductManager,
    private val promotionManager: PromotionManager,
) {

    fun start() {
        println(productManager.products)
        println(promotionManager.promotions)
    }
}
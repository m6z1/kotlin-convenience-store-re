package store.controller

import store.model.ProductManager
import store.model.ProductToBuy
import store.model.PromotionManager
import store.model.PromotionState
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
        productsToBuy.forEach { productToBuy ->
            val promotionState = promotionManager.checkProductPromotion(productToBuy)
            println(promotionState)
            when (promotionState) {
                PromotionState.NOT_APPLICABLE -> {
                    // TODO: 영수증 추가 (프로모션 미적용) }
                }

                PromotionState.MORE_PRODUCT_APPLICABLE -> {
                    // TODO: 하나 더 가져오면 프로모션 적용 가능
                }

                PromotionState.SOME_PRODUCT_OUT_OF_STOCK -> {
                    // TODO: 몇 개가 프로모션 적용 불가능 (안내) }
                }

                PromotionState.APPLICABLE -> {
                    // TODO: 영수증 추가 (프로모션 적용)
                }
            }
        }
    }
}
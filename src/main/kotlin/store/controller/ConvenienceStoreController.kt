package store.controller

import store.model.*
import store.view.InputView
import store.view.OutputView

class ConvenienceStoreController(
    private val productManager: ProductManager,
    private val promotionManager: PromotionManager,
    private val inputView: InputView,
    private val outputView: OutputView,
    private val receipt: Receipt,
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
            when (promotionState) {
                PromotionState.NOT_APPLICABLE -> addPurchasedProductOfRegularPrice(productToBuy, false)

                PromotionState.MORE_PRODUCT_APPLICABLE -> checkBuyerAddingProductForPromotion(productToBuy)

                PromotionState.SOME_PRODUCT_OUT_OF_STOCK -> {
                    // TODO: 몇 개가 프로모션 적용 불가능 (안내) }
                }

                PromotionState.APPLICABLE -> {
                    // TODO: 영수증 추가 (프로모션 적용)
                }
            }
        }
    }

    private fun addPurchasedProductOfRegularPrice(productToBuy: ProductToBuy, isPromotionPeriod: Boolean) {
        val productPrice = productManager.getRegularPrice(productToBuy.name)
        val purchasedProduct = PurchasedProductOfRegularPrice(
            name = productToBuy.name,
            count = productToBuy.buyCount,
            price = productPrice,
        )
        receipt.addPurchasedProductOfRegularPrice(purchasedProduct)
        //TODO: 재고 관리 업데이트
    }

    private fun checkBuyerAddingProductForPromotion(productToBuy: ProductToBuy) {
        while (true) {
            try {
                val response = inputView.readAddingProductForPromotion(productToBuy.name)
                return checkResponseOfAddingProductForPromotion(response, productToBuy)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun checkResponseOfAddingProductForPromotion(response: ResponseState, productToBuy: ProductToBuy) {
        when (response) {
            ResponseState.POSITIVE -> {
                val promotionProductInfo = productManager.getPromotionProduct(productToBuy.name)
                val countOfPromotion =
                    promotionManager.getGiveawaysCount(promotionProductInfo.promotion!!, productToBuy.buyCount)
                val purchasedProductOfPromotion = PurchasedProductOfPromotion(
                    name = productToBuy.name,
                    count = productToBuy.buyCount + 1,
                    price = promotionProductInfo.price,
                    countOfPromotion = countOfPromotion,
                )
                receipt.addPurchasedProductOfPromotion(purchasedProductOfPromotion)
            }

            ResponseState.NEGATIVE -> {
                val promotionProductInfo = productManager.getPromotionProduct(productToBuy.name)
                val promotion = promotionManager.getPromotion(promotionProductInfo.promotion!!)
                if (promotion.buy >= productToBuy.buyCount) {
                    addPurchasedProductOfRegularPrice(productToBuy, true)
                }
            }
        }
    }
}
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
                PromotionState.SOME_PRODUCT_OUT_OF_STOCK -> checkBuyerSomeProductToPayRegularPrice(productToBuy)
                PromotionState.APPLICABLE -> addPurchasedProductOfPromotion(productToBuy)
            }
        }
        checkMembership()
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
                    promotionManager.getGiveawaysCount(promotionProductInfo?.promotion!!, productToBuy.buyCount + 1)
                val purchasedProductOfPromotion = PurchasedProductOfPromotion(
                    name = productToBuy.name,
                    count = productToBuy.buyCount + 1,
                    price = promotionProductInfo.price,
                    countOfPromotion = countOfPromotion,
                )
                receipt.addPurchasedProductOfPromotion(purchasedProductOfPromotion)

                //TODO: 재고 관리 업데이트
            }

            ResponseState.NEGATIVE -> {
                val promotionProductInfo = productManager.getPromotionProduct(productToBuy.name)
                val promotion = promotionManager.getPromotion(promotionProductInfo?.promotion!!)
                if (promotion.buy >= productToBuy.buyCount) {
                    addPurchasedProductOfRegularPrice(productToBuy, true)
                    return
                }
                val countOfPromotion =
                    promotionManager.getGiveawaysCount(promotionProductInfo.promotion, productToBuy.buyCount)
                val purchasedProductOfPromotion = PurchasedProductOfPromotion(
                    name = productToBuy.name,
                    count = productToBuy.buyCount,
                    price = promotionProductInfo.price,
                    countOfPromotion = countOfPromotion,
                )
                receipt.addPurchasedProductOfPromotion(purchasedProductOfPromotion)

                //TODO: 재고 관리 업데이트
            }
        }
    }

    private fun checkBuyerSomeProductToPayRegularPrice(productToBuy: ProductToBuy) {
        while (true) {
            try {
                val productCountOfRegularPrice = promotionManager.getProductCountOfRegularPrice(productToBuy)
                val response = inputView.readSomeProductToPayRegularPrice(productToBuy.name, productCountOfRegularPrice)
                return checkResponseSomeProductToPayRegularPrice(response, productToBuy, productCountOfRegularPrice)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun checkResponseSomeProductToPayRegularPrice(
        response: ResponseState,
        productToBuy: ProductToBuy,
        productCountOfRegularPrice: Int,
    ) {
        when (response) {
            ResponseState.POSITIVE -> {
                val promotionProductInfo = productManager.getPromotionProduct(productToBuy.name)
                val countOfPromotion =
                    promotionManager.getGiveawaysCount(
                        promotionProductInfo?.promotion!!,
                        productToBuy.buyCount - productCountOfRegularPrice
                    )
                val purchasedProductOfPromotion = PurchasedProductOfPromotion(
                    name = productToBuy.name,
                    count = productToBuy.buyCount,
                    price = promotionProductInfo.price,
                    countOfPromotion = countOfPromotion,
                )
                receipt.addPurchasedProductOfPromotion(purchasedProductOfPromotion)

                // TODO: 재고 관리 업데이트
            }

            ResponseState.NEGATIVE -> {
                val promotionProductInfo = productManager.getPromotionProduct(productToBuy.name)
                val countOfPromotion =
                    promotionManager.getGiveawaysCount(
                        promotionProductInfo?.promotion!!,
                        productToBuy.buyCount - productCountOfRegularPrice,
                    )
                val purchasedProductOfPromotion = PurchasedProductOfPromotion(
                    name = productToBuy.name,
                    count = productToBuy.buyCount - productCountOfRegularPrice,
                    price = promotionProductInfo.price,
                    countOfPromotion = countOfPromotion,
                )
                receipt.addPurchasedProductOfPromotion(purchasedProductOfPromotion)

                //TODO: 재고 관리 업데이트
            }
        }
    }

    private fun addPurchasedProductOfPromotion(productToBuy: ProductToBuy) {
        val product = productManager.getPromotionProduct(productToBuy.name)
        val countOfPromotion = promotionManager.getGiveawaysCount(product?.promotion!!, productToBuy.buyCount)
        val purchasedProduct = PurchasedProductOfPromotion(
            name = productToBuy.name,
            count = productToBuy.buyCount,
            price = product.price,
            countOfPromotion = countOfPromotion,
        )
        receipt.addPurchasedProductOfPromotion(purchasedProduct)

        //TODO: 재고 관리 업데이트
    }

    private fun checkMembership() {
        while (true) {
            try {
                val response = inputView.readMembership()
                return checkResponseMembership(response)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun checkResponseMembership(response: ResponseState) {
        when (response) {
            ResponseState.POSITIVE -> receipt.addMembershipDiscount()
            ResponseState.NEGATIVE -> Unit
        }
        showPurchasedProducts()
    }

    private fun showPurchasedProducts() {
        val purchasedProductsOfRegularPrice = receipt.getPurchasedProductsOfRegularPrice()
        val purchasedProductsOfPromotion = receipt.getPurchasedProductsOfPromotion()
        outputView.printPurchasedProducts(purchasedProductsOfRegularPrice, purchasedProductsOfPromotion)
        if (purchasedProductsOfPromotion.all { it.countOfPromotion != 0 }) {
            outputView.printGiveaways(purchasedProductsOfPromotion)
        }
        showReceiptSummary()
    }

    private fun showReceiptSummary() {
        outputView.printReceiptSummary(
            buyCount = receipt.getTotalPurchasedProductsCount(),
            totalMoney = receipt.getTotalMoney(),
            promotionDiscount = receipt.getPromotionDiscount(),
            membershipDiscount = receipt.membershipDiscount,
            moneyToPay = receipt.getMoneyToPay(),
        )
        checkMoreShopping()
    }

    private fun checkMoreShopping() {
        while (true) {
            try {
                val response = inputView.readMoreShopping()
                return checkResponseMoreShopping(response)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun checkResponseMoreShopping(response: ResponseState) {
        when (response) {
            ResponseState.POSITIVE -> {
                receipt.reset()
                return start()
            }

            ResponseState.NEGATIVE -> return
        }
    }
}
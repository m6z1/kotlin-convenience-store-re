package store.model

class Receipt {
    private val purchasedProductsOfRegularPrice = mutableListOf<PurchasedProductOfRegularPrice>()
    private val purchasedProductsOfPromotion = mutableListOf<PurchasedProductOfPromotion>()

    fun addPurchasedProductOfRegularPrice(purchasedProduct: PurchasedProductOfRegularPrice) {
        purchasedProductsOfRegularPrice.add(purchasedProduct)
    }

    fun addPurchasedProductOfPromotion(purchasedProduct: PurchasedProductOfPromotion) {
        purchasedProductsOfPromotion.add(purchasedProduct)
    }
}
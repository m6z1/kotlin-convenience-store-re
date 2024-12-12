package store.model

class Receipt {
    private val purchasedProductsOfRegularPrice = mutableListOf<PurchasedProductOfRegularPrice>()
    private val purchasedProductsOfPromotion = mutableListOf<PurchasedProductOfPromotion>()
    private var membershipDiscount = 0

    fun addPurchasedProductOfRegularPrice(purchasedProduct: PurchasedProductOfRegularPrice) {
        purchasedProductsOfRegularPrice.add(purchasedProduct)
    }

    fun addPurchasedProductOfPromotion(purchasedProduct: PurchasedProductOfPromotion) {
        purchasedProductsOfPromotion.add(purchasedProduct)
    }

    fun addMembershipDiscount() {
        val discount = purchasedProductsOfRegularPrice.sumOf { product ->
            product.price * product.count
        } * 0.3

        if (discount.toInt() >= 8000) {
            membershipDiscount = 8000
            return
        }
        membershipDiscount = discount.toInt()
    }
}
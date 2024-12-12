package store.model

class Receipt {
    private val purchasedProductsOfRegularPrice = mutableListOf<PurchasedProductOfRegularPrice>()
    private val purchasedProductsOfPromotion = mutableListOf<PurchasedProductOfPromotion>()
    var membershipDiscount = 0
        private set

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

    fun getPurchasedProductsOfRegularPrice() = purchasedProductsOfRegularPrice.map { it.copy() }.toList()

    fun getPurchasedProductsOfPromotion() = purchasedProductsOfPromotion.map { it.copy() }.toList()

    fun getTotalPurchasedProductsCount(): Int {
        return purchasedProductsOfPromotion.sumOf { it.count } + purchasedProductsOfRegularPrice.sumOf { it.count }
    }

    fun getTotalMoney(): Int {
        var totalMoney = 0
        totalMoney += purchasedProductsOfPromotion.sumOf { it.price * it.count }
        totalMoney += purchasedProductsOfRegularPrice.sumOf { it.price * it.count }
        return totalMoney
    }

    fun getPromotionDiscount(): Int {
        return purchasedProductsOfPromotion.sumOf { it.price * it.countOfPromotion }
    }

    fun getMoneyToPay(): Int {
        return getTotalMoney() - getPromotionDiscount() - membershipDiscount
    }
}
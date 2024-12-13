package store.view

import store.model.Product
import store.model.PurchasedProductOfPromotion
import store.model.PurchasedProductOfRegularPrice
import java.text.DecimalFormat

class OutputView {

    fun printWelcome(products: List<Product>) {
        println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.\n")
        products.forEach { product ->
            if (product.quantity == 0) {
                if (product.promotion == null) {
                    println("- ${product.name} ${THOUSAND_COMMA.format(product.price)}원 재고 없음")
                    return@forEach
                }
                println("- ${product.name} ${THOUSAND_COMMA.format(product.price)}원 재고 없음 ${product.promotion}")
                return@forEach
            }
            println("- ${product.name} ${THOUSAND_COMMA.format(product.price)}원 ${product.quantity}개 ${product.promotion ?: ""}")
        }
        println()
    }

    fun printPurchasedProducts(
        purchasedProductsOfRegularPrice: List<PurchasedProductOfRegularPrice>,
        purchasedProductsOfPromotion: List<PurchasedProductOfPromotion>,
    ) {
        println("===========W 편의점=============")
        println("상품명\t\t수량\t금액")
        if (purchasedProductsOfPromotion.isEmpty()) {
            return printPurchasedProductsOfRegular(purchasedProductsOfRegularPrice)
        }
        purchasedProductsOfPromotion.forEach { product ->
            println("${product.name}\t\t${product.count}\t${THOUSAND_COMMA.format(product.count * product.price)}")
        }
        purchasedProductsOfRegularPrice.forEach { product ->
            println("${product.name}\t\t${product.count}\t${THOUSAND_COMMA.format(product.count * product.price)}")
        }
    }

    private fun printPurchasedProductsOfRegular(
        purchasedProductsOfRegularPrice: List<PurchasedProductOfRegularPrice>,
    ) {
        purchasedProductsOfRegularPrice.forEach { product ->
            println("${product.name}\t\t${product.count}\t${THOUSAND_COMMA.format(product.count * product.price)}")
        }
    }

    fun printGiveaways(purchasedProductsOfPromotion: List<PurchasedProductOfPromotion>) {
        println("===========증\t정=============")
        purchasedProductsOfPromotion.forEach { product ->
            if (product.countOfPromotion != 0) {
                println("${product.name}\t\t${product.countOfPromotion}")
            }
        }
    }

    fun printReceiptSummary(
        buyCount: Int,
        totalMoney: Int,
        promotionDiscount: Int,
        membershipDiscount: Int,
        moneyToPay: Int,
    ) {
        println("==============================")
        println("총구매액\t\t${buyCount}\t${THOUSAND_COMMA.format(totalMoney)}")
        println("행사할인\t\t\t-${THOUSAND_COMMA.format(promotionDiscount)}")
        println("멤버십할인\t\t\t-${THOUSAND_COMMA.format(membershipDiscount)}")
        println("내실돈\t\t\t${THOUSAND_COMMA.format(moneyToPay)}")
    }

    companion object {
        private val THOUSAND_COMMA = DecimalFormat("#,###")
    }
}
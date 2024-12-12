package store.view

import store.model.Product
import java.text.DecimalFormat

class OutputView {

    fun printWelcome(products: List<Product>) {
        println("안녕하세요. W편의점입니다.\n" + "현재 보유하고 있는 상품입니다.\n")
        products.forEach { product ->
            if (product.quantity == 0) {
                println("- ${product.name} ${THOUSAND_COMMA.format(product.price)} 재고 없음 ${product.promotion ?: ""}")
                return@forEach
            }
            println("- ${product.name} ${THOUSAND_COMMA.format(product.price)} ${product.quantity}개 ${product.promotion ?: ""}")
        }
    }

    companion object {
        private val THOUSAND_COMMA = DecimalFormat("#,###")
    }
}
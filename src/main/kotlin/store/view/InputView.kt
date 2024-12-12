package store.view

import camp.nextstep.edu.missionutils.Console.readLine
import store.model.ProductToBuy

class InputView {

    fun readProductsToBuy(): List<ProductToBuy> {
        println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])")
        val readingProductsToBuy = readLine().split(",")
        val productsToBuy = mutableListOf<String>()
        val products = mutableListOf<ProductToBuy>()
        readingProductsToBuy.forEach { productToBuy ->
            require(productToBuy.isNotBlank()) { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
            require(productToBuy.contains("-")) { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
            productsToBuy.add(productToBuy.removeSurrounding("[", "]"))
        }

        productsToBuy.forEach { productToBuy ->
            val product = productToBuy.split("-")
            product[1].toIntOrNull() ?: throw IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.")
            products.add(ProductToBuy(product[0], product[1].toInt()))
        }

        return products.toList()
    }
}
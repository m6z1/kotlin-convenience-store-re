package store.view

import camp.nextstep.edu.missionutils.Console.readLine
import store.model.ProductToBuy
import store.model.ResponseState

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

    fun readAddingProductForPromotion(productName: String): ResponseState {
        println("현재 ${productName}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)")
        val response = ResponseState.from(readLine().uppercase())
        return response
    }

    fun readSomeProductToPayRegularPrice(productName: String, productCountOfRegularPrice: Int): ResponseState {
        println("현재 $productName ${productCountOfRegularPrice}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)")
        val response = ResponseState.from(readLine().uppercase())
        return response
    }
}
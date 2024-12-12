package store.model

import java.time.LocalDate

class PromotionManager {
    private val fileReader = FileReader(PROMOTION_FILE_PATH)
    private val productManager = ProductManager()
    private val promotions = mutableListOf<Promotion>()

    init {
        val fileLines = fileReader.readFile().drop(1)
        fileLines.forEach {
            val promotionData = it.split(",")
            promotions.add(
                Promotion(
                    promotionData[0],
                    promotionData[1].toInt(),
                    promotionData[2].toInt(),
                    LocalDate.parse(promotionData[3]),
                    LocalDate.parse(promotionData[4]),
                )
            )
        }
    }

    fun checkProductPromotion(productToBuy: ProductToBuy): PromotionState {
        val promotionProduct = productManager.getPromotionProduct(productToBuy.name)
        val promotion = promotions.find { it.name == promotionProduct.promotion }!!
        if (isValidPromotionPeriod(promotionProduct.promotion!!).not()) return PromotionState.NOT_APPLICABLE
        if (promotionProduct.quantity < productToBuy.buyCount) return PromotionState.SOME_PRODUCT_OUT_OF_STOCK
        if (productToBuy.buyCount % (promotion.buy + promotion.get) == promotion.buy) {
            if (productToBuy.buyCount + promotion.get <= promotionProduct.quantity) {
                return PromotionState.MORE_PRODUCT_APPLICABLE
            }
            return PromotionState.SOME_PRODUCT_OUT_OF_STOCK
        }
        return PromotionState.APPLICABLE
    }

    private fun isValidPromotionPeriod(productPromotionName: String): Boolean {
        val promotion = promotions.find { it.name == productPromotionName }!!
        return LocalDate.now() in promotion.startDate..promotion.endDate
    }

    fun getGiveawaysCount(promotionName: String, buyCount: Int): Int {
        val promotion = promotions.find { it.name == promotionName }!!
        return buyCount / (promotion.buy + promotion.get)
    }

    fun getPromotion(promotionName: String): Promotion {
        return promotions.find { it.name == promotionName }!!
    }

    fun getProductCountOfRegularPrice(productToBuy: ProductToBuy): Int {
        val promotionProduct = productManager.getPromotionProduct(productToBuy.name)
        val promotion = promotions.find { it.name == promotionProduct.name }!!
        val promotionSetSize = promotion.buy + promotion.get
        val availableOfPromotionProductQuantity = promotionProduct.quantity / promotionSetSize
        return productToBuy.buyCount - (availableOfPromotionProductQuantity * promotionSetSize)
    }

    companion object {
        private const val PROMOTION_FILE_PATH = "src/main/resources/promotions.md"
    }
}
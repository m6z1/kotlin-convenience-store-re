package store.model

import java.time.LocalDate

class PromotionManager {
    private val fileReader = FileReader(PROMOTION_FILE_PATH)
   val promotions = mutableListOf<Promotion>()

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

    companion object {
        private const val PROMOTION_FILE_PATH = "src/main/resources/promotions.md"
    }
}
package store.model

enum class PromotionState(private val title: String) {
    NOT_APPLICABLE("적용 불가"),
    MORE_PRODUCT_APPLICABLE("추가로 가져오면 적용 가능"),
    SOME_PRODUCT_OUT_OF_STOCK("일부 수량 적용 불가, 정가 결제 해야 함"),
    APPLICABLE("적용 가능"),
}
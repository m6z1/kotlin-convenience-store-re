package store.model

enum class ResponseState(private val code: String) {
    POSITIVE("Y"), NEGATIVE("N");

    companion object {

        fun from(code: String): ResponseState {
            return ResponseState.entries.find { it.code == code }
                ?: throw IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.")
        }
    }
}
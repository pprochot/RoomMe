package uj.roomme.domain.auth

enum class ErrorCode(val id: Int) {
    EmailAlreadyInDB(0),
    WrongEmailOrPassword(1);

    companion object {
        fun fromCode(code: Int): ErrorCode? {
            return values().firstOrNull { it.id == code }
        }
    }
}
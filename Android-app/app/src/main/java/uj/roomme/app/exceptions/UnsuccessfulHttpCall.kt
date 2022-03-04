package uj.roomme.app.exceptions

import java.lang.RuntimeException

class UnsuccessfulHttpCall(code: Int, body: Any?) :
    RuntimeException("Http call finished with status code: '$code' and with body: '$body'") {
}
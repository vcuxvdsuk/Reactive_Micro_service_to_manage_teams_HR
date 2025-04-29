package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class NotFoundException : RuntimeException {
    constructor():super()
    constructor(message:String):super(message)
    constructor(cause:Throwable):super(cause)
    constructor(message:String, cause:Throwable):super(message, cause)
}
package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalExceptionHandler {

    // כל שגיאה כללית
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = ex.localizedMessage ?: "An unexpected error occurred."
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // טיפול בשגיאות שמוגדרות עם סטטוס
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = ex.statusCode.value(),
            message = ex.reason ?: "Something went wrong."
        )
        return ResponseEntity(errorResponse, ex.statusCode)
    }

    //  NotFound
    @ExceptionHandler(ChangeSetPersister.NotFoundException::class)
    fun handleNotFoundException(ex: ChangeSetPersister.NotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Resource not found."
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.allErrors.joinToString(", ") { it.defaultMessage ?: "Invalid input" }
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = errors
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

}

// מחלקה לייצג שגיאה
data class ErrorResponse(
    val status: Int,
    val message: String
)
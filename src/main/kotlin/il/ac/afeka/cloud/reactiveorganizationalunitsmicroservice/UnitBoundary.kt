package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.validation.annotation.Validated
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import jakarta.validation.constraints.NotBlank


@Validated
class UnitBoundary(
    var unitId: String?, //irrelevant, over-written in DB
    @field:NotBlank(message = "name must not be empty")
    var name: String?,
    var creationDate:LocalDateTime?,      //irrelevant, over-written in DB
    @field:NotBlank(message = "description must not be empty")
    var description: String?
) {

    constructor() : this(null, null, null, null)

    constructor(entity: UnitEntity) :
            this(entity.id, entity.name, entity.creationDate, entity.description)

    fun toEntity():UnitEntity{
        return UnitEntity(null,
            this.name,
            LocalDateTime.now(),
            this.description)
    }
    override fun toString(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = creationDate?.let { formatter.format(it) } ?: ""
        return """{"unitId":"$unitId", "name":"$name", "creationDate":"$formattedDate", "description":"$description"}"""
    }
}

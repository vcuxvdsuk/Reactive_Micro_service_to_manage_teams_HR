package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.format.DateTimeFormatter


@Validated
class UnitBoundary(
    var unitId: String?, //irrelevant, over-written in DB
    @field:NotNull(message = "string must not be null")
    @field:NotBlank(message = "name must not be empty")
    var name: String?,
    var creationDate:LocalDateTime?,      //irrelevant, over-written in DB
    @field:NotNull(message = "string must not be null")
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
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val formattedDate = creationDate?.let { it.format(formatter) } ?: ""
        return """{"unitId":"$unitId", "name":"$name", "creationDate":"$formattedDate", "description":"$description"}"""
    }

}

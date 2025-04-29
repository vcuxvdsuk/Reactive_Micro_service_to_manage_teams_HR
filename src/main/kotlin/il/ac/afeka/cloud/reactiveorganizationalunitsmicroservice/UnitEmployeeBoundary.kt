package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

@Validated
class UnitEmployeeBoundary(
    @field:NotNull(message = "string must not be null")
    @field:NotBlank(message = "string must not be blank")
    @field:Email(message = "email must be in email format")
    var email:String?
) {


}
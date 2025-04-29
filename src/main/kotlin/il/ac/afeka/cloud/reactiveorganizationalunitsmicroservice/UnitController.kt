package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/units"])
@Validated
class UnitController(
    val unitService:UnitService) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUnit(@RequestBody @Valid unitBoundary:UnitBoundary): Mono<UnitBoundary>{
        /*        POST /units         */
        return unitService.createUnit(unitBoundary)
    }

    @GetMapping(
        path = ["/{unitId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getById(@PathVariable("unitId") id:String
                ): Mono<UnitBoundary>{
        /*        GET /units/{unitId}         */
        return unitService.getUnitById(id)
    }

    @GetMapping(
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun getById(@RequestParam("page", defaultValue = "0") page:Int,
                @RequestParam("size", defaultValue = "10") size:Int
                ): Flux<UnitBoundary> {
        /*        GET /units?page={page}&size={size}         */
        if (page<0 || size<1){
            return Flux.error(InvalidInputException("page must be larger then -1 and size larger then 0"))
        }
        return unitService.getAllUnits(page,size)
    }

    @PutMapping(
        path = ["/{unitId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUnit(@RequestBody @Valid unitBoundary:UnitBoundary,
                   @PathVariable("unitId") id:String
    ): Mono<Void> {
        /*       PUT /units/{unitId}        */
        return unitService.updateUnit(id,unitBoundary)
    }

    @DeleteMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteAll(): Mono<Void> {
        /*       DELETE /units        */
        return unitService.deleteAll()
    }


    //bonus
    @PutMapping(
        path = ["/{unitId}/users"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addEmployeeToUnit(@RequestBody @Valid employee:UnitEmployeeBoundary,
                   @PathVariable("unitId") id:String
    ): Mono<Void> {
        /*       PUT /units/{unitId}/users       */
        return unitService.addEmployeeToUnit(id,employee)
    }


    @GetMapping(
        path = ["/{unitId}/users"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun getAllEmployeesOfUnit(
        @PathVariable("unitId") unitId:String,
        @RequestParam("page", defaultValue = "0") page:Int,
        @RequestParam("size", defaultValue = "10") size:Int
    ): Flux<UnitEmployeeBoundary> {
        /*        GET /units/{unitId}/users?page={page}&size={size}        */
        if (page<0 || size<1){
            return Flux.error(InvalidInputException("page must be larger then -1 and size larger then 0"))
        }
        return unitService.getAllEmployeesOfUnit(unitId,page,size)
    }

    @GetMapping(
        path = ["/{email}/units"],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    fun getAllUnitsOfEmployee(
        @PathVariable("email") email:String,
        @RequestParam("page", defaultValue = "0") page:Int,
        @RequestParam("size", defaultValue = "10") size:Int
    ): Flux<UnitBoundary> {
        /*        GET /units/{email}/units?page={page}&size={size}       */
        if (page<0 || size<1){
            return Flux.error(InvalidInputException("page must be larger then -1 and size larger then 0"))
        }
        return unitService.getAllUnitsOfEmployee(email,page,size)
    }


    @DeleteMapping(
        path = ["/{unitId}/users"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteAllEmployeesOfUnit(
        @PathVariable("unitId") unitId:String,
    ): Mono<Void> {
        /*       DELETE /units/{unitId}/users       */
        return unitService.deleteAllEmployeesOfUnit(unitId)
    }
}
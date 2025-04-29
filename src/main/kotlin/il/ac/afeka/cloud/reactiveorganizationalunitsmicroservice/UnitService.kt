package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UnitService {
    fun createUnit(unit:UnitBoundary):Mono<UnitBoundary>
    fun getUnitById(id:String):Mono<UnitBoundary>
    fun getAllUnits(page:Int,size:Int):Flux<UnitBoundary>
    fun updateUnit(id:String,unit:UnitBoundary):Mono<Void>
    fun deleteAll():Mono<Void>

    //bonus
    fun addEmployeeToUnit(id: String,employeeBoundary: UnitEmployeeBoundary):Mono<Void>
    fun getAllEmployeesOfUnit(id: String, page: Int, size: Int):Flux<UnitEmployeeBoundary>
    fun getAllUnitsOfEmployee(email: String, page: Int, size: Int):Flux<UnitBoundary>
    fun deleteAllEmployeesOfUnit(id: String):Mono<Void>
}
package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice


import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UnitCrud : ReactiveMongoRepository<UnitEntity, String> {
    fun saveUnit(unit: UnitEntity): Mono<UnitEntity>
    fun getUnitById(id:String): Mono<UnitEntity>
    fun findAllByIdNotNull(pageable: Pageable): Flux<UnitEntity>
    fun updateUnit(id:String,pageable: Pageable): Mono<UnitEntity>
    fun deleteAllUnits(): Mono<Void>
}

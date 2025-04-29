package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice


import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface UnitCrud : ReactiveMongoRepository<UnitEntity, String> {
    fun findAllByIdNotNull(pageable: Pageable): Flux<UnitEntity>
}

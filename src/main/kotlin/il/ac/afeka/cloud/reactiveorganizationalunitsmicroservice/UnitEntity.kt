package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "units")
class UnitEntity(
    @Id var id:String?,
    var name:String?,
    var creationDate: LocalDateTime?,
    var description:String?
) {

    constructor():this(null,null,null,null)

    fun toBoundary():UnitBoundary{
        return UnitBoundary(this)
    }
    override fun toString(): String{
        return ""
    }

}
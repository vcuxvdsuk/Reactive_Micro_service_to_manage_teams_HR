package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import kotlinx.coroutines.reactor.mono
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.NotFound
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.management.openmbean.InvalidKeyException

@Service
class UnitServiceImp (
    val unitCrud: UnitCrud
):UnitService{
    override fun createUnit(unit: UnitBoundary): Mono<UnitBoundary> {
        /*

        POST /units

        פעולה שמוסיפה לשירות בצורה ריאקטיבית יחידה ארגונית חדשה.
        פעולה זו מקבלת כקלט JSON של UnitBoundary, שהמבנה שלו מפורט בהמשך.
        על השירות שלך להגדיר מזהה ייחודי ליחידה החדשה. במידה ומפעילי השירות מעבירים בפרטי היחידה הארגונית שנשלחים לשירות מזהה, השירות שלך ידרוס את המזהה.
        הפעולה תכשל אם חסרים בקלט שנשלח אליה נתונים חשובים, שעליך להבין מהם בעצמך.
        השירות יקבע את תאריך היצירה של היחידה הארגונית ויאחסן אותה בבסיס נתונים.

         */
        return Mono.just(unit.toEntity())
            .flatMap { if(!it.name.isNullOrBlank()){
                            unitCrud.saveUnit(it).map { it.toBoundary() }
                        }else{
                            Mono.error(InvalidKeyException("name cant be empty"))
                        }
            }
    }

    override fun getUnitById(id: String): Mono<UnitBoundary> {
        /*

        GET /units/{unitId}
        פעולה ששולפת בצורה ריאקטיבית יחידה ארגונית ספציפית מהשירות.
        במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר כפרמטר לפעולה, השירות יחזיר Mono ריק
         */
        return unitCrud
            .getUnitById(id)
            .flatMap { if (it != null){
                    Mono.just(it.toBoundary())
                }else{
                    Mono.error(NotFoundException())
                }
            }
    }

    override fun getAllUnits(page: Int, size: Int): Flux<UnitBoundary> {
        /*

        GET /units?page={page}&size={size}

        פעולה ששולפת בצורה ריאקטיבית יחידות ארגוניות מהשירות, עם Pagination.
        עליך לקבוע במימוש שלך מיון דטרמיניסטי ליחידות הארגוניות שמוחזרות מהשירות.
        במידה ולא קיימות כלל יחידות ארגוניות בשירות, או שחרגנו מגבולות הנתונים בבסיס הנתונים עם הפרמטרים של ה-Pagination, השירות יחזיר Flux ריק
        לתשומת לבך - עליך להגדיר את הערך שמוחזר מהשירות בפעולה זו, כך שיתאים ל-MIME TYPE שהודגם בכתה

         */

        return unitCrud
            .findAllByIdNotNull(PageRequest.of(page, size, Sort.Direction.ASC, "id"))
            .map { it.toBoundary() }
            .log()
    }

    override fun updateUnit(id: String,unit:UnitBoundary): Mono<UnitBoundary> {
        /*

        PUT /units/{unitId}

        פעולה שמאפשרת לעדכן את פרטי יחידה ארגונית מסוימת בצורה ריאקטיבית.
        בנוסף למזהה של היחידה הארגונית שיש לעדכן, שמועבר ב-URL, פעולה זו מקבלת גם JSON עם פרטי היחידה הארגונית המעודכנים
        הפעולה מאפשרת לעדכן את שם היחידה הארגונית, ואת התיאור שלה.
        לעומת זאת, במידה ומפעילי השירות מנסים לעדכן את המזהה או תאריך היצירה של היחידה הארגונית, הפעולה תתעלם מנתונים אלה
        במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר ב-URL של הפעולה, הפעולה תכשל.
        פעולה זו מחזירה Mono ריק

         */
        return unitCrud.getUnitById(id)
            .flatMap {
                if (it != null) {
                    unitCrud
                        .save(it.toBoundary().toEntity())
                        .map { it.toBoundary() }
                } else {
                    Mono.error(NotFoundException())
                }
            }
    }

    override fun deleteAll(): Mono<Void> {
        /*
        DELETE /units

        פעולה שמוחקת בצורה ריאקטיבית את כל הנתונים בשירות ומחזירה Mono ריק
        במידה ומימשת את סעיפי הבונוס, פעולה זו תמחק גם את כל הנתונים שקשורים למימוש הבונוס.
        פעולה זו, תשמש אותך לבדיקת השירות שלך.

         */
        return unitCrud
            .deleteAllUnits()
            .then()
    }
}


package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.management.openmbean.InvalidKeyException

@Service
class UnitServiceImp (
    val unitCrud: UnitCrud
):UnitService{
    override fun createUnit(unit: UnitBoundary): Mono<UnitBoundary> {
        /*
        פעולה שמוסיפה לשירות בצורה ריאקטיבית יחידה ארגונית חדשה.
        פעולה זו מקבלת כקלט JSON של UnitBoundary, שהמבנה שלו מפורט בהמשך.
        על השירות שלך להגדיר מזהה ייחודי ליחידה החדשה. במידה ומפעילי השירות מעבירים בפרטי היחידה הארגונית שנשלחים לשירות מזהה, השירות שלך ידרוס את המזהה.
        הפעולה תכשל אם חסרים בקלט שנשלח אליה נתונים חשובים, שעליך להבין מהם בעצמך.
        השירות יקבע את תאריך היצירה של היחידה הארגונית ויאחסן אותה בבסיס נתונים.
         */
        return Mono.just(unit.toEntity())
            .flatMap { if(!it.name.isNullOrBlank()){
                            unitCrud.save(it).map { it.toBoundary() }
                        }else{
                            Mono.error(InvalidKeyException("name cant be empty"))
                        }
            }
    }

    override fun getUnitById(id: String): Mono<UnitBoundary> {
        /*
        פעולה ששולפת בצורה ריאקטיבית יחידה ארגונית ספציפית מהשירות.
        במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר כפרמטר לפעולה, השירות יחזיר Mono ריק
         */
        return unitCrud
            .findById(id)
            .flatMap { if (it != null){
                    Mono.just(it.toBoundary())
                }else{
                    Mono.error(NotFoundException())
                }
            }
    }

    override fun getAllUnits(page: Int, size: Int): Flux<UnitBoundary> {
        /*
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

    override fun updateUnit(id: String,unit:UnitBoundary): Mono<Void> {
        /*
        פעולה שמאפשרת לעדכן את פרטי יחידה ארגונית מסוימת בצורה ריאקטיבית.
        בנוסף למזהה של היחידה הארגונית שיש לעדכן, שמועבר ב-URL, פעולה זו מקבלת גם JSON עם פרטי היחידה הארגונית המעודכנים
        הפעולה מאפשרת לעדכן את שם היחידה הארגונית, ואת התיאור שלה.
        לעומת זאת, במידה ומפעילי השירות מנסים לעדכן את המזהה או תאריך היצירה של היחידה הארגונית, הפעולה תתעלם מנתונים אלה
        במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר ב-URL של הפעולה, הפעולה תכשל.
        פעולה זו מחזירה Mono ריק
         */
        return unitCrud.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("no unit with given ID")))
            .flatMap {
                it.name = unit.name
                it.description = unit.description
                unitCrud.save(it)
            }
            .then()
    }

    override fun deleteAll(): Mono<Void> {
        /*
        DELETE /units

        פעולה שמוחקת בצורה ריאקטיבית את כל הנתונים בשירות ומחזירה Mono ריק
        במידה ומימשת את סעיפי הבונוס, פעולה זו תמחק גם את כל הנתונים שקשורים למימוש הבונוס.
        פעולה זו, תשמש אותך לבדיקת השירות שלך.

         */
        return unitCrud
            .deleteAll()
            .then()
    }


    //bonus
    override fun addEmployeeToUnit(id: String, employeeBoundary: UnitEmployeeBoundary):Mono<Void>{
        /*

פעולה שמשייכת עובדת ליחידה ארגונית בצורה ריאקטיבית.
בנוסף למזהה של היחידה הארגונית, שמועבר ב-URL, פעולה זו מקבלת גם JSON במבנה של UnitEmployeeBoundary, שמפורט בהמשך, וכולל רק כתובת דואל. אם כבר הגדירו את כתובת הדואל ברשימת העובדות והעובדים של היחידה הארגונית, פעולה זו לא תשנה דבר בשירות.
במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר כפרמטר לפעולה, השירות לא יעשה דבר ולא יחזיר שגיאה.
בכל מקרה, פעולה זו תחזיר Mono ריק
         */
        return unitCrud
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("no unit with given ID")))
            .flatMap { entity ->
                if (entity.moreAttributes == null) {
                    entity.moreAttributes = mutableSetOf()
                }
                entity.moreAttributes!!.add(employeeBoundary.email.toString())
                unitCrud.save(entity)
            }
            .then()
            .log()
    }

    override fun getAllEmployeesOfUnit(id: String, page: Int, size: Int):Flux<UnitEmployeeBoundary>{
        /*
            פעולה שמאפשרת לשלוף בצורה ריאקטיבית, את פרטי העובדות והעובדים שמשויכים ליחידה הארגונית, עם Pagination.
            על השירות שלך למיין את פרטי העובדות והעובדים שהוא שולף לפי כתובת הדואל, שהיא המאפיין היחיד שמוחזר ב-JSON שמתאר כל אחת ואחד מהעובדים, במבנה של UnitEmployeeBoundary, שמפורט בהמשך.
            במידה ולא משויכים כלל עובדות או עובדים ליחידה הארגונית, או במידה וחרגנו מגבולות הנתונים בבסיס הנתונים עם הפרמטרים של ה-Pagination, השירות יחזיר Flux ריק
            לתשומת לבך על השירות שלך להגדיר את הערך שמוחזר מהשירות בפעולה זו, כך שיתאים ל-MIME TYPE שהודגם בכתה
         */
        return unitCrud.findById(id).flatMapMany { employee ->
            val allEmployees = employee.moreAttributes
                ?.map { UnitEmployeeBoundary(it) }
                ?.sortedBy { it.email }
                ?.drop(page*size)
                ?.take(size)
                ?: emptyList()

            Flux.fromIterable(allEmployees)
        }
    }

    override fun getAllUnitsOfEmployee(email: String, page: Int, size: Int):Flux<UnitBoundary>{
        /*
        פעולה שמאפשרת לשלוף בצורה ריאקטיבית, את פרטי היחידות הארגוניות, שעובדת או עובד מסוימים משויכים אליהן, עם Pagination.
        על השירות שלך למין את פרטי היחידות הארגוניות שמוחזרות בפעולה זו, לפי המזהה - unitId - של היחידה הארגונית, ולהחזיר Flux של עצמים מסוג UnitBoundary.
        במידה והעובדת לא משוייכת לאף יחידה ארגונית, או שחרגנו מגבולות הנתונים בבסיס הנתונים עם הפרמטרים של ה-Pagination, השירות יחזיר Flux ריק
        לתשומת לבך עליך להגדיר את הערך שמוחזר מהשירות בפעולה זו, כך שיתאים ל-MIME TYPE שהודגם בכתה
         */
        return unitCrud.findAll()
            .filter { it.moreAttributes.orEmpty().contains(email) }
            .map { it.toBoundary() }
            .skip((page * size).toLong())
            .take(size.toLong())
    }

    override fun deleteAllEmployeesOfUnit(id: String):Mono<Void>{
        /*

פעולה שמוחקת בצורה ריאקטיבית את השיוך של כל העובדות והעובדים ששויכו ליחידה ארגונית מסוימת.
פעולה זו תחזיר Mono ריק
         */
        return unitCrud
            .findById(id)
            .flatMap<Void?> {
                it.moreAttributes = mutableSetOf()
                unitCrud.save(it)
                    .then()
            }
    }
}




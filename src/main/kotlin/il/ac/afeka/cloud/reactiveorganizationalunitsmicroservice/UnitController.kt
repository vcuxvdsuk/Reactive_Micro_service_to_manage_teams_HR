package il.ac.afeka.cloud.reactiveorganizationalunitsmicroservice

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/units"])
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
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getById(@RequestParam("page") page:Int,
                @RequestParam("size") size:Int
                ): Flux<UnitBoundary> {
        /*        GET /units?page={page}&size={size}         */
        return unitService.getAllUnits(page,size)
    }

    @PutMapping(
        path = ["/{unitId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUnit(@RequestBody @Valid unitBoundary:UnitBoundary,
                   @PathVariable("unitId") id:String
    ): Mono<UnitBoundary> {
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



/*

סעיפי בונוס לניהול עובדות ועובדים ביחידות הארגוניות
במידה והשירות שלך יממש את כל פעולות הבונוס בהצלחה, ינתן לך בונוס על המימוש:


PUT /units/{unitId}/users

פעולה שמשייכת עובדת ליחידה ארגונית בצורה ריאקטיבית.
בנוסף למזהה של היחידה הארגונית, שמועבר ב-URL, פעולה זו מקבלת גם JSON במבנה של UnitEmployeeBoundary, שמפורט בהמשך, וכולל רק כתובת דואל. אם כבר הגדירו את כתובת הדואל ברשימת העובדות והעובדים של היחידה הארגונית, פעולה זו לא תשנה דבר בשירות.
במידה ולא קיימת יחידה ארגונית עם מזהה שהוגדר כפרמטר לפעולה, השירות לא יעשה דבר ולא יחזיר שגיאה.
בכל מקרה, פעולה זו תחזיר Mono ריק


GET /units/{unitId}/users?page={page}&size={size}

פעולה שמאפשרת לשלוף בצורה ריאקטיבית, את פרטי העובדות והעובדים שמשויכים ליחידה הארגונית, עם Pagination.
על השירות שלך למיין את פרטי העובדות והעובדים שהוא שולף לפי כתובת הדואל, שהיא המאפיין היחיד שמוחזר ב-JSON שמתאר כל אחת ואחד מהעובדים, במבנה של UnitEmployeeBoundary, שמפורט בהמשך.
במידה ולא משויכים כלל עובדות או עובדים ליחידה הארגונית, או במידה וחרגנו מגבולות הנתונים בבסיס הנתונים עם הפרמטרים של ה-Pagination, השירות יחזיר Flux ריק
לתשומת לבך על השירות שלך להגדיר את הערך שמוחזר מהשירות בפעולה זו, כך שיתאים ל-MIME TYPE שהודגם בכתה


GET /units/Baruch.Ori@s.afeka.ac.il/units?page={page}&size={size}

פעולה שמאפשרת לשלוף בצורה ריאקטיבית, את פרטי היחידות הארגוניות, שעובדת או עובד מסוימים משויכים אליהן, עם Pagination.
על השירות שלך למין את פרטי היחידות הארגוניות שמוחזרות בפעולה זו, לפי המזהה - unitId - של היחידה הארגונית, ולהחזיר Flux של עצמים מסוג UnitBoundary.
במידה והעובדת לא משוייכת לאף יחידה ארגונית, או שחרגנו מגבולות הנתונים בבסיס הנתונים עם הפרמטרים של ה-Pagination, השירות יחזיר Flux ריק
לתשומת לבך עליך להגדיר את הערך שמוחזר מהשירות בפעולה זו, כך שיתאים ל-MIME TYPE שהודגם בכתה


DELETE /units/{unitId}/users

פעולה שמוחקת בצורה ריאקטיבית את השיוך של כל העובדות והעובדים ששויכו ליחידה ארגונית מסוימת.
פעולה זו תחזיר Mono ריק


 */


}
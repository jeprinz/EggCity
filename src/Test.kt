import java.awt.Color

fun main(args: Array<String>){
    val polyG = PolygonGraph<Structure>(ColoredThing(Color.YELLOW, makeRect(Point(0.0,0.0), 100.0, 200.0)))

    val numbs = arrayListOf<Int>(1,2,3,4,5,6,7,8,9)
    val groups = floodfill<Int>(numbs, {a,b -> a-b==2 || b-a==2})
    println(groups)
}

fun putRectInSpace(blank: Blank, polyG: PolygonGraph<Structure>, vertical: Boolean): Boolean{
    val centoid: Point = blank.poly.centroid()
    val width: Double = blank.poly.width()
    val height: Double = blank.poly.height()
    if (width > 50 && height > 50){
        val rect: Polygon
        if (vertical){
            rect = makeRect(centoid, width*.1, 10000.0)
        } else{
            rect = makeRect(centoid, 10000.0, height*.1)
        }
        return false
    } else {
        return false
    }
}

class ColoredThing(colr: Color, poly: Polygon): Structure(poly){
    val color = colr
}

fun makeRect(center: Point, width: Double, height: Double) : Polygon{
    val cx = center.x
    val cy = center.y

    val upLeft = Point(cx - width/2, cy + height/2)
    val upRight = Point(cx + width/2, cy + height/2)
    val downLeft = Point(cx - width/2, cy - height/2)
    val downRight = Point(cx + width/2, cy - height/2)

    return polyFromPoints(arrayListOf(upLeft, upRight, downLeft, downRight))

}
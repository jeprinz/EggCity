import java.awt.Color

fun main(args: Array<String>){
    val polyG = PolygonGraph<Structure>(ColoredThing(Color.YELLOW, makeRect(Point(0.0,0.0), 100.0, 200.0)))

    putRectInSpace(polyG.getNodes().first(), polyG, true)


    println(polyG)
}

fun putRectInSpace(blank : Graph.Node<Structure, PolygonGraph.Edge>, polyG: PolygonGraph<Structure>, vertical: Boolean): Boolean{
    val centoid: Point = blank.node.poly.centroid()
    val width: Double = blank.node.poly.width()
    val height: Double = blank.node.poly.height()
    if (width > 50 && height > 50){
        val rect: Polygon
        if (vertical){
            rect = makeRect(centoid, width*.1, 10000.0)
        } else{
            rect = makeRect(centoid, 10000.0, height*.1)
        }

        val structure = ColoredThing(Color.WHITE, rect)

        polyG.splitPolygon(blank,  rect, structure)
        return true
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
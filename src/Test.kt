import java.awt.Color

fun main(args: Array<String>){
    val initalRect = makeRect(Point(0.0,0.0), 100.0, 200.0)
    val plane = ColoredThing(Color.YELLOW)
    val polyG = PolygonGraph<Structure>(plane)
    polyG.splitInnerPolygon(polyG.getNodes().first(), Blank(), initalRect)

    val nodes = polyG.getNodes()
    print(nodes)


    putRectInSpace(ArrayList(polyG.getNodes())[1], polyG, true)


    println(polyG.getNodes().size)
}


fun putRectInSpace(blank : PolygonGraph<Structure>.NodeId, polyG: PolygonGraph<Structure>, vertical: Boolean): Boolean{
    val poly = polyG.getPolygon(blank)
    val centoid: Point = poly.centroid()
    val width: Double = poly.width()
    val height: Double = poly.height()
    if (width > 50 && height > 50){
        val rect: Polygon
        if (vertical){
            rect = makeRect(centoid, width*.1, 10000.0)
        } else{
            rect = makeRect(centoid, 10000.0, height*.1)
        }

        val structure = ColoredThing(Color.WHITE)

        polyG.placePolygon(blank,  rect, structure)
        return true
    } else {
        return false
    }
}

class ColoredThing(colr: Color): Structure{
    val color = colr
}

fun makeRect(center: Point, width: Double, height: Double) : Polygon{
    val cx = center.x
    val cy = center.y

    val upLeft = Point(cx - width/2, cy + height/2)
    val upRight = Point(cx + width/2, cy + height/2)
    val downLeft = Point(cx - width/2, cy - height/2)
    val downRight = Point(cx + width/2, cy - height/2)

    return polyFromPoints(arrayListOf(upLeft, upRight, downRight, downLeft))

}
import java.awt.Color

fun main(args: Array<String>) {
    val initialRect = makeRect(Point(0.0,0.0), 100.0, 200.0)
    val plane = ColoredThing(Color.YELLOW)
    val polyG = PolygonGraph<Structure>(plane)
    polyG.splitInnerPolygon(polyG.getNodes().first(), Blank(), initialRect)

    val coRect = makeRect(Point(0.0,0.0), 50.0, 210.0)
    polyG.placePolygon(ArrayList(polyG.getNodes())[1], coRect, ColoredThing(Color.WHITE))
    println(polyG.getNodes().size)
    println(ArrayList(polyG.getNodes())[0].getPolygon())
    println(ArrayList(polyG.getNodes())[1].getPolygon())
    println(ArrayList(polyG.getNodes())[2].getPolygon())
    println(ArrayList(polyG.getNodes())[3].getPolygon())

//    val p1 = Point(0.0,0.0)
//    val p2 = Point(10.0,-10.0)
//    val p3 = Point(100.0,-100.0)
//
//    println("1: ${initialRect.inside(p1)} 2: ${initialRect.inside(p2)} 3: ${initialRect.inside(p3)}")

}
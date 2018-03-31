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
}
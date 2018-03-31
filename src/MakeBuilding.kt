import java.util.*

fun buildPlot(blank: PolygonGraph<Structure>.NodeId, polyG: PolygonGraph<Structure>): Pair<Polygon, Plot>{
    val blankPoly = blank.getPolygon()
    val surrounding = blank.getAdjacentNodes()
    val roads : HashMap<Road, Collection<Segment>> = hashMapOf()
    for (thing in surrounding){
        val data = thing.getData()
        if (data is Road){
            roads.put(data, blank.getSharedEdges(thing))
        }
    }

    val onePoint: Point
    val twoPoint: Point

    if(roads.isNotEmpty()){
        val list = ArrayList(roads.values)
        Collections.shuffle(list)
        val border = ArrayList(list.first())
        Collections.shuffle(border)
        onePoint = border.first().p1
        twoPoint = border.last().p2
    } else {
        val list = ArrayList(surrounding)
        onePoint = list.first().getPolygon().segs.first().p1
        twoPoint = list.last().getPolygon().segs.last().p2
    }

    val blankWidth = blankPoly.width()
    val blankHeight = blankPoly.height()

    val length = (blankWidth + blankHeight) / 2

    val perpenX = -(onePoint.x - twoPoint.x)
    val perpenY = (onePoint.y - twoPoint.y)

    val p1a = Point(onePoint.x + perpenX, onePoint.y + perpenY)
    val p1b = Point(onePoint.x - perpenX, onePoint.y - perpenY)
    val p2a = Point(twoPoint.x + perpenX, twoPoint.y + perpenY)
    val p2b = Point(twoPoint.x - perpenX, twoPoint.y - perpenY)

    val finalPoly = polyFromPoints(arrayListOf(p1a, p2a, p2b, p1b))

    return Pair(finalPoly, Plot())
}

class Plot : Structure{

}

fun totalLength(segs: Collection<Segment>): Double{
    var total = 0.0
    for (seg in segs){
        total += seg.length()
    }
    return total
}
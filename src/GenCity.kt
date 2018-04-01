fun genCity(size: Double) : PolygonGraph<Structure>{
    val polyG = PolygonGraph<Structure>(Rtwo())

    val city = Blank()
    val cityOutline = makecity(size, .5, 10, 5, 2.0)

    val rTwo = polyG.getNodes().first()

    val blank1 = polyG.splitInnerPolygon(rTwo, city, cityOutline.shape)


//    //Try drawing one road
//    val road = makeRoad(blank1.getPolygon(), 1.0)
//    print("road: ${road.shape}")
//
//    polyG.placePolygon(blank1, road.shape, road)
//
//
//    return polyG

    for (i in 0..15){
        val blank = getBlank(polyG)
        if (blank != null){
            doBlank(polyG, size, blank)
        }
    }

    return polyG

}

fun getBlank(polyG: PolygonGraph<Structure>): PolygonGraph<Structure>.NodeId?{
    val nodes = polyG.getNodes()
    val blanks = nodes.filter {nodeId -> nodeId.getData() is Blank}
    if (blanks.isEmpty()){
        return null
    } else {
        return blanks.first()
    }

}

fun doBlank(polyG: PolygonGraph<Structure>, size: Double, blank: PolygonGraph<Structure>.NodeId){
    val blankPoly = blank.getPolygon()
    val area = blankPoly.width() * blankPoly.height()

    val proportion = area / (size * size)

    if (proportion > 0.2 || true){
        val road = makeRoad(blankPoly, 30.0)
        polyG.placePolygon(blank, road.shape, road)
    } else {
        val plot = buildPlot(blank, polyG)
        polyG.placePolygon(blank, plot.first, plot.second)
    }
    println("nope")
}

fun main(args: Array<String>){
    val city = genCity(1000.0)
    city.getNodes().forEach{
        node ->
        println("heres a node for ya")
    }
}

class Rtwo : Structure
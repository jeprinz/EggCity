fun genCity(size: Double) : PolygonGraph<Structure>{
    val polyG = PolygonGraph<Structure>(Rtwo())

    val city = Blank()
    val cityOutline = makecity(size, .5, 4, 5, 2.0)

    val rTwo = polyG.getNodes().first()

    val blank1 = polyG.splitInnerPolygon(rTwo, city, cityOutline.shape)


    //Try drawing one road
    val road = makeRoad(blank1.getPolygon(), 1.0)
    print("road: ${road.shape}")

    polyG.placePolygon(blank1, road.shape, road)


    return polyG


}

fun main(args: Array<String>){
    val city = genCity(100.0)
    city.getNodes().forEach{
        node ->
        println("heres a node for ya")
        println(node.getPolygon())
        println(node.getPolygon().getOrderedPoints())
    }
}

class Rtwo : Structure
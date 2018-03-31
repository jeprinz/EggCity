fun genCity(size: Double) : PolygonGraph<Structure>{
    val polyG = PolygonGraph<Structure>(Rtwo())

    val city = Blank()
    val cityOutline = makecity(size, .5, 100, 5, 2.0)

    val rTwo = polyG.getNodes().first()

    val blank1 = polyG.splitInnerPolygon(rTwo, city, cityOutline.shape)


    //Try drawing one road
    val road = makeRoad(blank1.getPolygon(), 20.0)
            
    polyG.placePolygon(blank1, road.shape, road)


    return polyG


}

class Rtwo : Structure
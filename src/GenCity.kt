fun genCity(size: Double) : PolygonGraph<Structure>{
    val polyG = PolygonGraph<Structure>(Rtwo())

    val city = Blank()
    val cityOutline = makecity(size, .5, 100, 5, 2.0)

    val rTwo = polyG.getNodes().first()

    polyG.splitInnerPolygon(rTwo, city, cityOutline.shape)

    return polyG


}

class Rtwo : Structure
fun genCity(size: Double){
    val polyG = PolygonGraph<Structure>(Rtwo())

    val city = Blank()
    val cityOutline = makecity(size, .5, 100, 5, 2.0)

    val rTwo = polyG.getNodes().first()

    polyG.splitInnerPolygon(rTwo, city, cityOutline.shape)




}

class Rtwo : Structure
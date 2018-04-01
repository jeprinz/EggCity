class Park(shap: Polygon) : Structure {
    val shape = shap

}

fun makePark(poly: Polygon): Park {
    return Park(poly)
}
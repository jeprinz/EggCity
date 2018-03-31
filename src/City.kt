import kotlin.math.cos

class City(shap: Polygon) {
    val shape: Polygon = shap

}

/**
 * rad is expected radius of the city
 * varr is a number between 0 and .5 which determines how much the city's width varries
 * precpon is how many points define the city
 * precrand is how many trig functions we use
 */
fun makecity(rad: Double, varr: Double, precpon: Int, precrand: Int,cof:Double): City {
    var arl: ArrayList<Point> = ArrayList()
    var arr: ArrayList<Array<Double>> = ArrayList()
    for (i in 0..precrand) {
        arr.add( arrayOf(Math.pow(2.0, i.toDouble()), Math.random()*(precrand-i)/precrand ,Math.random()*6.284))
    }
    for (i in 0..precpon) {
        var thet: Double = 6.283 / precpon.toDouble() * i.toDouble()
        var x: Double = 0.0
        var y: Double = 0.0
        var r :Double =0.0
        for (j in 0..precrand) {
            r += arr.get(j).get(1)*rad/precrand*((Math.cos((arr.get(j).get(2)+1.0*arr.get(j).get(0)*thet))+Math.sin(arr.get(j).get(2)+1.0*arr.get(j).get(0)*thet )))

        }
        x=Math.pow(1.5*(.5*varr*r+rad*(1.0-varr))/rad,cof)*rad*Math.cos(thet)
        y=Math.pow(1.5*(.5*varr*r+rad*(1.0-varr))/rad,cof)*rad*Math.sin(thet)
        arl.add(Point(x, y))
    }
    return City(polyFromPoints(arl))
}
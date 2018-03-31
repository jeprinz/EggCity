class City(shap: Polygon) {
    val shape: Polygon = shap

}

fun makecity(rad: Double, varr: Double, precpon: Int, precrand:Int): City {
    var arl:ArrayList<Point> = ArrayList()
   var arr:Array<Array<Double>> = arrayOf()
    arr.set(0,arrayOf(1.0 ,rad*(1.0 -varr) ,rad*(1.0 - varr)))
    for (i in 1..precrand){
        arr.set(i,arrayOf(Math.pow(2.0 , i.toDouble()),rad*Math.random()*2.0*varr/precrand.toDouble(),rad*Math.random()*2.0*varr/precrand.toDouble() ))
    }
    for(i in 0..precpon){
        var thet:Double = 6.283/precpon.toDouble()*i.toDouble()
        arl.add(Point(arr.get(i).get(1)*Math.cos(arr.get(i).get(0)*thet), arr.get(i).get(2)*Math.sin(arr.get(i).get(0)*thet)))
    }
    return City(Polygon(arl))
}
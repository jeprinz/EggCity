class Road(shap: Polygon, len: Double, slop: Double,p1:Point,p2:Point) : Structure {
    val shape: Polygon = shap
    val length = len
    val slope = slop
    val pm1 =p1
    val pm2 =p2
}

fun poiToRoad(pm1: Point, pm2: Point, siz: Double): ArrayList<Segment> {
    val slo = -1.0 / getSlop(pm1, pm2)
    val yd: Double = siz * Math.sin(Math.atan(slo))
    val xd: Double = siz * Math.cos(Math.atan(slo))
    val lis: ArrayList<Segment> = ArrayList()
    lis.add(Segment(Point(pm1.x + xd, pm1.y + yd), Point(pm1.x - xd, pm1.y - yd)))
    lis.add(Segment(Point(pm1.x - xd, pm1.y - yd), Point(pm2.x - xd, pm2.y - yd)))
    lis.add(Segment(Point(pm2.x - xd, pm2.y - yd), Point(pm2.x + xd, pm2.y + yd)))
    lis.add(Segment(Point(pm2.x + xd, pm2.y + yd), Point(pm1.x + xd, pm1.y + yd)))
    return lis
}

fun makeRoad(pla: Polygon, siz: Double): Road {
    val segg: ArrayList<Segment> = pla.segs
    var i1 = 0
    var i2 = 0
    while (i1 == i2) {
        i1 = (Math.random() * segg.size).toInt()
        i2 = (Math.random() * segg.size).toInt()
    }
    val s1: Segment = segg.get(i1)
    val s2: Segment = segg.get(i2)
    val p1: Point = getPoint(s1)
    val p2: Point = getPoint(s2)
    val slope: Double = getSlop(p1, p2)
    val pm1 = Point(p1.x - siz * Math.cos(Math.atan(slope)), p1.y - siz * Math.sin(Math.atan(slope)))
    val pm2 = Point(p2.x + siz * Math.cos(Math.atan(slope)), p2.y + siz * Math.sin(Math.atan(slope)))

    val lis: ArrayList<Segment> = poiToRoad(pm1, pm2, siz)

    return Road(Polygon(lis), getDist(pm1, pm2), getSlop(pm1, pm2),pm1,pm2)


}

fun manyRoads(pla: Polygon, siz: Double, num: Int,md:Double): ArrayList<Road> {
    val lis: ArrayList<Road> = ArrayList()
    val plis: ArrayList<Point> = ArrayList()
    var i:Int =0
    while(i<num) {
        val r: Road = makeRoad(pla, siz)
        if(disCheck(plis,r,md)){
            lis.add(r)
            plis.add(r.pm1)
            plis.add(r.pm2)
            i++
        }
    }
    return lis
}

fun disCheck(plis:ArrayList<Point>,r:Road,md:Double):Boolean {
    for (i in 0..(plis.size-1)){
        if(getDist(plis.get(i),r.pm1)<md||getDist(plis.get(i),r.pm2)<md){
            return false
        }
    }
    return true
}

fun getSlop(p1: Point, p2: Point): Double {
    return (p2.y - p1.y) / (p2.x - p1.x)
}

fun getDist(p1: Point, p2: Point): Double {
    return Math.pow((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y), .5)
}

fun getPoint(seg: Segment): Point {
    val loc: Double = Math.random()
    return Point(seg.p1.x + loc * (seg.p2.x - seg.p1.x), seg.p1.y + loc * (seg.p2.y - seg.p1.y))
}

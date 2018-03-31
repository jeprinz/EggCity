class Road(shap: Polygon, len: Double, slop: Double) : Structure {
    val shape: Polygon = shap
    val length = len
    val slope = slop
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
    var i1: Int = 0
    var i2: Int = 0
    while (i1 == i2) {
        i1 = (Math.random() * segg.size).toInt()
        i2 = (Math.random() * segg.size).toInt()
    }
    val s1: Segment = segg.get(i1)
    val s2: Segment = segg.get(i2)
    val p1: Point = getPoint(s1);
    val p2: Point = getPoint(s2)
    val slope: Double = getSlop(p1, p2)
    val pm1: Point = Point(p1.x - 1000 * siz * Math.cos(Math.atan(slope)), p1.y - 1000 * siz * Math.sin(Math.atan(slope)))
    val pm2: Point = Point(p2.x + 1000 * siz * Math.cos(Math.atan(slope)), p2.y + 1000 * siz * Math.sin(Math.atan(slope)))

    val lis: ArrayList<Segment> = poiToRoad(pm1, pm2, siz)

    return Road(Polygon(lis), getDist(pm1, pm2), getSlop(pm1, pm2))


}

fun manyRoads(pla: Polygon, siz: Double, num: Int): ArrayList<Road> {
    var lis: ArrayList<Road> = ArrayList()
    for (i in 0..num) {
        lis.add(makeRoad(pla, siz))
    }
    return lis
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

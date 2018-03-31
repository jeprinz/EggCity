class Road(shap: Polygon) {
    val shape: Polygon = shap
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
    val pm1: Point = getPoint(s1);
    val pm2: Point = getPoint(s2)
    val slope: Double = getSlop(pm1, pm2)
    val pm1x: Double = pm1.x - siz * Math.cos(Math.atan(slope))
    val pm1y: Double = pm1.y - siz * Math.sin(Math.atan(slope))
    val pm2x: Double = pm2.x + siz * Math.cos(Math.atan(slope))
    val pm2y: Double = pm2.y + siz * Math.sin(Math.atan(slope))

    val slo = -1.0 / getSlop(pm1, pm2)
    val yd: Double = siz * Math.sin(Math.atan(slo))
    val xd: Double = siz * Math.cos(Math.atan(slo))
    val lis: ArrayList<Segment> = ArrayList()
    lis.add(Segment(Point(pm1x + xd, pm1y + yd), Point(pm1x - xd, pm1y - yd)))
    lis.add(Segment(Point(pm1x - xd, pm1y - yd), Point(pm2x - xd, pm2y - yd)))
    lis.add(Segment(Point(pm2x - xd, pm2y - yd), Point(pm2x + xd, pm2y + yd)))
    lis.add(Segment(Point(pm2x + xd, pm2y + yd), Point(pm1x + xd, pm1y + yd)))
    print(lis)

    return Road(Polygon(lis))


}

fun getSlop(p1: Point, p2: Point): Double {
    return (p2.y - p1.y) / (p2.x - p1.x)
}

fun getPoint(seg: Segment): Point {
    val loc: Double = Math.random()
    return Point(seg.p1.x + loc * (seg.p2.x - seg.p1.x), seg.p1.y + loc * (seg.p2.y - seg.p1.y))
}

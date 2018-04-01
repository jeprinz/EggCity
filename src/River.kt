class River(shap: Polygon) : Structure {
    val shape: Polygon = shap
}

fun makeRiver(poly: Polygon, wid: Double): River {
    return River(longboy(poly, wid))
}

fun longboy(poly: Polygon, wid: Double): Polygon {
    var i1 = 0
    var i2 = 0
    while (i1 == i2) {
        i1 = (Math.random() * poly.segs.size).toInt()
        i2 = (Math.random() * poly.segs.size).toInt()
    }
    val s1: Segment = poly.segs.get(i1)
    val s2: Segment = poly.segs.get(i2)
    val seglis: ArrayList<Segment> = ArrayList()
    seglis.add(Segment(s1.p1, s2.p2))
    val top: ArrayList<Point> = ArrayList()
    val bot: ArrayList<Point> = ArrayList()
    var slop = 0.0
    for (s in seglis) {
        val pl: ArrayList<Point> = getper(s, wid)
        if (top.isEmpty() && bot.isEmpty()) {
            top.add(pl[0])
            bot.add(pl.get(1))
        }
        if (slop < getSlop(s.p1, s.p2)) {
            top.add(top.size - 1, pl.get(0))
            bot.add(pl.get(1))
        }
        top.add(pl.get(2))
        if (slop > getSlop(s.p1, s.p2)) {
            bot.add(bot.size - 1, pl.get(1))
            bot.add(pl.get(0))
        }
        bot.add(pl.get(3))
        slop = getSlop(s.p1, s.p2)
    }
    val liss: ArrayList<Segment> = ArrayList()
    liss.add(Segment(bot.get(0), top.get(0)))
    liss.addAll(pointToSeg(top))
    liss.add(Segment(top.get(top.size - 1), bot.get(bot.size - 1)))
    liss.addAll(pointToSeg(bot))
    return Polygon(liss)

}

fun getper(s: Segment, wid: Double): ArrayList<Point> {
    val slo = -1.0 / getSlop(s.p1, s.p2)
    val yd: Double = wid * Math.sin(Math.atan(slo))
    val xd: Double = wid * Math.cos(Math.atan(slo))
    val lis: ArrayList<Point> = ArrayList()
    lis.add(Point(s.p1.x + xd, s.p1.y + yd))
    lis.add(Point(s.p1.x - xd, s.p1.y - yd))
    lis.add(Point(s.p2.x + xd, s.p2.y + yd))
    lis.add(Point(s.p2.x - xd, s.p2.y - yd))
    return lis

}

fun pointToSeg(lis: ArrayList<Point>): ArrayList<Segment> {
    val seglis: ArrayList<Segment> = ArrayList()
    for (i in 1..(lis.size)) {
        seglis.add(Segment(lis.get(i - 1), lis.get(i)))
    }
    return seglis
}
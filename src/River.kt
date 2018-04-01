class River(shap: Polygon) : Structure {
    val shape: Polygon = shap
}

fun makeRiver(lis: ArrayList<Point>, wid: Double): River {
    return River(longboy(lis, wid))
}

fun longboy(lis: ArrayList<Point>, wid: Double): Polygon {
    var seglis: ArrayList<Segment> = pointToSeg(lis)
    var top: ArrayList<Point> = ArrayList()
    var bot: ArrayList<Point> = ArrayList()
    var slop: Double = 0.0
    for (s in seglis) {
        var pl: ArrayList<Point> = getper(s, wid)
        if (top.isEmpty() && bot.isEmpty()) {
            top.add(pl.get(0))
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
    var lis: ArrayList<Segment> = ArrayList()
    lis.add(Segment(bot.get(0), top.get(0)))
    lis.addAll(pointToSeg(top))
    lis.add(Segment(top.get(top.size - 1), bot.get(bot.size - 1)))
    lis.addAll(pointToSeg(bot))
    return Polygon(lis)

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
    var seglis: ArrayList<Segment> = ArrayList()
    for (i in 1..(lis.size)) {
        seglis.add(Segment(lis.get(i - 1), lis.get(i)))
    }
    return seglis
}
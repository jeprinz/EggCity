class Polygon(nsegs: Collection<Segment>) {
    val pts: ArrayList<Point> = arrayListOf()
    val segs: ArrayList<Segment> = ArrayList(nsegs)
    init {
        for (i in 0..segs.size-1) {
            pts.add(segs[i].p1)
        }
//        for (i in 1..segs.size) {
//            for (j in 0..i) {
//                if (intersect(segs[i], segs[j])) {
//                    throw RuntimeException("Polygon is self-intersecting")
//                }
//            }
//        }
    }

    fun inside(pt: Point): Boolean {
        val ray = Segment(pt, Point(999999999.0,999999999.0))
        var inside = false
        if (includesPoint(pt)) {
            return false
        }
        segs.forEach(
                {seg -> inside = (inside != intersect(ray, seg))}
        )
        return inside
    }

    fun includesPoint(pt: Point): Boolean {
        var on = false
        segs.forEach(
                {seg -> on = on || seg.contains(pt)}
        )
        return on
    }

    fun intersectSegment(segment: Segment): Segment? {
        segs.forEach { seg -> if (intersect(seg, segment)) {return seg}}
        return null
    }

    fun inside(seg: Segment): Boolean {//actually is midpoint inside
        return inside(Point((seg.p1.x + seg.p2.x)/2, (seg.p1.y + seg.p2.y)/2))
    }

    fun midpointOn(seg: Segment): Boolean {
        return includesPoint(Point((seg.p1.x + seg.p2.x)/2, (seg.p1.y + seg.p2.y)/2))
    }

    fun sliceSegment(seg: Segment) : Collection<Segment>{
        val intersector = intersectSegment(seg);
        if(intersector != null){
            val mid = intersection(seg, intersector)
            println("seg: ${seg}, intersector: ${intersector}")
            return sliceSegment(Segment(seg.p1, mid)).union(sliceSegment(Segment(mid, seg.p2)))
        } else {
            return arrayListOf(seg)
        }

    }

    fun slicePoly(chopMeUp : Polygon): Polygon{
        val newSegs = arrayListOf<Segment>()
        chopMeUp.segs.map(
                {
                    seg ->
                    newSegs.addAll(sliceSegment(seg))
                }
        )
        return Polygon(newSegs)
    }

    fun centroid(): Point { //doesn't actually calculate the centroid
        var sx: Double = 0.0
        var sy: Double = 0.0
        pts.forEach(
                {
                    pt -> sx = sx + pt.x; sy = sy + pt.y
                }
        )
        return Point(sx/pts.size, sy/pts.size)
    }

    fun width(): Double {
        var minx: Double = pts[0].x
        var maxx: Double = pts[0].x
        pts.forEach(
                {
                    pt -> if (pt.x < minx) {minx = pt.x}; if (pt.x > maxx) {maxx = pt.x}
                }
        )
        return maxx - minx
    }

    fun height(): Double {
        var minx: Double = pts[0].x
        var maxx: Double = pts[0].x
        pts.forEach(
                {
                    pt -> if (pt.y < minx) {minx = pt.y}; if (pt.y > maxx) {maxx = pt.y}
                }
        )
        return maxx - minx
    }

    fun area(): Double {
        var shoelace: Double = 0.0
        for (i in 0..pts.size-1) {
            shoelace = shoelace + pts[i].x*pts[(i+1)%pts.size].y - pts[i].y*pts[(i+1)%pts.size].x
        }
        return Math.abs(shoelace)/2
    }

    override fun toString(): String {
        return segs.toString()
    }

}

class Path(npts: ArrayList<Point>) {
    val pts: ArrayList<Point> = npts
    val segs: ArrayList<Segment> = arrayListOf()
    init {
        for (i in 0..pts.size-1) {
            segs.add(Segment(pts[i], pts[i+1]))
        }
        for (i in 1..segs.size) {
            for (j in 0..i) {
                if (intersect(segs[i], segs[j])) {
                    throw RuntimeException("Path is self-intersecting")
                }
            }
        }
    }

    override fun toString(): String{
        return segs.toString()
    }
}

fun adjacent(P1: Polygon, P2: Polygon): Boolean {
    return HashSet(P1.segs).intersect(HashSet(P2.segs)).isNotEmpty()
}

class Point(nx: Double, ny: Double) {
    val x: Double = nx
    val y: Double = ny

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other?.javaClass != javaClass -> false
            else -> (other as Point).x == x && other.y == y
        }
    }

    override fun toString(): String {
        return "[${x}, ${y}]"
    }
    fun equals(p: Point): Boolean {
        return p.x == x && p.y == y
    }
}

class Segment(initial: Point, terminal: Point) {
    val p1: Point = initial
    val p2: Point = terminal
    override fun toString(): String {
        return "[${p1}, ${p2}]"
    }

    fun contains(pt: Point): Boolean {
        if ((pt.y - p1.y)*(p2.x - p1.x) == (p2.y - p1.y)*(pt.x - p1.x)) {
            if (minX() > pt.x || maxX() < pt.x) {
                return false
            }
            if (minY() > pt.y || maxY() < pt.y) {
                return false
            }
            return true
        }
        return false
    }

    fun slope(): Double {
        return (p1.y - p2.y)/(p1.x - p2.x)
    }

    fun minX(): Double {
        return Math.min(p1.x, p2.x)
    }

    fun maxX(): Double {
        return Math.max(p1.x, p2.x)
    }

    fun minY(): Double {
        return Math.min(p1.y, p2.y)
    }

    fun maxY(): Double {
        return Math.max(p1.y, p2.y)
    }

    fun length(): Double{
        return Math.sqrt(Math.pow((p2.x - p1.x),2.0) + Math.pow((p2.y - p1.y),2.0))
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other?.javaClass != javaClass -> false
            else -> (other as Segment).p1 == p1 && other.p2 == p2
        }
    }
}

fun ccw(p1: Point, p2: Point, p3: Point): Boolean { // checks if 3 points are oriented counterclockwise
    return ((p3.y - p1.y) * (p2.x - p1.x) > (p2.y - p1.y) * (p3.x - p1.x))
}

fun intersect(s1: Segment, s2: Segment): Boolean {
    if ((ccw(s1.p1, s2.p1, s2.p2)  != ccw(s1.p2, s2.p1, s2.p2)) && (ccw(s1.p1, s1.p2, s2.p1) != ccw(s1.p1, s1.p2, s2.p2))) {
        val ip = intersection(s1, s2)
        if (ip.equals(Point(Double.NaN, Double.NaN))) {return false}
        return !(ip.equals(s1.p1) || ip.equals(s1.p2) || ip.equals(s2.p1) || ip.equals(s2.p2))
    }
    return false
}

fun concurrence(s1: Segment, s2: Segment): Segment? {
    val slop = s1.slope()
    if (slop == s2.slope() && slop == Segment(s1.p1, s2.p1).slope()) {
        val x0: Double = Math.max(s1.minX(), s2.minX())
        val x1: Double = Math.min(s1.maxX(), s2.maxX())
        if (x0 >= x1) {return null}
        else {
            return Segment(Point(x0, s1.p1.y + slop*(x0 - s1.p1.x)), Point(x1, s1.p1.y + slop*(x1 - s1.p1.x)))
        }
    }
    else if (slop.isNaN() && s2.slope().isNaN() && Segment(s1.p1, s2.p1).slope().isNaN()) {
        val y0: Double = Math.max(s1.minY(), s2.minY())
        val y1: Double = Math.min(s1.maxY(), s2.maxY())
        if (y0 >= y1) {return null}
        else {
            return Segment(Point(s1.p1.x,y0), Point(s1.p1.x,y1))
        }
    }
    return null
}

fun intersection(s1: Segment, s2: Segment): Point {
    return Point((((s1.p1.x*s1.p2.y) - (s1.p1.y*s1.p2.x))*(s2.p1.x - s2.p2.x) - (s1.p1.x - s1.p2.x)*((s2.p1.x*s2.p2.y) - (s2.p1.y*s2.p2.x)))
            / ((s1.p1.x - s1.p2.x)*(s2.p1.y - s2.p2.y) - (s1.p1.y - s1.p2.y)*(s2.p1.x - s2.p2.x)),
            (((s1.p1.x*s1.p2.y) - (s1.p1.y*s1.p2.x))*(s2.p1.y - s2.p2.y) - (s1.p1.y - s1.p2.y)*((s2.p1.x*s2.p2.y) - (s2.p1.y*s2.p2.x)))
                    / ((s1.p1.x - s1.p2.x)*(s2.p1.y - s2.p2.y) - (s1.p1.y - s1.p2.y)*(s2.p1.x - s2.p2.x))
            )
}

fun main(args: Array<String>) {
    val seg1 = Segment(Point(1.0,1.0), Point(2.0,2.0))
    val seg2 = Segment(Point(1.5,0.0), Point(1.5,8.0))
    val inter = intersection(seg1, seg2)
    println("(" + inter.x + "," + inter.y + ")")
}

fun polyFromPoints(pts : List<Point>) : Polygon{
    val segs = arrayListOf<Segment>()
    for (i in 0..pts.size-1){
        segs.add(Segment(pts[i%pts.size], pts[(i+1)%pts.size]))
    }
    return Polygon(segs)
}
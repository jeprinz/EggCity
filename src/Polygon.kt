class Polygon(nsegs: Collection<Segment>) {
    val pts: ArrayList<Point> = arrayListOf()
    val segs: ArrayList<Segment> = ArrayList(nsegs)
    init {
        for (i in 0..pts.size-1) {
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
        val ray = Segment(pt, Point(Double.MAX_VALUE, Double.MAX_VALUE))
        var inside = false
        segs.forEach(
                {seg -> inside = (inside != intersect(ray, seg))}
        )
        return inside
    }

    fun intersectSegment(segment: Segment): Segment? {
        segs.forEach(
                {seg -> if (intersect(seg, segment)) {return segment}}
        )
        return null
    }

    fun inside(seg: Segment): Boolean {//actually is midpoint inside
        return inside(Point((seg.p1.x + seg.p2.x)/2, (seg.p1.y + seg.p2.y)/2))
    }

    fun sliceSegment(seg: Segment) : Collection<Segment>{
        val intersector = intersectSegment(seg);
        if(intersector != null){
            val mid = intersection(seg, intersector)
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
        var minx: Double = Double.MAX_VALUE
        var maxx: Double = Double.MIN_VALUE
        pts.forEach(
                {
                    pt -> if (pt.x < minx) {minx = pt.x}; if (pt.x > maxx) {maxx = pt.x}
                }
        )
        return maxx - minx
    }

    fun height(): Double {
        var minx: Double = Double.MAX_VALUE
        var maxx: Double = Double.MIN_VALUE
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
}

class Segment(initial: Point, terminal: Point) {
    val p1: Point = initial
    val p2: Point = terminal
}

fun ccw(p1: Point, p2: Point, p3: Point): Boolean { // checks if 3 points are oriented counterclockwise
    return ((p3.y - p1.y) * (p2.x - p1.x) > (p2.y - p1.y) * (p3.x - p1.x))
}

fun intersect(s1: Segment, s2: Segment): Boolean {
    return (ccw(s1.p1, s2.p1, s2.p2)  != ccw(s1.p2, s2.p1, s2.p2)) && (ccw(s1.p1, s1.p2, s2.p1) != ccw(s1.p1, s1.p2, s2.p2))
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
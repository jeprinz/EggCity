class Polygon(nsegs: Collection<Segment>) {
    val pts: ArrayList<Point> = arrayListOf()
    val segs: ArrayList<Segment> = ArrayList(nsegs)
    init {
        for (i in 0..pts.size) {
            pts.add(segs[i].p1)
        }
        for (i in 1..segs.size) {
            for (j in 0..i) {
                if (intersect(segs[i], segs[j])) {
                    throw RuntimeException("Polygon is self-intersecting")
                }
            }
        }
    }

    fun inside(pt: Point): Boolean {
        val ray = Segment(pt, Point(Double.MAX_VALUE, Double.MAX_VALUE))
        var inside = false
        segs.forEach(
                {seg -> inside = (inside != intersect(ray, seg))}
        )
        return inside
    }

    fun intersectSegment(segment: Segment): Boolean {
        segs.forEach(
                {seg -> if (intersect(seg, segment)) {return true}}
        )
        return false
    }

    fun inside(seg: Segment): Boolean {
        return (inside(seg.p1) && inside(seg.p2))
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
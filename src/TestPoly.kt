fun main(args: Array<String>){
    val seg1 = Segment(Point(5.0, 50.0), Point(5.0, -50.0))
    val seg1a = Segment(Point(10.0, 50.0), Point(10.0, -50.0))
    val seg2 = Segment(Point(-50.0, 5.0), Point(50.0, 5.0))

    val r1: Polygon = makeRect(Point(0.0,0.0), 100.0, 10.0)
    val r2 = makeRect(Point(0.0,0.0), 10.0, 100.0)

    val res = r1.slicePoly(r2)
    print(res)

}
import com.sun.deploy.registration.InstallHints

class Road(shap:Polygon) {
    val shape:Polygon=shap
}
fun makeRoad(bla:Blank, siz:Double):Road {
var segg: ArrayList<Segment> =bla.poly.segs
    var i1:Int =0
    var i2:Int =0
    while (i1==i2) {
        i1 = (Math.random() * segg.size).toInt()
        i2 = (Math.random() * segg.size).toInt()
    }
    var s1:Segment=segg.get(i1)
    var s2:Segment=segg.get(i2)
    var pm1:Point=getPoint(s1);
    var pm2:Point=getPoint(s2)
    var pm1x:Double=pm1.x
    var pm1y:Double=pm1.y
    var pm2x:Double=pm2.x
    var pm2y:Double=pm2.y

    var slo=-1.0/getSlop(pm1,pm2)
    var yd:Double= siz/2*slo
    var xd:Double=Math.pow(siz*siz/4-yd*yd,.5)
    var lis:ArrayList<Segment> = ArrayList()
    lis.add(Segment(Point(pm1x+xd,pm1y+yd),Point(pm1x-xd,pm1y-yd)))
    lis.add(Segment(Point(pm1x-xd,pm1y-yd),Point(pm2x-xd,pm2y-yd)))
    lis.add(Segment(Point(pm2x+xd,pm2y+yd),Point(pm2x-xd,pm2y-yd)))
    lis.add(Segment(Point(pm1x+xd,pm1y+yd),Point(pm2x+xd,pm2y+yd)))

    return Road(Polygon(lis))


}
fun getSlop(p1:Point,p2:Point):Double{
    return (p2.y-p1.y)/(p2.x-p1.x)
}
fun getPoint(seg:Segment):Point{
    var loc:Double = Math.random()
    return Point(seg.p1.x+loc*(seg.p2.x-seg.p1.x),seg.p1.y+loc*(seg.p2.y-seg.p1.y))
}

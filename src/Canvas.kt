import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon as PolygonFX
import javafx.stage.Stage
import kotlin.collections.LinkedHashMap

class Canvas: Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage ?: return
        primaryStage.title = "City Generator"

        val btn = Button("Generate")
        btn.onAction = EventHandler<ActionEvent>() {
            println("Clicked")
        }

        val city = makecity(100.0, .50, 100, 3,1.0)
        city.shape.segs.shuffle()
        val cityPoly = polyToFXPoly(city.shape)
        cityPoly.fill = Color.rgb(255, 255, 255)
                cityPoly.stroke = Color.rgb(0, 0, 0)

        val root = Group()
        root.children.add(cityPoly)

        primaryStage.run {
            scene = Scene(root, 400.0, 400.0)
            show()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Canvas::class.java)
        }
    }

}

fun polyToFXPoly(poly : Polygon) : PolygonFX {
    val segMap = LinkedHashMap<Segment, Segment>()
    var found = false
    for (seg1 in poly.segs) {
        for (seg2 in poly.segs) {
            if (seg1.p2 == seg2.p1) {
                segMap[seg1] = seg2
                found = true
                break
            }
        }
        if (!found) {
            throw RuntimeException("Segment has no adjacent segment")
        }
    }
    val firstSeg = poly.segs[0]
    val doubleList = arrayListOf(firstSeg.p1.x + 200, firstSeg.p1.y + 200)
    var currSeg = segMap[firstSeg]
    while (currSeg != firstSeg) {
        doubleList.add(currSeg!!.p1.x + 200)
        doubleList.add(currSeg.p1.y + 200)
        currSeg = segMap[currSeg]
    }

    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}

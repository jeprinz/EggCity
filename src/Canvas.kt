import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon as PolygonFX
import javafx.stage.Stage
import java.util.*
import kotlin.collections.ArrayList

class Canvas: Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage ?: return
        primaryStage.title = "City Generator"

        val btn = Button("Generate")
        btn.onAction = EventHandler<ActionEvent>() {
            println("Clicked")
        }

        val pts1 = arrayOf<Double>(
                20.0, 20.0,
                20.0, 100.0,
                200.0, 100.0,
                200.0, 20.0
        )
        val poly1 = PolygonFX()
        poly1.points.addAll(pts1)
        poly1.fill = Color.rgb(102,255,153)

        val pts2 = arrayOf<Double>(
                250.0, 225.0,
                225.0, 375.0,
                350.0, 350.0,
                325.0, 210.0
        )
        val poly2 = PolygonFX()
        poly2.points.addAll(pts2)
        poly2.fill = Color.rgb(0,153,255)

//        val city = makecity(100.0, 0.3, 100, 10)

        val root = Group()
//        root.children.add(polyToFXPoly(city.shape))

        root.children.add(poly1)
        root.children.add(poly2)

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
//    val segMap = TreeMap<Segment, Segment>()
    val doubleList = arrayListOf<Double>()
    for (seg in poly.segs) {
        doubleList.add(seg.p1.x)
        doubleList.add(seg.p1.y)
    }
    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}

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

        val city = makecity(100.0, 0.25, 100, 3)

        val root = Group()
        root.children.add(polyToFXPoly(city.shape))

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
    val doubleList = arrayListOf<Double>()
    for (seg in poly.segs) {
        doubleList.add(seg.p1.x + 200)
        doubleList.add(seg.p1.y + 200)
    }
    println(doubleList.toString())
    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}

fun polyToFXPoly2(poly : Polygon) : PolygonFX {
    val segMap = TreeMap<Segment, Segment>()
    val doubleList = arrayListOf<Double>()
    for (seg1 in poly.segs) {
        for (seg2 in poly.segs) {

        }
    }
    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}

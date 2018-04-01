import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon as PolygonFX
import javafx.stage.Stage
import kotlin.collections.LinkedHashMap
import javafx.scene.canvas.Canvas as CanvasFX


class CityCanvas: Application() {


    override fun start(primaryStage: Stage?) {
        primaryStage ?: return
        primaryStage.title = "City Generator"

        val polyGroup = Group()

        polyGroup.onZoom = EventHandler { it ->
            it.consume()
            val scale = it.totalZoomFactor
            polyGroup.scaleX = scale
            polyGroup.scaleY = scale
        }

        val comboVariance = ComboBox<String>(FXCollections.observableArrayList("High", "Medium", "Low"))
        comboVariance.promptText = "Variance"

        val textNumPoints = TextField()
        textNumPoints.promptText = "# of Pts"
        textNumPoints.textProperty().addListener(ChangeListener { _, _, newValue ->
            if (!newValue.matches(Regex("\\d*"))) {
                textNumPoints.text = newValue.replace(Regex("[^\\d]"), "")
            }
        })

        val comboRandomness = ComboBox<String>(FXCollections.observableArrayList("High", "Medium", "Low"))
        comboRandomness.promptText = "Randomness"

        val btn = Button("Generate")
        btn.onAction = EventHandler<ActionEvent>() {
            val variance = when (comboVariance.value) {
                "High" -> .7
                "Medium" -> .5
                "Low" -> .25
                else -> 0.0
            }
            val numPoints = if (textNumPoints.text.isEmpty()) {50} else {textNumPoints.text.toInt()}
            val randomness = when (comboRandomness.value) {
                "High" -> 25
                "Medium" -> 10
                "Low" -> 5
                else -> 0
            }

            val canvas = CanvasFX(500.0, 500.0)
            val gc = canvas.graphicsContext2D
            drawPolys(gc)
            polyGroup.children.remove(0, polyGroup.children.size)
            polyGroup.children.add(canvas)

            val cityStage = Stage()
            val cityRoot = VBox()
            cityRoot.children.add(polyGroup)
            cityRoot.alignment = Pos.CENTER

            cityStage.run {
                scene = Scene(cityRoot, 500.0, 500.0)
                show()
            }
        }

        val startRoot = VBox()
        startRoot.children.addAll(comboVariance, textNumPoints, comboRandomness, btn)
        startRoot.alignment = Pos.CENTER

        primaryStage.run {
            scene = Scene(startRoot, 200.0, 200.0)
            show()
        }
    }

    private fun drawPolys(gc: GraphicsContext) {
        val cityOutline = makecity(100.0, .5, 10, 5, 2.0)
        val cityPolyPts = outlineToStuff(cityOutline.shape)
        gc.strokePolygon(DoubleArray(cityPolyPts.size, { i -> cityPolyPts[i].x}),
                DoubleArray(cityPolyPts.size, { i -> cityPolyPts[i].y}), cityPolyPts.size)
    }

    private fun outlineToStuff(poly : Polygon) : ArrayList<Point> {
        println(poly)
        val diffX = 250
        val diffY = 250
        val firstSeg = poly.segs[0]
        var currSeg = firstSeg
        var currPnt = firstSeg.p2
        val pts = arrayListOf(Point(firstSeg.p2.x + diffX, firstSeg.p2.y + diffY))
        while (currPnt != firstSeg.p1) {
            var found = false
            for (seg in poly.segs) {
                if (seg.p1 == currPnt && currSeg != seg) {
                    currPnt = seg.p2
                    currSeg = seg
                    found = true
                    break
                }
                else if (seg.p2 == currPnt && currSeg != seg) {
                    currPnt = seg.p1
                    currSeg = seg
                    found = true
                    break
                }
            }
            if (!found) {
                throw RuntimeException("Segment has no adjacent segment")
            }
            pts.add(Point(currPnt.x + diffX, currPnt.y + diffY))
        }
        return pts
    }



    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(CityCanvas::class.java)
        }
    }
}

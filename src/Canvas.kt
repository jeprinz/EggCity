import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.input.ZoomEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon as PolygonFX
import javafx.stage.Stage
import kotlin.collections.LinkedHashMap

class Canvas: Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage ?: return
        primaryStage.title = "City Generator"

        val polyGroup = Group()
        polyGroup.onZoom = EventHandler { it ->
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
            val variance = when {
                comboVariance.value == "High" -> .7
                comboVariance.value == "Medium" -> .5
                comboVariance.value == "Low" -> .25
                else -> 0.0
            }
            val numPoints = if (textNumPoints.text.isEmpty()) {50} else {textNumPoints.text.toInt()}
            val randomness = when {
                comboRandomness.value == "High" -> 25
                comboRandomness.value == "Medium" -> 10
                comboRandomness.value == "Low" -> 5
                else -> 0
            }
            val cityOutline = makecity(300.0, variance, numPoints, randomness, 1.0)
            val cityPolyFX = polyToFXPoly(cityOutline.shape, false)
            cityPolyFX.fill = Color.rgb(255, 255, 255)
            cityPolyFX.stroke = Color.rgb(0, 0, 0)

            val road = makeRoad(cityOutline.shape, 5.0)
            val roadPolyFX = polyToFXPoly(road.shape, false)
            roadPolyFX.fill = Color.rgb(0, 100, 0)

            polyGroup.children.remove(0, polyGroup.children.size)
            polyGroup.children.add(cityPolyFX)
            polyGroup.children.add(roadPolyFX)

            val cityStage = Stage()
            val cityRoot = VBox()
            cityRoot.children.add(polyGroup)
            cityRoot.alignment = Pos.CENTER

            cityStage.run {
                scene = Scene(cityRoot, 500.0, 500.0)
                show()
            }
        }

        val root = VBox()
        root.children.addAll(comboVariance, textNumPoints, comboRandomness, btn)
        root.alignment = Pos.CENTER

        primaryStage.run {
            scene = Scene(root, 200.0, 200.0)
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

fun polyToFXPoly(poly : Polygon, printVals: Boolean) : PolygonFX {
    val segMap = LinkedHashMap<Segment, Segment>()
//    val segsFound = ArrayList<Segment>()
    var found = false
    for (seg1 in poly.segs) {
        if (printVals) {
            println("(" + seg1.p1.x + "," + seg1.p1.y + ") (" + seg1.p2.x + "," + seg1.p2.y + ")")
        }
        for (seg2 in poly.segs) {
            if ((seg1.p1 == seg2.p1 && seg1.p2 != seg1.p2) || (seg1.p1 == seg2.p2 && seg1.p2 != seg1.p1) ||
                    (seg1.p2 == seg2.p1 && seg1.p1 != seg1.p2) || (seg1.p2 == seg2.p2 && seg1.p1 != seg1.p1)) {
                if (segMap[seg2] == seg1) {
                    continue
                }
                segMap[seg1] = seg2
                found = true
                break
            }
        }
        if (!found) {
            throw RuntimeException("Segment has no adjacent segment")
        }
    }
    val diffX = 0.0
    val diffY = 0.0
    val firstSeg = poly.segs[0]
    val doubleList = arrayListOf(firstSeg.p1.x + diffX, firstSeg.p1.y + diffY)
    var prevSeg = firstSeg
    var currSeg = segMap[firstSeg]
    while (currSeg != firstSeg) {
        when {
            currSeg!!.p1 == prevSeg.p1 || currSeg.p1 == prevSeg.p2 -> {
                doubleList.add(currSeg.p1.x + diffX)
                doubleList.add(currSeg.p1.y + diffY)
            }
            currSeg.p2 == prevSeg.p1 || currSeg.p2 == prevSeg.p2 -> {
                doubleList.add(currSeg.p2.x + diffX)
                doubleList.add(currSeg.p2.y + diffY)
            }
        }
        prevSeg = currSeg!!
        currSeg = segMap[currSeg]
    }

    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}

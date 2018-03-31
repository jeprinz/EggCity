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
import javafx.scene.input.TransferMode
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
            it.consume()
            val scale = it.totalZoomFactor
            polyGroup.scaleX = scale
            polyGroup.scaleY = scale
        }

//        polyGroup.onDragDetected = EventHandler { event ->
//            event.consume()
//            var db = polyGroup.startDragAndDrop(TransferMode.MOVE)
//
//        }

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
//            val cityOutline = makecity(300.0, variance, numPoints, randomness, 1.0)
//            val cityPolyFX = polyToFXPoly(cityOutline.shape)
//            cityPolyFX.fill = Color.rgb(255, 255, 255)
//            cityPolyFX.stroke = Color.rgb(0, 0, 0)

            val polygonList = graphToPolyFXList(genCity(100.0))

            polyGroup.children.remove(0, polyGroup.children.size)
            polyGroup.children.addAll(polygonList)

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

fun polyToFXPoly(poly : Polygon) : PolygonFX {
    val segMap = LinkedHashMap<Segment, Segment>()
    var found = false
    for (seg1 in poly.segs) {
        for (seg2 in poly.segs) {
            if (matchOnePoint(seg1, seg2)) {
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

fun matchOnePoint(seg1: Segment, seg2: Segment) : Boolean {
    return (seg1.p1 == seg2.p1 || seg1.p1 == seg2.p2 || seg1.p2 == seg2.p1 || seg1.p2 == seg2.p2) && seg1 != seg2
}

fun graphToPolyFXList(polygonGraph : PolygonGraph<Structure>): ArrayList<PolygonFX> {
    val nodesList = polygonGraph.getNodes()
    val polyFXList = ArrayList<PolygonFX>()
    for (node in nodesList) {
        val nodePoly = node.getPolygon()
        val nodePolyFX = polyToFXPoly(nodePoly)
        when (node.getData().javaClass) {
            Road::class -> {
                nodePolyFX.fill = Color.rgb(0, 0, 100)
            }
            else -> {
                nodePolyFX.fill = Color.rgb(255, 255, 255)
                nodePolyFX.stroke = Color.rgb(0, 0, 0)
            }
        }
        polyFXList.add(nodePolyFX)
    }
    return polyFXList
}

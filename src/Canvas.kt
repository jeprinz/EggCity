/*
import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon as PolygonFX
import javafx.stage.Stage
import java.util.*
import kotlin.collections.LinkedHashMap

class Canvas: Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage ?: return
        primaryStage.title = "City Generator"

        val polyGroup = Group()

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
            val numPoints = textNumPoints.text.toInt()
            val randomness = when {
                comboRandomness.value == "High" -> 25
                comboRandomness.value == "Medium" -> 10
                comboRandomness.value == "Low" -> 5
                else -> 0
            }
            val city = makecity(300.0, variance, numPoints, randomness, 2.0)
//            city.shape.segs.shuffle()
            val cityPoly = polyToFXPoly(city.shape)
            cityPoly.fill = Color.rgb(255, 255, 255)
            cityPoly.stroke = Color.rgb(0, 0, 0)

            polyGroup.children.remove(0, polyGroup.children.size)
            polyGroup.children.add(cityPoly)
        }

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.add(comboVariance,0, 0)
        grid.add(textNumPoints,1, 0)
        grid.add(comboRandomness,2, 0)
        grid.add(btn,3, 0)

        val root = VBox()
        root.children.add(grid)
        root.children.add(polyGroup)
        root.alignment = Pos.BASELINE_CENTER


        primaryStage.run {
            scene = Scene(root, 500.0, 500.0)
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
    val diff = 0.0
    val firstSeg = poly.segs[0]
    val doubleList = arrayListOf(firstSeg.p1.x + diff, firstSeg.p1.y + diff)
    println(doubleList[0])
    var currSeg = segMap[firstSeg]
    while (currSeg != firstSeg) {
        doubleList.add(currSeg!!.p1.x + diff)
        doubleList.add(currSeg.p1.y + diff)
        currSeg = segMap[currSeg]
    }
    val result = PolygonFX()
    result.points.addAll(doubleList)
    return result
}*/

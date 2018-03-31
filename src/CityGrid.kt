import com.sun.org.apache.xpath.internal.operations.Bool
import sun.security.util.PolicyUtil

class CityGrid(cityArea : Polygon){
    val adjacencies : Graph<Structure> = Graph();

    init {
        adjacencies.addNode(Blank(cityArea))
    }

    fun getBlanks() : Collection<Blank> {
        val blanks = arrayListOf<Blank>()
        val structures = adjacencies.getNodes()
        structures.forEach(fun(s : Structure){
            if (s is Blank) {
                blanks.add(s)
            }
        })
        return blanks
    }

    fun placeInBlank(blank : Blank, structure: Structure){
        if (!adjacencies.hasNode(blank)){
            throw RuntimeException("Tried to place structure in blank that does not exist in city");
        } else {
            adjacencies.removeNode(blank);
            //here is where I need
        }
    }
}

class Blank(poly : Polygon) : Structure(poly){
}

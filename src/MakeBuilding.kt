fun buildPlot(blank: PolygonGraph<Structure>.NodeId, polyG: PolygonGraph<Structure>): Pair<Polygon, Plot>{
    val surrounding = blank.getAdjacentNodes()
    val roads : HashMap<Road, Collection<Segment>> = hashMapOf()
    for (thing in surrounding){
        val data = thing.getData()
        if (data is Road){
            roads.put(data, blank.getSharedEdges(thing))
        }
    }




}

class Plot : Structure{

}
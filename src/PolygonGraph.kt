class PolygonGraph<NodeData>(initialNode: NodeData) { // NodeData or No Data??????????
    val adjacencies : Graph<NodeData, Edge> = Graph()
    init {
        adjacencies.addNode(initialNode)
    }

    fun splitInnerPolygon(node: Graph.Node<NodeData, Edge>, data: NodeData, polygon: Polygon): Graph.Node<NodeData, Edge> {
        val newNode = adjacencies.addNode(data)
        polygon.segs.forEach({seg -> adjacencies.addEdge(newNode, node, Edge(seg))})
        return newNode
    }

    fun splitPolygon(node: Graph.Node<NodeData, Edge>, clipper: Polygon): {
        val intClipSegs: ArrayList<Segment> = arrayListOf()
        val intOldSegs: ArrayList<Segment> = arrayListOf()
        val intersectingSegs: ArrayList<Segment> = arrayListOf()
        val oldPolygon: Polygon = Polygon(node.neighbors.map({edge -> edge.data.seg}))
        clipper.segs.forEach(
                {
                    seg -> if (oldPolygon.inside(seg)) {intClipSegs.add(seg)}
                    else if (oldPolygon.intersectSegment(seg)) {

                    }
                }
        )
        oldPolygon.segs.forEach(
                {seg -> if (clipper.inside(seg))}
        )

    }

    fun getNodes(): Collection<Graph.Node<NodeData, Edge>> {
        return adjacencies.getNodes()
    }

    class Edge (segment: Segment){
        val seg: Segment = segment
    }
}
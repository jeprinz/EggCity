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

    fun splitPolygon(node: Graph.Node<NodeData, Edge>, clipper: Polygon, n: NodeData) {
        val inClip: ArrayList<Segment> = arrayListOf()
        val inOld: ArrayList<Graph.Edge<NodeData, Edge>> = arrayListOf()
        val interClip: ArrayList<Segment> = arrayListOf()
        val interOld: ArrayList<Graph.Edge<NodeData, Edge>> = arrayListOf()
        val oldPolygon: Polygon = Polygon(node.neighbors.map({edge -> edge.data.seg}))
        clipper.segs.forEach(
                {
                    seg ->
                    if (oldPolygon.inside(seg)) {
                        inClip.add(seg)
                    }
                    else {
                        val intersector = oldPolygon.intersectSegment(seg)
                        if (intersector != null) {
                            interClip.add(seg)
                        }
                    }
                }
        )
        node.neighbors.forEach(
                {
                    e -> if (clipper.inside(e.data.seg)) {
                        inOld.add(e)
                    }
                    else {
                        val intersector = clipper.intersectSegment(e.data.seg)
                        if (intersector != null) {
                            interOld.add(e)
                        }
                    }
                }
        )

        val newNode = adjacencies.addNode(n)
        inClip.forEach(
                {
                    seg -> adjacencies.addEdge(newNode, node, Edge(seg))
                }
        )
        inOld.forEach(
                {
                    e -> 
                }
        )

    }

    fun getNodes(): Collection<Graph.Node<NodeData, Edge>> {
        return adjacencies.getNodes()
    }

    class Edge (segment: Segment){
        val seg: Segment = segment
    }
}


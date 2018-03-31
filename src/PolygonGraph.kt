import javax.xml.soap.Node

class PolygonGraph<NodeData>(initialNode: NodeData) { // NodeData or No Data??????????
    val adjacencies : Graph<NodeData, Edge> = Graph()
    init {
        adjacencies.addNode(initialNode)
    }

    fun splitInnerPolygon(node: NodeId, data: NodeData, polygon: Polygon): NodeId {
        val newNode = adjacencies.addNode(data)
        polygon.segs.forEach({seg -> adjacencies.addEdge(newNode, node.node, Edge(seg))})
        return NodeId(newNode)
    }

    fun getPolygon(node: NodeId): Polygon{
        return Polygon(node.node.neighbors.map({edge -> edge.data.seg}))
    }

    fun placePolygon(node: NodeId, clipper: Polygon, n: NodeData) {
        splitPolygon(node, clipper, n)
        fixNode(node.node)
    }

    fun placePolygons(node: NodeId, clippers: Collection<Polygon>, n: NodeData) {
        clippers.forEach {
            clipper -> splitPolygon(node, clipper, n)
        }
        fixNode(node.node)
    }

    private fun splitPolygon(node: NodeId, clipper: Polygon, n: NodeData) {//clipper is the poly were intersecting and making a new node out of
        val inClip: ArrayList<Segment> = arrayListOf()
        val inOld: HashMap<Segment, Graph.Edge<NodeData, Edge>> = hashMapOf()
        val oldPolygon: Polygon = Polygon(node.node.neighbors.map({edge -> edge.data.seg}))
        println("clipper: ${clipper}")
        println("oldPolygon: ${oldPolygon}")
        val mincedClipper = oldPolygon.slicePoly(clipper);
        println("mincedClipper: ${mincedClipper}")
        val mincedEdgesOld = sliceEdges(node.node.neighbors, clipper)
        println("size mincedClip: ${mincedClipper.segs.size} size mincedEdgesOld: ${mincedEdgesOld.size}")
        mincedClipper.segs.forEach(
                {
                    seg ->
                    if (oldPolygon.inside(seg)) {
                        inClip.add(seg)
                    }
                }
        )

        mincedEdgesOld.forEach(
                {
                    (seg, edge) -> if (clipper.inside(seg)) {
                        inOld.put(seg, edge)
                    }
                    if (clipper.midpointOn(seg)) {
                        inOld.put(seg, edge)
                    }
                }
        )

        val newNode = adjacencies.addNode(n)
        inClip.forEach(
                {
                    seg -> adjacencies.addEdge(newNode, node.node, Edge(seg))
                }
        )
        inOld.forEach(
                {
                    (seg, e) ->
                    val otherNode = if(e.node1 != node){e.node1}else{e.node2}
                    adjacencies.addEdge(otherNode, newNode, Edge(seg))
                }
        )
        inOld.values.forEach({
            edge -> adjacencies.removeEdge(edge)
        })
    }

    private fun fixNode(node: Graph.Node<NodeData, Edge>){
        val edges = node.neighbors
        fun adjace(a: Graph.Edge<NodeData, Edge>, b: Graph.Edge<NodeData, Edge>): Boolean{
            val a1 = a.data.seg.p1
            val a2 = a.data.seg.p2
            val b1 = b.data.seg.p1
            val b2 = b.data.seg.p2
            return a1 == b1 || a1 == b2 || a2 == b1 || a2 == b2
        }
        val edgeGroups = floodfill(edges, {a, b -> adjace(a, b)})
        for (edgeGroup in edgeGroups){
            val newNode = adjacencies.addNode(node.node)
            for (edge in edgeGroup){
                adjacencies.addEdge(newNode, edge.otherNode(node), edge.data)
            }
        }
        adjacencies.removeNode(node)
    }

    fun getNodes(): Collection<NodeId> {
        return adjacencies.getNodes().map({
            node ->
            NodeId(node)
        })
    }

    class Edge (segment: Segment){
        val seg: Segment = segment
    }

    private fun sliceEdges(chopMeUp : Collection<Graph.Edge<NodeData, Edge>>, knife : Polygon): Map<Segment, Graph.Edge<NodeData, Edge>>{
        val map = hashMapOf<Segment, Graph.Edge<NodeData, Edge>>()
        for (edge in chopMeUp){
            val segs = knife.sliceSegment(edge.data.seg)
            for (seg in segs){
                map.put(seg, edge)
            }
        }
        return map
    }

    inner class NodeId(node: Graph.Node<NodeData, Edge>){
        val node = node//TODO
        fun getData(): NodeData{
            return node.node
        }
        fun getAdjacentNodes(): Collection<NodeId>{
            return node.neighbors.map({
                neighbor -> NodeId(neighbor.otherNode(node))
            })
        }
        fun getSharedEdges(other: NodeId): Collection<Segment>{
            return node.neighbors.filter{
                edge -> edge.node1 == other || edge.node2 == other
            }.map {
                edge -> edge.data.seg
            }
        }
        fun getPolygon(): Polygon{
            return getPolygon(this)
        }
    }
}


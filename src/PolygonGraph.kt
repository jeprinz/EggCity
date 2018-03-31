import javax.xml.soap.Node

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

    fun splitPolygon(node: Graph.Node<NodeData, Edge>, clipper: Polygon, n: NodeData) {//clipper is the poly were intersecting and making a new node out of
        val inClip: ArrayList<Segment> = arrayListOf()
        val inOld: HashMap<Segment, Graph.Edge<NodeData, Edge>> = hashMapOf()
        val oldPolygon: Polygon = Polygon(node.neighbors.map({edge -> edge.data.seg}))
        val mincedClipper = oldPolygon.slicePoly(clipper);
        val mincedEdgesOld = sliceEdges(node.neighbors, clipper)
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
                    (seg, e) ->
                    val otherNode = if(e.node1 != node){e.node1}else{e.node2}
                    adjacencies.addEdge(otherNode, newNode, Edge(seg))
                }
        )
        inOld.values.forEach({
            edge -> adjacencies.removeEdge(edge)
        })

        fixNode(node)

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

    fun getNodes(): Collection<Graph.Node<NodeData, Edge>> {
        return adjacencies.getNodes()
    }

    class Edge (segment: Segment){
        val seg: Segment = segment
    }

    //TODO get rid of next two FUN[]
    fun intersectSegmentWithEdges(segment: Segment, edges : Collection<Graph.Edge<NodeData, Edge>>): Graph.Edge<NodeData, Edge>? {
        edges.forEach(
                {edge -> if (intersect(edge.data.seg, segment)) {return edge}}
        )
        return null
    }

    fun sliceSegment(seg: Segment, edges : Collection<Graph.Edge<NodeData, Edge>>) : Collection<Segment>{
        val intersector = intersectSegmentWithEdges(seg, edges);
        if(intersector != null){
            val mid = intersection(seg, intersector.data.seg)
            return sliceSegment(Segment(seg.p1, mid), edges).union(sliceSegment(Segment(mid, seg.p2), edges))
        } else {
            return arrayListOf(seg)
        }

    }

    fun sliceEdges(chopMeUp : Collection<Graph.Edge<NodeData, Edge>>, knife : Polygon): Map<Segment, Graph.Edge<NodeData, Edge>>{
        val map = hashMapOf<Segment, Graph.Edge<NodeData, Edge>>()
        for (edge in chopMeUp){
            val segs = knife.sliceSegment(edge.data.seg)
            for (seg in segs){
                map.put(seg, edge)
            }
        }
        return map
    }
}


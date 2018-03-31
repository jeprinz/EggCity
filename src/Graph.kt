class Graph<N, E> {

    var nodeMap: ArrayList<Node<N, E>> = arrayListOf()
    var edgeList : ArrayList<Edge<N, E>> = arrayListOf()

    fun addNode(n : N) : Node<N,E>{
        val node = Node<N,E>(n)
        nodeMap.add(node)
        return node
    }

    fun removeNode(node : Node<N, E>) {
        for (edge in node.neighbors){
            val otherNode = edge.otherNode(node)
            otherNode.neighbors.removeAll(node.neighbors)
        }
        edgeList.removeAll(node.neighbors)
        nodeMap.remove(node)
    }

    fun removeEdge(edge: Edge<N,E>){
        edgeList.remove(edge)
        for (node in arrayListOf(edge.node1, edge.node2)){
            node.neighbors.remove(edge)
        }
    }

    fun hasNode(node : Node<N,E>) : Boolean{
        return getNodes().contains(node)
    }

    fun addEdge(n1 : Node<N,E>, n2 : Node<N,E>, data :E) {
        if (!nodeMap.contains(n1) || !nodeMap.contains(n2)) {
            throw IllegalStateException("Expected '$n1' and '$n2' nodes to exist in graph")
        }
        val edge = Edge(n1, n2, data)
        edgeList.add(edge)
        n1.neighbors.add(edge)
        n2.neighbors.add(edge)
    }

    fun getNodes() : Collection<Node<N,E>>{
        return nodeMap
    }

    data class Node<N,E>(val node: N) {

        val neighbors: ArrayList<Edge<N, E>> = arrayListOf()

        override fun equals(other: Any?): Boolean {
            if (this === other) {return true}
            else if (other?.javaClass != javaClass) return false
            else {
                return (other as Node<N, E>).node!! == node
            }
        }

        override fun toString(): String {
            return node.toString()
        }
    }

    data class Edge<N, E>(val node1: Node<N, E>, val node2: Node<N, E>, val data : E){
        fun otherNode(theOneIHave: Node<N, E>): Node<N,E>{
            if (theOneIHave == node1){
                return node2
            } else if (theOneIHave == node2){
                return node1
            } else{
                throw RuntimeException("Well fuck")
            }
        }
    }

}
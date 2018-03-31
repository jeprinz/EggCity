class Graph<N, E> {

    var nodeList : ArrayList<Node<N, E>> = arrayListOf()
    var edgeList : ArrayList<Edge<N, E>> = arrayListOf()

    fun addNode(node : Node<N, E>) {
        nodeList.add(node)
    }

    fun removeNode(node : Node<N, E>) {
        edgeList.removeAll(node.neighbors)
        nodeList.remove(node)
    }

    fun hasNode(node : N) : Boolean{
        return getNodes().contains(node)
    }

    fun addEdge(node1 : N, node2 : N, data :E) {
        val n1 = Node<N, E>(node1); val n2 = Node<N,E>(node2)
        if (!nodeList.contains(n1) || !nodeList.contains(n2)) {
            throw IllegalStateException("Expected '$node1' and '$node2' nodes to exist in graph")
        }
        val edge = Edge(n1, n2, data)
        edgeList.add(edge)
        n1.neighbors.add(edge)
        n2.neighbors.add(edge)
    }

    fun getNodes() : Collection<N>{
        return nodeList.map(
                fun(node : Node<N, E>) : N{
                    return node.node
                }
        )
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

    data class Edge<N, E>(val node1: Node<N, E>, val node2: Node<N, E>, val data : E)

}
class Graph<V> {

    var nodeList : ArrayList<Node<V>> = arrayListOf()
    var edgeList : ArrayList<Edge<V>> = arrayListOf()

    fun addNode(node : V) {
        nodeList.add(Node(node))
    }

    fun removeNode(node : V) {
        nodeList.remove(Node(node))
    }

    fun addEdge(node1 : V, node2 : V) {
        val n1 = Node(node1); val n2 = Node(node2)
        if (!nodeList.contains(n1) || !nodeList.contains(n2)) {
            throw IllegalStateException("Expected '$node1' and '$node2' nodes to exist in graph")
        }
        edgeList.add(Edge(n1, n2))
        n1.neighbors.add(n2)
        n2.neighbors.add(n1)
    }

    data class Node<V>(val node: V) {

        val neighbors: ArrayList<Node<V>> = arrayListOf()

        override fun equals(other: Any?): Boolean {
            if (this === other) {return true}
            else if (other?.javaClass != javaClass) return false
            else {
                return (other as Node<V>).node!! == node
            }
        }

        override fun toString(): String {
            return node.toString()
        }
    }

    data class Edge<V>(val node1: Node<V>, val node2: Node<V>)

}
class Graph<N, E> {

    var nodeList : ArrayList<Node<N>> = arrayListOf()
    var edgeList : ArrayList<Edge<N, E>> = arrayListOf()

    fun addNode(node : N) {
        nodeList.add(Node(node))
    }

    fun removeNode(node : N) {
        nodeList.remove(Node(node))
    }

    fun hasNode(node : N) : Boolean{
        return getNodes().contains(N)
    }

    fun addEdge(node1 : N, node2 : N, data :E) {
        val n1 = Node(node1); val n2 = Node(node2)
        if (!nodeList.contains(n1) || !nodeList.contains(n2)) {
            throw IllegalStateException("Expected '$node1' and '$node2' nodes to exist in graph")
        }
        edgeList.add(Edge(n1, n2, data))
        n1.neighbors.add(n2)
        n2.neighbors.add(n1)
    }

    fun getNodes() : Collection<N>{
        return nodeList.map(
                fun(node : Node<N>) : N{
                    return node.node
                }
        )
    }

    data class Node<N>(val node: N) {

        val neighbors: ArrayList<Node<N>> = arrayListOf()

        override fun equals(other: Any?): Boolean {
            if (this === other) {return true}
            else if (other?.javaClass != javaClass) return false
            else {
                return (other as Node<N>).node!! == node
            }
        }

        override fun toString(): String {
            return node.toString()
        }
    }

    data class Edge<N, E>(val node1: Node<N>, val node2: Node<N>, val data : E)

}
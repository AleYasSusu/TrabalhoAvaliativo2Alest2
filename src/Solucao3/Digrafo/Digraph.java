
/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                http://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                http://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1
 *  1:
 *  2: 0 3
 *  3: 5 2
 *  4: 3 2
 *  5: 4
 *  6: 9 4 8 0
 *  7: 6 9
 *  8: 6
 *  9: 11 10
 *  10: 12
 *  11: 4 12
 *  12: 9
 *
 ******************************************************************************/

package Solucao3.Digrafo;

import java.io.Serializable;

/**
 * The {@code Digraph} class represents a directed graph of vertices named 0
 * through <em>V</em> - 1. It supports the following two primary operations: add
 * an edge to the digraph, iterate over all of the vertices adjacent from a
 * given vertex. Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an adjacency-lists representation, which is a
 * vertex-indexed array of {@link Bag} objects. All operations take constant
 * time (in the worst case) except iterating over the vertices adjacent from a
 * given vertex, which takes time proportional to the number of such vertices.
 * <p>
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Marcelo Cohen
 */

public class Digraph implements Serializable {

	private final int V; // number of vertices in this digraph
	private Bag<Integer>[] adj; // adj[v] = adjacency list for vertex v
	private int[] indegree; // indegree[v] = indegree of vertex v

	/**
	 * Initializes an empty digraph with <em>V</em> vertices.
	 *
	 * @param V
	 *            the number of vertices
	 * @throws IllegalArgumentException
	 *             if {@code V < 0}
	 */
	public Digraph(int V) {
		if (V < 0)
			throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		indegree = new int[V];
		adj = (Bag<Integer>[]) new Bag[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new Bag<>();
		}
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
	}

	/**
	 * Adds the directed edge v→w to this digraph.
	 *
	 * @param v
	 *            the tail vertex
	 * @param w
	 *            the head vertex
	 * @throws IllegalArgumentException
	 *             unless both {@code 0 <= v < V} and {@code 0 <= w < V}
	 */
	public void addEdge(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		adj[v].add(w);
		indegree[w]++;
	}

	/**
	 * Returns the vertices adjacent from vertex {@code v} in this digraph.
	 *
	 * @param v
	 *            the vertex
	 * @return the vertices adjacent from vertex {@code v} in this digraph, as
	 *         an iterable
	 * @throws IllegalArgumentException
	 *             unless {@code 0 <= v < V}
	 */
	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
	 * Returns the number of directed edges incident to vertex {@code v}. This
	 * is known as the <em>indegree</em> of vertex {@code v}.
	 *
	 * @param v
	 *            the vertex
	 * @return the indegree of vertex {@code v}
	 * @throws IllegalArgumentException
	 *             unless {@code 0 <= v < V}
	 */
	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}


}

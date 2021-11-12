// Este é um exemplo simples de implementação de grafo representado por lista
// de adjacências

import java.util.List;
import java.util.ArrayList;

public class Grafo {
	List<Vertice> vertices;
	List<Aresta> arestas;

	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
	}

	Aresta addAresta(Vertice origem, Vertice destino) {
		Aresta e = new Aresta(origem, destino);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	

	

	 public String toString() {
	        String r = "";
	        for (Vertice u : vertices) {
	            r += u.getNome() + " -> ";
	            for (Aresta e : u.getAdj()) {
	                Vertice v = e.getDestino();
	                r += v.getNome() + ", ";
	            }
	            r += "\n";
	        }
	        return r;
	    }

}

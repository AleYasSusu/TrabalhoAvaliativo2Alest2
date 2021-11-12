

public class Aresta {
   private Vertice origem;
   private Vertice destino;

    public Vertice getOrigem() {
	return origem;
}

public void setOrigem(Vertice origem) {
	this.origem = origem;
}

public Vertice getDestino() {
	return destino;
}

public void setDestino(Vertice destino) {
	this.destino = destino;
}

	Aresta(Vertice origem, Vertice destino) {
        this.origem = origem;
        this.destino = destino;
    }

}

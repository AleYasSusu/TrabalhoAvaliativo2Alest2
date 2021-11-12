import java.util.ArrayList;
import java.util.List;

public class Vertice {
       private String nome;
        private int time;
       private List<Aresta> adj;

        public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public List<Aresta> getAdj() {
		return adj;
	}

	public void setAdj(List<Aresta> adj) {
		this.adj = adj;
	}

		Vertice(String nome, int time) {
        	this.time = time;
            this.nome = nome;
            this.adj = new ArrayList<Aresta>();
        }

        void addAdj(Aresta e) {
            adj.add(e);
        }
    }
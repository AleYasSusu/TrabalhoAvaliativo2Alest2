package Solucao2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo implements Serializable {
    private Map<Vertice, List<Vertice>> listaAdjacentes;

    Grafo() {
        this.listaAdjacentes = new HashMap<>();
    }

    void addVertice(String tarefa) {
        listaAdjacentes.putIfAbsent(new Vertice(tarefa), new ArrayList<>());
    }

    void removeVertice(String tarefa) {
        Vertice v = new Vertice(tarefa);
        listaAdjacentes.values().forEach(e -> e.remove(v));
        listaAdjacentes.remove(new Vertice(tarefa));
    }

    void addAresta(String tarefa1, String tarefa2) {
        Vertice v1 = new Vertice(tarefa1);
        Vertice v2 = new Vertice(tarefa2);
        listaAdjacentes.get(v1).add(v2);
    }

    void removeAresta(String tarefa1, String tarefa2) {
        Vertice v1 = new Vertice(tarefa1);
        Vertice v2 = new Vertice(tarefa2);
        List<Vertice> adjacentesV1 = listaAdjacentes.get(v1);
        if (adjacentesV1 != null) adjacentesV1.remove(v2);
    }

    List<Vertice> getVerticesAdj(String tarefa) {
        return listaAdjacentes.get(new Vertice(tarefa));
    }

    public Grafo copia()
    {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);

            return (Grafo) in.readObject();

        }
        catch (Exception e){
            System.err.println("Erro de cópia através de serialização: " +e);
            return null;
        }

    }

    class Vertice implements Serializable {
        String tarefa;

        Vertice(String tarefa) {
            this.tarefa = tarefa;
        }

        @Override
        public int hashCode() {
            final int primo = 31;
            int resultado = 1;
            resultado = primo * resultado + getInstanciaGrafo().hashCode();
            resultado = primo * resultado + ((tarefa == null) ? 0 : tarefa.hashCode());
            return resultado;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vertice other = (Vertice) obj;
            if (!getInstanciaGrafo().equals(other.getInstanciaGrafo()))
                return false;
            if (tarefa == null) {
                if (other.tarefa != null)
                    return false;
            } else if (!tarefa.equals(other.tarefa))
                return false;
            return true;
        }

        private Grafo getInstanciaGrafo() {
            return Grafo.this;
        }

    }
}

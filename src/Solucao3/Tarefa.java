package Solucao3;

import java.io.Serializable;
import java.util.Comparator;

public class Tarefa implements Serializable {

    public String tarefa;
    public Integer tempo;
    public Integer indegree;

    public Tarefa(String tarefa_bruto){

        String[] dados_tarefa = tarefa_bruto.split("_");

        this.tarefa = dados_tarefa[0];
        this.tempo = Integer.parseInt(dados_tarefa[1]);
        this.indegree = 0;

    }

    @Override
    public int hashCode() {
        return tarefa.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tarefa other = (Tarefa) obj;
        if (!tarefa.equals(other.tarefa))
            return false;
        return true;
    }

    static class SortAlfabetico implements Comparator<Tarefa> {
        public int compare(Tarefa a, Tarefa b)
        {
            return a.tarefa.compareToIgnoreCase(b.tarefa);
        }
    }

}

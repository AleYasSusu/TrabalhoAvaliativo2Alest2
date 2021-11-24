package Solucao1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {

    static Grafo grafo;
    static int tempo_percorrido;
    static int quantidade_minions;

    static ArrayList<String> listaVertice;
    static ArrayList<String> listaPrioridade;
    static ArrayList<String> listaEmProgresso;

    public static void main(String[] args){

        long startTime = System.nanoTime();

        String nome_arquivo = "oito_enunciado";

        if(args.length>0){
            nome_arquivo = args[0];
            quantidade_minions = Integer.parseInt(args[1]);
        }

        grafo = new Grafo();
        listaVertice = new ArrayList<>();
        listaPrioridade = new ArrayList<>();
        listaEmProgresso = new ArrayList<>();

        String[] elemento_linha;
        Path path1 = Paths.get(nome_arquivo +".txt");

        try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.equals("}")) {

                elemento_linha = line.split(" -> ");
                grafo.addVertice(elemento_linha[0]);
                grafo.addVertice(elemento_linha[1]);
                grafo.addAresta(elemento_linha[0],elemento_linha[1]);

                if(!listaVertice.contains(elemento_linha[0])) listaVertice.add(elemento_linha[0]);
                if(!listaVertice.contains(elemento_linha[1])) listaVertice.add(elemento_linha[1]);
            }
        }
        catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: ", e);
        }

        contarMinions();
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("\nTempo de execução em segundos " + timeElapsed / 1000000000.0);

    }

    public static void contarMinions() {

        ArrayList<String> listaAuxiliar = (ArrayList<String>) listaVertice.clone();
        Grafo grafoAuxiliar = grafo.copia();

        int tempo_ideal = Integer.MAX_VALUE;
        int minions_ideal = Integer.MAX_VALUE;
        quantidade_minions = listaVertice.size();

        for (int i = quantidade_minions;i>0;i--){

            treinarMinions();

            if(quantidade_minions<minions_ideal && tempo_percorrido<=tempo_ideal){
                minions_ideal = quantidade_minions;
                tempo_ideal = tempo_percorrido;
            }

            quantidade_minions--;
            tempo_percorrido = 0;
            listaVertice = (ArrayList<String>) listaAuxiliar.clone();
            grafo = grafoAuxiliar.copia();

        }

        System.out.println("Quantidade Minions: " +minions_ideal +"\n Tempo Percorrido: " +tempo_ideal);

    }

    public static void treinarMinions() {

        if (listaVertice.isEmpty()) return;

        Set<String> listaAuxiliar = new LinkedHashSet<>();
        ArrayList<String> listaProximasTarefas = new ArrayList<>(listaVertice);

        for (String label : listaVertice) {
            listaAuxiliar.addAll(BuscaPorTarefasIniciais(grafo, label));
        }

        listaProximasTarefas.removeAll(listaAuxiliar);
        listaProximasTarefas.sort(String::compareToIgnoreCase);

        int k = 0;
        for (String tarefa : listaPrioridade) {
            if (listaProximasTarefas.contains(tarefa)) {
                listaProximasTarefas.remove(tarefa);
                listaProximasTarefas.add(k,tarefa);
                k++;
            }
        }

        listaPrioridade.clear();

        k = 0;
        for (String tarefa : listaEmProgresso) {
            if (listaProximasTarefas.contains(tarefa)) {
                listaProximasTarefas.remove(tarefa);
                listaProximasTarefas.add(k,tarefa);
                k++;
            }
        }

        listaEmProgresso.clear();

        ArrayList<String> tarefasIniciadas = new ArrayList<>();
        String[] splitAux1 = listaProximasTarefas.get(0).split("_");
        int menorTempo = Integer.parseInt(splitAux1[1]);

        for (int i=0;i<quantidade_minions;i++){

            if(listaProximasTarefas.isEmpty()) break;

            tarefasIniciadas.add(listaProximasTarefas.get(0));

            String[] splitAux2 = listaProximasTarefas.get(0).split("_");
            if (menorTempo > Integer.parseInt(splitAux2[1])) menorTempo = Integer.parseInt(splitAux2[1]);
            listaProximasTarefas.remove(0);
        }

        tempo_percorrido += menorTempo;

        for (String tarefa:tarefasIniciadas) {
            String[] splitAux3 = tarefa.split("_");
            int novoTempo = Integer.parseInt(splitAux3[1]) - menorTempo;
            String novaTarefa = splitAux3[0] + "_" + novoTempo;

            if(novoTempo>0){
                grafo.addVertice(novaTarefa);
                listaVertice.add(novaTarefa);
                listaEmProgresso.add(novaTarefa);

                List<Grafo.Vertice> listaAuxiliar1 = grafo.getVerticesAdj(tarefa);
                List<String> listaAuxiliar2 = new ArrayList<>();
                for(Grafo.Vertice tarefaAdjacente: listaAuxiliar1){
                    listaAuxiliar2.add(tarefaAdjacente.tarefa);
                }


                for (String tarefaAdjacente : listaAuxiliar2) {
                    grafo.addAresta(novaTarefa, tarefaAdjacente);
                    grafo.removeAresta(tarefa, tarefaAdjacente);
                }

            }
            else{

                List<Grafo.Vertice> listaAuxiliar1 = grafo.getVerticesAdj(tarefa);
                List<String> listaAuxiliar2 = new ArrayList<>();

                for(Grafo.Vertice auxTarefa: listaAuxiliar1){
                    listaAuxiliar2.add(auxTarefa.tarefa);
                }

                if(!listaAuxiliar2.isEmpty()) {
                    listaAuxiliar2.sort(String::compareToIgnoreCase);
                    listaPrioridade.add(listaAuxiliar2.get(0));
                }
            }


            grafo.removeVertice(tarefa);
            listaVertice.remove(tarefa);

        }

        treinarMinions();

    }

    public static Set<String> BuscaPorTarefasIniciais(Grafo grafo, String tarefa) {
        Set<String> listaTarefasVisitadas = new LinkedHashSet<>();
        ArrayList<String> listaAuxiliarDuplicatas = new ArrayList<>();
        Stack<String> pilha = new Stack<>();
        pilha.push(tarefa);
        while (!pilha.isEmpty()) {
            String vertex = pilha.pop();
            if (!listaTarefasVisitadas.contains(vertex)) {
                listaTarefasVisitadas.add(vertex);
                listaAuxiliarDuplicatas.add(vertex);
                for (Grafo.Vertice v : grafo.getVerticesAdj(vertex)) {
                    pilha.push(v.tarefa);
                }
            }
        }

        if(Collections.frequency(listaAuxiliarDuplicatas,tarefa)==1) listaTarefasVisitadas.remove(tarefa);
        return listaTarefasVisitadas;
    }


}

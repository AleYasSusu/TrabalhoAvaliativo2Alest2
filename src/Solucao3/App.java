package Solucao3;

import Solucao3.Digrafo.Digraph;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {

    static Digraph grafo;
    static Set<Tarefa> listaVertice = new LinkedHashSet<>();

    static int tempo_percorrido;
    static int quantidade_minions;

    static ArrayList<Tarefa> listaEmProgresso = new ArrayList<>();

    //Mapa Bidirecional
    static Map<Tarefa,Integer> mapTarefaVertice = new HashMap<>();
    static Map<Integer,Tarefa> mapVerticeTarefa = new HashMap<>();

    public static void main(String[] args){
        long startTime = System.nanoTime();

        //Ambas variáveis devem ser alteradas de acordo com o caso de teste para operação correta
        //Exemplo: se o arquivo de texto for vinte_e_cinco.txt, quantidade_vertice = 25
        String nome_arquivo = "oito_enunciado";
        int quantidade_vertice = 8;

        if(args.length>0){
            nome_arquivo = args[0];
            quantidade_vertice = Integer.parseInt(args[1]);
        }

        grafo = new Digraph(quantidade_vertice);

        Path path1 = Paths.get(nome_arquivo +".txt");
        lerArquivo(path1);

        contarMinions(quantidade_vertice);

        long endTime = System.nanoTime();

        System.out.println("\nTempo de execução em segundos " + (endTime - startTime) / 1000000000.0);
        System.exit(0);

    }

    public static void contarMinions(int tamanhoLista) {

        Digraph grafoAuxiliar = (Digraph) copia(grafo);
        Set<String> listaAuxiliar = (Set<String>) copia(listaVertice);
        Map<Tarefa,Integer> mapTVauxiliar = (Map<Tarefa, Integer>) copia(mapTarefaVertice);
        Map<Integer,Tarefa> mapVTauxiliar = (Map<Integer, Tarefa>) copia(mapVerticeTarefa);

        int minionMIN = 0;
        int minionMAX = tamanhoLista;

        int minions_ideal = tamanhoLista;
        quantidade_minions = tamanhoLista;

        treinarMinions();

        int tempo_ideal = tempo_percorrido;

        tempo_percorrido = 0;
        grafo = (Digraph) copia(grafoAuxiliar);
        listaVertice = (Set<Tarefa>) copia(listaAuxiliar);
        mapVerticeTarefa = (Map<Integer, Tarefa>) copia(mapVTauxiliar);
        mapTarefaVertice = (Map<Tarefa, Integer>) copia(mapTVauxiliar);

        while(true){

            treinarMinions();

            if(tempo_percorrido>tempo_ideal) minionMIN = quantidade_minions;
            else if(tempo_percorrido==tempo_ideal && quantidade_minions<minions_ideal){
                minionMAX = quantidade_minions;
                minions_ideal = minionMAX;
            }

            quantidade_minions = (minionMIN + minionMAX)/2;

            if (quantidade_minions==minionMIN) break;

            tempo_percorrido = 0;
            grafo = (Digraph) copia(grafoAuxiliar);
            listaVertice = (Set<Tarefa>) copia(listaAuxiliar);
            mapVerticeTarefa = (Map<Integer, Tarefa>) copia(mapVTauxiliar);
            mapTarefaVertice = (Map<Tarefa, Integer>) copia(mapTVauxiliar);

        }

        System.out.println("Quantidade de Minions Ideal: " +minions_ideal +"\nTempo de Treinamento: " +tempo_ideal);

    }

    public static void treinarMinions(){

        ArrayList<Tarefa> listaProximasTarefas = new ArrayList<>();

        listaVertice.forEach(vertice -> {
            if(grafo.indegree(mapTarefaVertice.get(vertice))==0) listaProximasTarefas.add(vertice);
        });

        listaProximasTarefas.sort(new Tarefa.SortAlfabetico());

        treinarMinions(listaProximasTarefas);

    }

    public static void treinarMinions(ArrayList<Tarefa> listaProximasTarefas) {

        listaProximasTarefas.sort(new Tarefa.SortAlfabetico());

        listaProximasTarefas.addAll(0,listaEmProgresso);
        listaEmProgresso.clear();

        if(listaProximasTarefas.isEmpty()) return;

        ArrayList<Tarefa> tarefasIniciadas = new ArrayList<>();

        int menorTempo = Integer.MAX_VALUE;

        for (int i=0;i<quantidade_minions;i++){

            if(listaProximasTarefas.isEmpty()) break;

            int tempoTarefa = listaProximasTarefas.get(0).tempo;

            if (menorTempo > tempoTarefa) menorTempo = tempoTarefa;

            tarefasIniciadas.add(listaProximasTarefas.get(0));
            listaProximasTarefas.remove(0);

        }

        tempo_percorrido += menorTempo;

        for (Tarefa tarefa:tarefasIniciadas) {

            tarefa.tempo = tarefa.tempo - menorTempo;

            if(tarefa.tempo>0) listaEmProgresso.add(tarefa);

            else{

                int vertice = mapTarefaVertice.get(tarefa);

                grafo.adj(vertice).forEach(verticeAdjacente -> {

                    mapVerticeTarefa.get(verticeAdjacente).indegree--;
                    if(mapVerticeTarefa.get(verticeAdjacente).indegree==0) listaProximasTarefas.add(mapVerticeTarefa.get(verticeAdjacente));
                });
            }
        }

        treinarMinions(listaProximasTarefas);

    }

    public static void lerArquivo(Path path1){

        String[] elemento_linha;

        try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.equals("}")) {

                elemento_linha = line.split(" -> ");

                Tarefa tarefa1 = new Tarefa(elemento_linha[0]);
                Tarefa tarefa2 = new Tarefa(elemento_linha[1]);

                listaVertice.add(tarefa1);
                listaVertice.add(tarefa2);

            }
        }
        catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: " +e);
            System.exit(0);
        }

        int i = 0;
        for (Tarefa vertice: listaVertice) {
            mapTarefaVertice.put(vertice,i);
            mapVerticeTarefa.put(i,vertice);
            i++;
        }

        try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.equals("}")) {

                elemento_linha = line.split(" -> ");

                int vertice1 = mapTarefaVertice.get(new Tarefa(elemento_linha[0]));
                int vertice2 = mapTarefaVertice.get(new Tarefa(elemento_linha[1]));

                grafo.addEdge(vertice1,vertice2);
                mapVerticeTarefa.get(vertice2).indegree++;

            }
        }
        catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: " +e);
            System.exit(0);
        }

    }

    public static Object copia(Object object)
    {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(object);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);

            return in.readObject();

        }
        catch (Exception e){
            System.err.println("Erro de cópia através de serialização: " +e);
            return null;
        }

    }

}

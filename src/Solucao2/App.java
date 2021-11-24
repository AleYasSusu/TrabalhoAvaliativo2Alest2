package Solucao2;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {

    static Grafo grafo;
    static int tempo_percorrido;
    static int quantidade_minions;

    static ArrayList<String> listaVertice = new ArrayList<>();
    static ArrayList<String> listaPrioridade = new ArrayList<>();
    static ArrayList<String> listaEmProgresso = new ArrayList<>();
    static Map<String,Integer> mapVerticePai = new HashMap<>();;

    public static void main(String[] args){

        long startTime = System.nanoTime();
        String[] elemento_linha;

        String nome_arquivo = "oito_enunciado";

        if(args.length>0){
            nome_arquivo = args[0];
            quantidade_minions = Integer.parseInt(args[1]);
        }

        grafo = new Grafo();

        Set<String> listaAuxiliar = new LinkedHashSet<>();

        Path path1 = Paths.get(nome_arquivo +".txt");

        try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.equals("}")) {

                elemento_linha = line.split(" -> ");
                grafo.addVertice(elemento_linha[0]);
                grafo.addVertice(elemento_linha[1]);
                grafo.addAresta(elemento_linha[0],elemento_linha[1]);

                if(mapVerticePai.containsKey(elemento_linha[1])){
                   Integer novoFilho = mapVerticePai.get(elemento_linha[1]) + 1;
                   mapVerticePai.replace(elemento_linha[1],novoFilho);
                }
                else{
                    mapVerticePai.put(elemento_linha[1],1);
                }

                listaAuxiliar.add(elemento_linha[0]);
                listaAuxiliar.add(elemento_linha[1]);

            }
        }
        catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: ", e);
        }

        int minionMAX = listaAuxiliar.size();
        listaAuxiliar.removeAll(mapVerticePai.keySet());
        listaVertice.addAll(listaAuxiliar);
        listaVertice.sort(String::compareToIgnoreCase);


        contarMinions(minionMAX);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("\nTempo de execução em segundos " + timeElapsed / 1000000000.0);

    }

    public static void contarMinions(int tamanhoLista) {

        ArrayList<String> listaAuxiliar = (ArrayList<String>) listaVertice.clone();
        Map<String,Integer> mapAuxiliar = copia(mapVerticePai);
        Grafo grafoAuxiliar = grafo.copia();

        int minionMAX = tamanhoLista;
        int minionMIN = 0;

        int minions_ideal = tamanhoLista;
        quantidade_minions = tamanhoLista;

        treinarMinions();

        int tempo_ideal = tempo_percorrido;

        listaVertice = (ArrayList<String>) listaAuxiliar.clone();
        mapVerticePai = copia(mapAuxiliar);
        grafo = grafoAuxiliar.copia();
        tempo_percorrido = 0;

        boolean isSmallest = false;
        while(!isSmallest){

            treinarMinions();

            if(tempo_percorrido>tempo_ideal) minionMIN = quantidade_minions;
            else if(tempo_percorrido==tempo_ideal && quantidade_minions<minions_ideal){
                minionMAX = quantidade_minions;
                minions_ideal = minionMAX;
            }

            quantidade_minions = (minionMIN + minionMAX)/2;

            if (((minionMIN + minionMAX)/2)==minionMIN){

                isSmallest = true;

            }

            listaVertice = (ArrayList<String>) listaAuxiliar.clone();
            mapVerticePai = copia(mapAuxiliar);
            grafo = grafoAuxiliar.copia();
            tempo_percorrido = 0;

        }

        System.out.println("Quantidade Minions: " +minions_ideal +"\nTempo Percorrido: " +tempo_ideal);

    }

    public static void treinarMinions(){

        ArrayList<String> listaProximasTarefas = new ArrayList<>(listaVertice);

        treinarMinions(listaProximasTarefas);

    }

    public static void treinarMinions(ArrayList<String> listaProximasTarefas) {

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

        if(listaProximasTarefas.isEmpty()) return;

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

                listaProximasTarefas.add(novaTarefa);

            }
            else{

                List<Grafo.Vertice> listaAuxiliar1 = grafo.getVerticesAdj(tarefa);
                List<String> listaAuxiliar2 = new ArrayList<>();

                for(Grafo.Vertice auxTarefa: listaAuxiliar1){
                    listaAuxiliar2.add(auxTarefa.tarefa);

                    int novoFilho = mapVerticePai.get(auxTarefa.tarefa) - 1;
                    mapVerticePai.replace(auxTarefa.tarefa,novoFilho);

                    if(mapVerticePai.get(auxTarefa.tarefa)==0){
                        listaProximasTarefas.add(auxTarefa.tarefa);
                        mapVerticePai.remove(auxTarefa.tarefa);
                    }

                }

                if(!listaAuxiliar2.isEmpty()) {
                    listaAuxiliar2.sort(String::compareToIgnoreCase);
                    listaPrioridade.add(listaAuxiliar2.get(0));
                }
            }


            grafo.removeVertice(tarefa);
            listaVertice.remove(tarefa);

        }


        treinarMinions(listaProximasTarefas);

    }

    public static Map<String,Integer> copia(Object object)
    {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(object);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);

            return (Map<String, Integer>) in.readObject();

        }
        catch (Exception e){
            System.err.println("Erro de cópia através de serialização: " +e);
            return null;
        }

    }


}

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Leitura {

	private final String fileName;
	private Grafo dependenciesTree;
	private Processor[] processors;
	

	public Leitura(String fileName) {
		dependenciesTree = new Grafo();
		this.fileName = fileName;
		readAndFillDependencies();
	}

	Grafo gr = new Grafo();

	private void readAndFillDependencies() {
		String fileName = ("oito_enunciado");
		Path path = Paths.get(fileName);
		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF8"))){
            String line = reader.readLine();

            processors = new Processor[2];

            for(int i = 0; i < processors.length; i++){
                processors[i] = new Processor();
            }
            
            while((line = reader.readLine()) != null && !line.equals("}")){
                
                String[] tokens = line.split(" -> ");
                String[] task1 = tokens[0].split("_");
                String[] task2 = tokens[1].split("_");
      
                Vertice dependency = new Vertice(task1[0], Integer.parseInt(task1[1]));
                Vertice dependent = new Vertice(task2[1], Integer.parseInt(task2[1]));
                
                dependenciesTree.addAresta(dependency, dependent);
                
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
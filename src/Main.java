import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Grafo gr = new Grafo();
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		System.out.println("Digite o nome do arquivo de teste: ");

		String testCase = in.nextLine();

		Leitura tm = new Leitura(testCase);
		System.out.println(gr.toString());
		
	}

	// test client

}

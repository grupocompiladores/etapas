package laxer;

/**
 *
 * @author gustavo
 */
public class Compilador {

    public static void main(String [] entrada) {

        Lexico lexer = new Lexico("arquivos/programa1.txt");
        Sintatico parser = new Sintatico(lexer);

        parser.prodPrograma(); // producao inicial

        parser.fechaArquivos();
        System.out.println("Compilacao de Programa Realizada!");
    }
}

package parser;

/**
 *
 * @author gustavo
 */
public class Compilador {

    public static void main(String [] entrada) {

        Lexico lexer = new Lexico("arquivos/programa3");
        Sintatico parser = new Sintatico(lexer);

        parser.Programa(); // producao inicial

        parser.fechaArquivos();
        System.out.println("Compilacao de Programa Realizada!");
    }
}

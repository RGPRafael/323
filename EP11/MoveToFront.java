/****************************************************************
    Nome: RAFAEL GONLÇALVES 
    NUSP: 9009600

    Ao preencher esse cabeçalho com o meu nome e o meu número USP,
    declaro que todas as partes originais desse exercício programa (EP)
    foram desenvolvidas e implementadas por mim e que portanto não 
    constituem desonestidade acadêmica ou plágio.
    Declaro também que sou responsável por todas as cópias desse
    programa e que não distribui ou facilitei a sua distribuição.
    Estou ciente que os casos de plágio e desonestidade acadêmica
    serão tratados segundo os critérios divulgados na página da 
    disciplina.
    Entendo que EPs sem assinatura devem receber nota zero e, ainda
    assim, poderão ser punidos por desonestidade acadêmica.

    Abaixo descreva qualquer ajuda que você recebeu para fazer este
    EP.  Inclua qualquer ajuda recebida por pessoas (inclusive
    monitoras e colegas). Com exceção de material de MAC0323, caso
    você tenha utilizado alguma informação, trecho de código,...
    indique esse fato abaixo para que o seu programa não seja
    considerado plágio ou irregular.

    Exemplo:

        A monitora me explicou que eu devia utilizar a função xyz().

        O meu método xyz() foi baseada na descrição encontrada na 
        página https://www.ime.usp.br/~pf/algoritmos/aulas/enumeracao.html.

    Descrição de ajuda ou indicação de fonte:



    Se for o caso, descreva a seguir 'bugs' e limitações do seu programa:

****************************************************************/
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/*
How should i read the binary input in encode()? 
    The input is a sequence of extended ASCII characters (0x00 to 0xFF). 
    You should read them in one character at a time using BinaryStdIn.readChar() until BinaryStdIn.isEmpty().

How should i read the binary input in decode()? 
    Same as encode(). 
*/

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    
    /*Move-to-front encoding. Your task is to maintain an ordered sequence of the 256 extended ASCII characters. Initialize the sequence 
    by making the ith character in the sequence equal to the ith extended ASCII character. Now, read each 8-bit character c from standard input, 
    one at a time; output the 8-bit index in the sequence where c appears; and move c to the front. 
    */
    public static void encode(){

        Alphabet alpha = new Alphabet();
        int R = alpha.radix();
        char[] list = new char[R];
        for (int i = 0; i < R; i++) list[i] = (char)( i & 0xff );
        
        while(!BinaryStdIn.isEmpty()){
            int end = 0;
            char c = BinaryStdIn.readChar();
            for(int i = 0; i < R; i++){
                if(list[i] == c){
                    BinaryStdOut.write((char)i);
                    end = i;
                }
            }
            //MOVE TO FRONT:
            char copy = list[end];
            for(int i = end; i > 0 ; i--)list[i] = list[i-1];
            list[0] = copy;
        } 
        BinaryStdOut.close();

        
        /*for (int i = 0; i < N; i++)
            if (alpha.contains(s.charAt(i)))
               count[alpha.toIndex(s.charAt(i))]++;
        for (int c = 0; c < R; c++)
            StdOut.println(alpha.toChar(c) + " " + count[c]);
        */
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        Alphabet alpha = new Alphabet();
        int R = alpha.radix();
        char[] list = new char[R];
        for (int i = 0; i < R; i++) list[i] = (char)( i & 0xff );
        while(!BinaryStdIn.isEmpty()){
            int end = 0;
            char c = BinaryStdIn.readChar();
            int cc = ( int ) c;
            BinaryStdOut.write(list[cc]);
            end = cc;
            //MOVE TO FRONT:
            char copy = list[end];
            for(int i = end; i > 0 ; i--)list[i] = list[i-1];
            list[0] = copy;
        } 
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args){
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");

    }

}
/****************************************************************
    Nome: Rafael Gonçalves 
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
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Quick;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform(){
        String imput =  new String();
        imput =  BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(imput);
        int n = csa.length();
        //StdOut.println(n);
        for(int i = 0; i < n; i++){
            if(csa.index(i) == 0)
                BinaryStdOut.write(i);
                //StdOut.println(i);
        }
        for(int i = 0; i < n; i++){
            int j  = csa.index(i); 
            char c = imput.charAt( (j + n-1) % n );
            //StdOut.print(c + "");
            BinaryStdOut.write(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform(){
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        
        int N = s.length(),  R = 256;
        int[] count = new int[R+1];
        char[] aux = new char[N];
        int[] next = new int[N];
        for(int i = 0;i < N;i++)
            count[s.charAt(i)+1]++;
        for(int r = 0;r < R;r++)
            count[r+1] += count[r];
        for(int i = 0;i < N;i++) {
            int tmp = count[s.charAt(i)]++;
            aux[tmp] = s.charAt(i);
            next[tmp] = i;
        }
        
        for(int i = 0;i < N;i++){
            BinaryStdOut.write(aux[first]);
            first = next[first];
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
        BinaryStdOut.flush();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");

    }

}


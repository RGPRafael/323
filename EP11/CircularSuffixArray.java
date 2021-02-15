/****************************************************************
    Nome: Rafael Gonlçalves
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
//package edu.princeton.cs.algs4;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.SuffixArray;

public class CircularSuffixArray{
    private final int n;         // number of characters in text
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null) throw new java.lang.NullPointerException();
        SuffixArray suffix = new SuffixArray(s);
        this.n = s.length();
        this.index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = suffix.index(i); 
    }

    // length of s
    public int length(){
        return this.n;
    }

    // returns index of ith sorted suffix
    public int index(int i){
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args){
        String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        CircularSuffixArray csuffix = new CircularSuffixArray(s);
        StdOut.println("  "+ "i" + "   " + "index[i]");
        for (int i = 0; i < s.length(); i++) {
            int index = csuffix.index(i);
            StdOut.printf("%3d %5d\n", i, index);
        }

    }

}

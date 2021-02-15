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
        Há warnings suprimidos.

****************************************************************/

import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;


public class PointST<Value> {

    // construct an empty symbol table of points
    private RedBlackBST<Point2D,Value> st;
    //private RectHV rect;    // 

    public PointST(){
        st = new RedBlackBST<Point2D,Value>();
    }

    // is the symbol table empty? 
    public boolean isEmpty(){
        return st.isEmpty();
    }

    // number of points
    public int size(){
    	if (st == null) return 0;
        return st.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val){
    	if (p == null) throw new IllegalArgumentException("calls put() with a null key");
        st.put(p,val);
    }

    // value associated with point p 
    public Value get(Point2D p){
    	if (p == null) throw new IllegalArgumentException("argument to get() is null");
        return st.get(p);
    }

    // does the symbol table contain point p? 
    public boolean contains(Point2D p){
        return st.get(p) != null;
    }

    // all points in the symbol table 
    public Iterable<Point2D> points(){
    	return st.keys();
    }

    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect){
    	Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D s : points()){
            //StdOut.println(s + " " + st.get(s));    	
            if(rect.contains(s)){
            	queue.enqueue(s); 
            }
        }
        return queue;

    } 


    //a nearest neighbor of point p; null if the symbol table is empty 
    public Point2D nearest(Point2D p){
        if (p == null)return null;
        Point2D a = st.max();
        double n  = a.distanceSquaredTo(p);
        if( n == 0){
            a = st.min();
            n = a.distanceSquaredTo(p);
        }
        for (Point2D s : points()){
            if( n > s.distanceSquaredTo(p)){
                n = s.distanceSquaredTo(p);
                a = s;
            }
        }
        return a; 

    }

    // unit testing (required)
    public static void main(String[] args){
        PointST<Integer> brute = new PointST<Integer>();
        int i = 0;
        double [] x = { 0, 1, 2 , 5, 7, 2 ,4 , 1, 5, 8, 9 };
        double [] y = { 2, 9, 3 , 4, 4, 1 ,0 , 3, 1, 4, 1 };

        while (i <x.length) {
            double x0 = x[i];
            double y0 = y[i];
            Point2D p = new Point2D(x0, y0);
            brute.put(p, i);
            i++;
        }

        StdOut.println("Size :" + " "  + brute.size());
        StdOut.println("isEmpty :"+ " " + brute.isEmpty());
        StdOut.println("contains (2.0, 3.0): " + brute.contains(new Point2D(2,3)) );
        StdOut.println("contains (0.0, 0.0): " + brute.contains(new Point2D(0,0)) );

        RectHV rect = new RectHV(0,0,4,4);
        StdOut.println("all points that are inside the rectangle (0,0,4,4) (or on the boundary): " );
        
        for (Point2D p : brute.range(rect))
            StdOut.println("Point: " + p );
        
        StdOut.println();
        StdOut.println("all points in the symbol table :");
        StdOut.println();
        
        for (Point2D s : brute.points())
            StdOut.println("Point: " + s + " " + brute.get(s));
        
        StdOut.println();
        
        StdOut.println("a nearest neighbor of point p : (1,4)");
        Point2D p1 = new Point2D(1, 4);
        StdOut.println("Point:" + " " + brute.nearest(p1));
    }

}

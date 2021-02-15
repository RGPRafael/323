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

public class KdTreeST<Value> {

    private Node root;     // root of the BST
    private int n;

    private class Node {
        private Point2D p;      // the point
        private Value value;    // the symbol table maps the point to this value
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree



        public Node(Point2D key, Value val) {
            this.p = key;
            this.value = val;
        }        
    }   

    // construct an empty symbol table of points 
    
    public KdTreeST(){
        n = 0;
    }

    // is the symbol table empty? 
    public boolean isEmpty(){
        return size() == 0;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return n;
    }     

    // number of points
    public int size(){
        return size(root);
    }

    // associate the value val with point p
    public void put(Point2D p, Value val){
        if (p == null) throw new IllegalArgumentException("calls put() with a null Point2D");
        if (val == null) {
            return;
        }
        root = put(root, p, val, 0);
    }

    private Node put(Node x, Point2D key, Value val, int axis) {
        

        if (x == null){
            n ++;
            return new Node(key, val);
        }
        int cmp;
        Double p;
        Point2D p0 = x.p;    

        if( p0.equals(key) == true){
            x.value = val;
        }

        if (axis == 0 ){
            p = x.p.x();
            Double k = key.x(); 
            cmp = k.compareTo(p);
            axis = 1;

            if (cmp < 0)      x.lb      = put(x.lb, key, val, axis);
            else if (cmp >= 0) x.rt      = put(x.rt, key, val, axis);
        }

        else{
            p = x.p.y();
            Double k = key.y(); 
            cmp = k.compareTo(p);
            axis = 0;

            if (cmp < 0)      x.lb      = put(x.lb, key, val, axis);
            else if (cmp >= 0) x.rt      = put(x.rt, key, val, axis);
            
        }
        return x;
    }

    private Value get(Node x, Point2D key, int axis) {
        if (x == null) return null;
        Value v = null;
        int cmp;
        Double p;
        Point2D p0 = x.p;    
        if( p0.equals(key) == true){
            return x.value;
        }
        if (axis == 0 ){
            p = x.p.x();
            Double k = key.x(); 
            cmp = k.compareTo(p);
            axis = 1;

            if (cmp < 0)      v = get(x.lb, key, axis);
            else if (cmp >= 0) v = get(x.rt, key, axis);
        }

        else{
            p = x.p.y();
            Double k = key.y(); 
            cmp = k.compareTo(p);
            axis = 0;

            if (cmp < 0)      v = get(x.lb, key, axis);
            else if (cmp >= 0) v = get(x.rt, key, axis);
            
        }
        return v;
    }

    // value associated with point p 
    public Value get(Point2D p){
        return get(root, p, 0);
    }

    // does the symbol table contain point p? 
    public boolean contains(Point2D p){
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(p) != null;
    }

    // all points in the symbol table 
    public Iterable<Point2D> points(){
        if (isEmpty()) return new Queue<Point2D>();
        Queue<Point2D> queue = new Queue<Point2D>();
        keys(root, queue);
        return queue;
    }

    private void keys(Node x, Queue<Point2D> queue) { 
        if (x == null) return;  
        keys(x.lb, queue); 
        queue.enqueue(x.p); 
        keys(x.rt, queue); 
    } 
    // all points that are inside the rectangle (or on the boundary) 
    /*public Iterable<Point2D> range(RectHV rect){

    }

    // a nearest neighbor of point p; null if the symbol table is empty 
    public Point2D nearest(Point2D p){

    }

    // unit testing (required)
    */public static void main(String[] args){
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        Point2D p = new Point2D(0.2, 0.3);


        double [] x = { 0.0, 1.9, 2.0 , 5.0, 0.0, 2.2 ,4.3 , 1.3, 5.2, 8.1, 9.0 };
        double [] y = { 2.1, 9.0, 3.0 , 4.0, 4.1, 1.9 ,0.3 , 3.1, 1.1, 4.1, 1.0 };
        int i = 0;
        while (i <x.length) {
            double x0 = x[i];
            double y0 = y[i];
            Point2D p1 = new Point2D(x0, y0);
            kdtree.put(p1, i);
            i++;
        }

        for (Point2D s : kdtree.points())
            StdOut.println("Point: " + s + " " + kdtree.get(s));
        
        StdOut.println();

        StdOut.println("Size:" +" "+ kdtree.size());
        StdOut.println("isEmpty:" +" "+ kdtree.isEmpty());
        StdOut.println("Point:" + " " + kdtree.get(new Point2D(5.0, 4.0 )));
        StdOut.println("contains (2.0, 3.0): " + kdtree.contains(new Point2D(2,3)) );
        StdOut.println("contains (0.0, 0.0): " + kdtree.contains(new Point2D(0.0,0.0)) );
        //StdOut.println(kdtree.contains(p));        

    }

}
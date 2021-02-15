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

/*
  * A randomized queue is similar to a stack or queue, except that the item 
  * removed is chosen uniformly at random among items in the data structure. */
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning of queue
    private Node<Item> last;     // end of queue
    private int n;               // number of elements on queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty queue.
     */
    public RandomizedQueue() {
        first = null;
        last  = null;
        n = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return {@code true} if this queue is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this queue.
     */
    public int size() {
        return n;
    }
    // return a random item (but do not remove it)
    /*public Item sample(){

    }
    /**
     * Adds the item to this queue.
     */
    public void enqueue(Item item) {
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else           oldlast.next = last;
        n++;
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        Item item;
        int i =  StdRandom.uniform(n);
        Node<Item> p = first;
        Node<Item> aux = p;
        if( i != 0){
            for(int j = 0; j < i; j++){
                aux = p;
                p = p.next;
            }
            item = p.item;
            if( i == n-1)
                last = aux;
            else {
                aux.next = p.next;
                p = p.next;
            }
        }
        else{
            item = first.item;
            first = first.next;
        }
        n--;
        return(item);


    }

    // return a random item (but do not remove it)
    public Item sample(){
        if (isEmpty()) throw new NoSuchElementException();
        Item item;
        int i =  StdRandom.uniform(n);
        Node<Item> p = first;
        for(int j = 0; j < i; j++)
            p = p.next;
        item = p.item;
        return(item);

    }

    /*Returns an iterator that iterates over the items in this queue in FIFO order.
     */
    public Iterator<Item> iterator()  {
        return new ListIterator(first);
    }
    @SuppressWarnings("unchecked")
    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node<Item> current;
        private int i = 0;
        private Item[] temp;

        public ListIterator(Node<Item> first) {
            //@SuppressWarnings("unchecked")
            temp = (Item[]) new Object[n];
            current = first;
            for (int j = 0; j < n; j++) {
                temp[j] = current.item;
                current = current.next;
            }

        }

        public boolean hasNext()  { return i < n;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int k = StdRandom.uniform(n-i);
            Item item = temp[k];
            temp[k] = temp[n-(++i)];
            temp[n-i] = null;
            return item;
        }
    }

     //Unit tests 

    public static void main(String[] args) {
        RandomizedQueue <String> queue = new RandomizedQueue <String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty()){
                StdOut.print("dequeue : " + queue.dequeue() + " "  + " left : ");
                Iterator <String>it = queue.iterator();
                while (it.hasNext())
                    StdOut.print(it.next() + " ");
            }
        }
        StdOut.println("(" + queue.size() + " left on queue)");
    }
}
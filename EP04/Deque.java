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
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;

public class Deque<Item> implements Iterable<Item> {
//public class Deque<Item> {

    private Node<Item> first;    // beginning 
    private Node<Item> last;     // end 
    private int n;               // number of elements on Deque

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque(){
        first = null;
        last  = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        if(first == null )return true;
        return false;
    }

    // return the number of items on the deque
    public int size(){
        return n;
    }

    // add the item to the front
    public void addFirst(Item item){
    	Node<Item> new_node = new Node<Item>();
        new_node.item = item;
        new_node.next = new_node.prev = null ;
        if (isEmpty()){ 
            //throw new NoSuchElementException("Deque underflow");
            last = first = new_node; 
        }
        // Inserts node at the front
        else { 
            new_node.next = first; 
            first.prev = new_node; 
            first = new_node; 
        } 
        n++;
        
    }

    // add the item to the back

    public void addLast(Item item){
        Node<Item> new_node = new Node<Item>();
        new_node.item = item;
        new_node.next = new_node.prev = null ;
        // If deque is empty 
        if (isEmpty()){
            //throw new NoSuchElementException("Deque underflow");
            first = last = new_node; 
        } 
        // Inserts node at the rear end 
        else { 
            new_node.prev = last; 
            last.next = new_node; 
            last = new_node; 
        } 
        n++;  
    } 

    // remove and return the item from the front
    public Item removeFirst(){
        Item item = first.item;
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
    	else {
    		Node<Item> tmp = new Node<Item>();
          	tmp = first.next;
        	if(tmp!= null) tmp.prev = null;
        	if(tmp== null) last = null;
        	first = tmp;
        	n--; 
    	}
        return item; 
        
    }

    // remove and return the item from the back
    public Item removeLast(){
        Item item = last.item;
    	if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        else {
            //remove an item from the beginning of the queue
            Node<Item> tmp = new Node<Item>();
            tmp = last.prev;
            if(tmp != null) tmp.next = null;
            if(tmp == null) first = null;
            last = tmp;
            n--;
        }
        return item;  
         
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
          return new ListIterator(first);
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }



    // unit testing (required)
    public static void main(String[] args){
        Deque<String> deque = new Deque<String>();
        String s= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String item = "abc";
        deque.addFirst(item);
        StdOut.println("addFirst : "+ " " + item + " ");
        item = "wxyz";
        deque.addLast(item);
        StdOut.println("addLast : " + " " +   item + " ");
        item = "PQRS";
        deque.addLast(item);
        StdOut.println("addLast : " + " " +   item + " ");
        item = "GH";
        deque.addFirst(item);
        StdOut.println("addFirst : " + " " +  item + " ");
        StdOut.println("size : " + " " + deque.size());
        // iterar com while
        StdOut.println("Iterando com iterador: ");
        Iterator <String>it = deque.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }
        StdOut.println("removeLast : "  + " " + deque.removeLast()  + " ");
        StdOut.println("removeLast : "  + " " + deque.removeLast()  + " ");
        StdOut.println("removeFirst : " + " " + deque.removeFirst() + " ");
        StdOut.println("(" + deque.size() + " left on deque)");
    }
}

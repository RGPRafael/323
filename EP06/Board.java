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
        Dificuldade em entender como implementar a função neighbors()
        dado a não entender muito bem como implementar iterable e o interator,
        alem da dificuldade de entender o problema desde questões basicas como
        seria a maneira correta de representar o board, por uma array com duas ou uma 
        dimensão. 

****************************************************************/
import java.util.Arrays;
import java.util.Iterator;
import java.lang.Object;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;


public class Board {

    private int[][] board ;
    private int[] board1D;
    private int[] goal;
    private int n ;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        int row = tiles.length;
        int col = tiles[0].length;
        n = row;
        board = new int [row][col];
        for(int i = 0 ; i < row; i++){
            for(int j = 0; j < col; j++){
                board[i][j] = tiles[i][j];
            }
        }
        board1D = new int[board.length * board.length];
        for(int i = 0; i < board.length; i ++){
            for(int s = 0; s < board.length; s ++){
                board1D[(i * board.length) + s] = board[i][s];
            }
        }
        goal =  new int[n * n];
        for(int i = 1, j = 0; j < (n*n)-1; i++,j++)goal[j]= i;
        goal[(n*n)-1] = 0;


    }
                                           
    // string representation of this board
    public String toString(){
        String s = Integer.toString(n) + "\n" ;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                s = s + String.format("%2d ",board[i][j]);
            }
            s = s + "\n";
        }
        return(s);

    }

    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col){
        int i = row;
        int j = col;
        int t = board[i][j];
        if( t != 0)return(t);
        return(0);
    }

    // board size n
    public int size(){
        return n;
    }

    // number of tiles out of place
    //To measure how close a board is to the goal board, we define two notions of distance. 
    //The Hamming distance betweeen a board and the goal board is the number of tiles in the wrong position. 
    /*
    public int hamming(){

    }*/

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int x, y, m = 0;
        //anda n board[][]
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if( board[i][j] != 0){
                    y = (board[i][j]-1)%n;
                    x = (board[i][j]-1)/n;
                    m = m + Math.abs(i - x) + Math.abs(j - y); 
                }

            }
        }
        return(m);
    }

    // is this board the goal board?
    public boolean isGoal(){
        for(int i = 0; i < board.length; i ++){
            for(int s = 0; s < board.length; s ++){
                if(goal[(i * board.length) + s] != board[i][s])
                    return(false);
            }
        }
        return(true);

    }

    // does this board equal y?
    @Override
    public boolean equals(Object y){

        if (this == y)  // se as referências forem iguais, é true
             return true;  
        if (y == null)  // this é sempre diferente de null - esse cheque é feito para evitar um NullPointerException abaixo
             return false;  
        if (getClass() != y.getClass())  // se não forem exatamente da mesma classe, são diferentes
             return false;  
        Board p = (Board) y;
        // field comparison
        return (  Arrays.equals(board1D, p.board1D) && board1D != null && n == p.n);


    }
    
    // all neighboring boards
    
    private String findzero(){
        int i = 0;
        int s = 0;
        for(i = 0; i < n; i ++){
            for(s = 0; s < n; s ++){
                if(board1D[(i * n) + s] == 0){
                    break;
                }
            }
        }        
        String lc = Integer.toString(i) + Integer.toString(s) ;
        return(lc);

    }
    public Iterable<Board> neighbors(){
        return new Iterable<Board>(){

            @Override
            public Iterator<Board> iterator(){

                return new Iterator<Board>(){

                    private int position;
                    private Board[] items;
                    String z = findzero();
                    int l = Character.getNumericValue(z.charAt(0));
                    int c = Character.getNumericValue(z.charAt(1));

                    public Board troca(int[][] array,int t ){
                        ///mova para cima t = 0
                        int aux ;
                        if( t == 0){
                            aux = array[l+1][c];
                            array[l+1][c]=0;
                            array[l][c] = aux;
                        }
                        // mova para baixo t = 1
                        else if( t == 1){
                            aux = array[l-1][c];
                            array[l-1][c]=0;
                            array[l][c] = aux;   
                        }
                        // mova para o lado esq t = 2 
                        else if( t == 2){
                            aux = array[l][c-1];
                            array[l][c-1]=0;
                            array[l][c] = aux;   
                        }
                        // mova para o lado direito t = 3
                        else if( t == 3){
                            aux = array[l][c+1];
                            array[l][c+1]=0;
                            array[l][c] = aux;   
                        }
                        return(new Board(array));
                    }

                    public void resolve(){
                        //int[] secondArray = Arrays.copyOf (board1D, n*n);
                        int t = 0;
                        int p = 0;
                        int [][] b = new int[n][n];
                        for(int i = 0 ; i < n; i++){
                            for(int j = 0; j < n; j++){
                                b[i][j] = board1D[(i * board1D.length) + j];
                            }
                        }
                        if(l == 0 && c != 0 && c != n -1){//teto
                            for(int i = 1 ; i <= 3; i++){
                                Board novo = troca(b,i);
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }
                        else if(l == 0 && c == 0){//canto superior esquerdo
                            t = 1;
                            for(int i = 0 ; i < 2; i++){
                                Board novo = troca(b,t+i);
                                t++;
                                items[p] = novo;//adiciona em items
                                p++;
                            }

                        }
                        else if(l == 0 && c == n -1){// canto superior direito
                            t = 1;
                            for(int i = 0 ; i < 2; i++){
                                Board novo = troca(b,t);
                                t++;
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }
                        else if(l== n -1 && c == 0){//canto inferior esquerdo
                            t = 0;
                            for(int i = 0 ; i < 2; i++){
                                Board novo = troca(b,t);
                                t = t + 3;
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }
                        else if(l == n - 1 && c == n -1){//canto inferior direito
                            t = 0;
                            for(int i = 0 ; i < 2; i++){
                                Board novo = troca(b,t); //troca
                                t = t + 2 ;//faz novo board
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }
                        else if(l == n -1 && c != 0 && c != n -1){//chao
                            t = 0; 
                            for(int i = 0 ; i < 3; i++){
                                if( t==1)t++;
                                Board novo = troca(b,t); //troca
                                t++;
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }
                        else{//meio
                            for(int i = 0 ; i <= 3; i++){
                                Board novo = troca(b,i);
                                items[p] = novo;//adiciona em items
                                p++;
                            }
                        }

                    }

                    @Override
                    public boolean hasNext(){
                        return position != items.length;
                    }

                    @Override
                    public Board next(){
                        if (!hasNext()) throw new NoSuchElementException();
                        return items[position++];
                    }
                };
            }
        };

    }
    


    // is this board solvable?
    public boolean isSolvable(){
        int inversoes = 0, t;
        for(int i = 0, j = 0; i < board1D.length; i++){
            t = board1D[i]; 
            for(int s = i + 1; s < board1D.length; s ++){
                if( t > board1D[s] && board1D[s] != 0)
                    inversoes++;
            }
        }
        if(inversoes%2 != 0)return(false);
        return(true);
    }

    // unit testing (required)
    public static void main(String[] args){
        int b[][] = {{8,1,3},{4,0,2},{7,6,5}};
        int c[][] = {{8,1,3},{4,0,2}};
        int a[][] = {{8,0,3},{4,0,2},{7,6,0}};
        Board board =  new Board(b);
        Board board2 = new Board(a);
        Board board3 = new Board(c);
        StdOut.println(board.size());
        StdOut.println(board.toString());
        StdOut.println(board.tileAt(2,2));

        boolean isEqual = board.equals(board2);
        StdOut.println(isEqual);

        isEqual = board.equals(board3);
        StdOut.println(isEqual);


    }

}
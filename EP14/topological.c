/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 * 
 * ADT Topological é uma "representação topológica" de digrafo.
 * Esta implementação usa ADT Digraph do EP13.
 *  
 * Busque inspiração em: 
 *
 *   https://algs4.cs.princeton.edu/42digraph/
 *   https://algs4.cs.princeton.edu/42digraph/DepthFirstOrder.java
 *   https://algs4.cs.princeton.edu/42digraph/Topological.java
 *   https://algs4.cs.princeton.edu/42digraph/DirectedCycle.java
 * 
 * TOPOLOGICAL
 *
 * Topological é uma ¨representação topológica" de um dado digrafo.
 * 
 * As principais operações são: 
 *
 *      - hasCycle(): indica se o digrafo tem um ciclo (DirectedCycle.java)
 *      - isDag(): indica se o digrafo é acyclico (Topological.java)
 *
 *      - pre(): retorna a numeração pré-ordem de um vértice em relação a uma dfs 
 *               (DepthFirstOrder.java)
 *      - pos(): retorna a numareção pós-ordem de um vértice em relação a uma dfs
 *               (DepthFirstOrder.java)
 *      - rank(): retorna a numeração topológica de um vértice (Topological.java)
 * 
 *      - preorder(): itera sobre todos os vértices do digrafo em pré-ordem
 *                    (em relação a uma dfs, DepthFirstOrder.java)
 *      - postorder(): itera sobre todos os vértices do digrafo em pós-ordem
 *                    (em relação a uma dfs, ordenação topologica reversa, 
 *                     DepthFirstOrder.java)
 *      - order(): itera sobre todos os vértices do digrafo em ordem  
 *                 topologica (Topological.java)
 *      - cycle(): itera sobre os vértices de um ciclo (DirectedCycle.java)
 *
 * O construtor e "destrutor" da classe consomem tempo linear..
 *
 * Cada chama das demais operações consome tempo constante.
 *
 * O espaço gasto por esta ADT é proporcional ao número de vértices V do digrafo.
 * 
 * Para documentação adicional, ver 
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "topological.h"

#include "digraph.h" /* Digraph, vDigraph(), eDigraph(), adj(), ... */
#include "bag.h"     /* add() e itens() */
#include "util.h"    /* emalloc(), ecalloc(), ERRO(), AVISO() */

#include <stdlib.h>  /* free() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debugging */
#endif

/*----------------------------------------------------------*/
/* 
 * Estrutura básica de um Topological
 * 
 */


struct topological {
    Bool *marked;                       // marked[v] = has v been marked in dfs?
    int *pre;                           // pre[v]    = preorder  number of v
    int *post;                          // post[v]   = postorder number of v

    Bag preorder;                      // vertices in preorder
    Bag postorder;                     // vertices in postorder


    int preCounter;                    // counter or preorder numbering
    int postCounter;                   // counter for postorder numbering
    
    int *rank;                          // rank[v] = rank of vertex v in order
    int *order;
    int order_size;
    int order_next;
    int *edgeTo;                        // edgeTo[v] = previous vertex on path to v

    Bool *onStack;                      // onStack[v] = is vertex on the stack?
    Bag cycle;                          // directed cycle (or null if no such cycle)
    Bool has_cycle;
    Bool is_dag;

};

/*------------------------------------------------------------*/
/* 
 * Protótipos de funções administrativas: tem modificador 'static'
 * 
 */

/*-----------------------------------------------------------*/
/*
 *  newTopologica(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA uma representação topológica de G.
 * 
 */
void dfs(Topological ts, Digraph G, int v);
void dfs_c(Topological ts, Digraph G, int v) ;

Bag  invert(Bag b);
void  set(Bool b[], int n);

Topological newTopological(Digraph G){
    Topological ts  = emalloc(sizeof(struct topological));

    ts->marked      = emalloc(vDigraph(G) * sizeof(Bool));
    ts->onStack     = emalloc(vDigraph(G) * sizeof(Bool));

    
    set(ts->marked,  vDigraph(G));
    set(ts->onStack, vDigraph(G));
    

    ts->pre         = emalloc(vDigraph(G) * sizeof(int));
    ts->post        = emalloc(vDigraph(G) * sizeof(int));

    ts->postorder   = newBag();
    ts->preorder    = newBag();

    ts->preCounter  = 0;
    ts->postCounter = 0;

    ts->rank        = NULL;
    ts->order       = emalloc(vDigraph(G) * sizeof(int));
    ts->order_size  = vDigraph(G);
    ts->order_next  = 0;
    ts->edgeTo      = emalloc(vDigraph(G) * sizeof(int));
    
    
    ts->cycle       = NULL;
    ts->has_cycle   = FALSE;
    ts->is_dag      = FALSE;

    for (int v = 0; v < vDigraph(G); v++){
        if (!ts->marked[v]) dfs(ts, G, v);
    }
    set(ts->marked,  vDigraph(G));

    for (int v = 0; v <  vDigraph(G); v++){
        if (!ts->marked[v] && ts->cycle == NULL){
            dfs_c(ts, G, v);
        }
    }
    if (!ts->has_cycle){
          
        //order = dfs.reversePost();
        for (int i = 0, v = itens(ts->postorder, TRUE); v!= -1; v = itens(ts->postorder, FALSE))
            ts->order[i++]  = v;
        
        ts->is_dag = TRUE;
        ts->rank = emalloc(vDigraph(G) * sizeof(int));
        
        for(int v = 0 , i = 0; v < vDigraph(G); v++){
            int k = ts->order[v];
            ts->rank[k] = i++;
        }
    }
    
    ts->preorder  = invert(ts->preorder);
    ts->postorder = invert(ts->postorder);

    return ts;
}
void set(Bool b[], int n){

    for (int v = 0; v < n; v++)
        b[v] = FALSE;
}

Bag invert(Bag b){
    Bag aux =  newBag();
    for (int pInt = itens(b, TRUE); pInt != -1; pInt = itens(b, FALSE)){
        add(aux, pInt);
    }
    freeBag(b);
    return aux;
}

// run DFS in digraph G from vertex v and compute preorder/postorder
void dfs(Topological ts, Digraph G, int v) {
    ts->marked[v]   = TRUE;
    ts->pre[v]      = ts->preCounter++;
    add(ts->preorder, v); 

    for (int i  = adj(G, v, TRUE); i != -1; i = adj(G, v, FALSE)){
        if (!ts->marked[i]){
            dfs(ts, G, i);
        }
    }
    add(ts->postorder, v); 
    ts->post[v] = ts->postCounter++;
}

void dfs_c(Topological ts, Digraph G, int v) {
    ts->onStack[v] = TRUE;
    ts->marked[v] = TRUE;
    for (int w  = adj(G, v, TRUE); w != -1; w = adj(G, v, FALSE)){ 
        if (ts->cycle != NULL){
            ts->has_cycle = TRUE;
            return;      
        }
        else if (!ts->marked[w]) {          
            ts->edgeTo[w] = v;
            dfs_c(ts, G, w);
        }

        else if (ts->onStack[w]) {
            ts->cycle = newBag();   
            ts->has_cycle = TRUE;
            for (int x = v; x != w; x = ts->edgeTo[x]) 
                add(ts->cycle, x);  
            add(ts->cycle, w);      
            //add(ts->cycle, v);      
        }
    }
    ts->onStack[v] = FALSE;
}


/*-----------------------------------------------------------*/
/*
 *  freeTopological(TS)
 *
 *  RECEBE uma representação topologica TS.
 *  DEVOLVE ao sistema toda a memória usada por TS.
 *
 */
void freeTopological(Topological ts){
    free(ts->marked); 
    free(ts->onStack);

    free(ts->edgeTo);
    if (!ts->has_cycle)free(ts->rank);
    else freeBag(ts->cycle); 
    free(ts->order);

    freeBag(ts->preorder); 
    freeBag(ts->postorder);
    
    free(ts->post);
    free(ts->pre); 
    
    free(ts);
}    

/*------------------------------------------------------------*/
/*
 *  OPERAÇÕES: 
 *
 */

/*-----------------------------------------------------------*/
/* 
 *  HASCYCLE(TS)
 *
 *  RECEBE uma representação topológica TS de um digrafo;
 *  RETORNA TRUE seu o digrafo possui um ciclo e FALSE em caso 
 *  contrário.
 *
 */
Bool hasCycle(Topological ts){

    if(ts->has_cycle == FALSE)return FALSE;
    return TRUE;
}

/*-----------------------------------------------------------*/
/* 
 *  ISDAG(TS)
 *
 *  RECEBE um representação topológica TS de um digrafo.
 *  RETORNA TRUE se o digrafo for um DAG e FALSE em caso 
 *  contrário.
 *
 */
Bool isDag(Topological ts){
    if(ts->is_dag == FALSE)return FALSE;
    return TRUE;
}

/*-----------------------------------------------------------*/
/* 
 *  PRE(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um 
 *  vértice V.
 *  RETORNA a numeração pré-ordem de V em TS.
 *
 */
int pre(Topological ts, vertex v){
    //validateVertex(v);
    return ts->pre[v];
}

/*-----------------------------------------------------------*/
/* 
 *  POST(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um 
 *  vértice V.
 *  RETORNA a numeração pós-ordem de V em TS.
 *
 */
int post(Topological ts, vertex v){
    //validateVertex(v);
    return ts->post[v];
}

/*-----------------------------------------------------------*/
/* 
 *  RANK(TS, V)
 *
 *  RECEBE uma representação topológica TS de um digrafo e um 
 *  vértice V.
 *  RETORNA a posição de V na ordenação topológica em TS;
 *  retorna -1 se o digrafo não for um DAG.
 *
 */
int rank(Topological ts, vertex v){
    if(!ts->is_dag)return -1;
    return ts->rank[v];


}

/*-----------------------------------------------------------*/
/* 
 *  PREORDER(TS, INIT)
 * 
 *  RECEBE uma representação topológica TS de um digrafo e um 
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  PREORDER() RETORNA o primeiro vértice na ordenação pré-ordem de TS.
 *  Se INIT é FALSE, PREORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pré-ordem de TS; se todos os vértices já foram retornados, 
 *                   a função retorna -1.
 */
vertex preorder(Topological ts, Bool init){
    return itens(ts->preorder, init);

}

/*-----------------------------------------------------------*/
/* 
 *  POSTORDER(TS, INIT)
 * 
 *  RECEBE uma representação topológica TS de um digrafo e um 
 *  Bool INIT.
 *
 *  Se INIT é TRUE,  POSTORDER() RETORNA o primeiro vértice na ordenação pós-ordem de TS.
 *  Se INIT é FALSE, POSTORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação pós-ordem de TS; se todos os vértices já foram retornados, 
 *                   a função retorna -1.
 */
vertex postorder(Topological ts, Bool init){
    return itens(ts->postorder, init);
}

/*-----------------------------------------------------------*/
/* 
 *  ORDER(TS, INIT)
 * 
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  ORDER() RETORNA o primeiro vértice na ordenação topológica 
 *                   de TS.
 *  Se INIT é FALSE, ORDER() RETORNA o vértice sucessor do último vértice retornado
 *                   na ordenação topológica de TS; se todos os vértices já foram 
 *                   retornados, a função retorna -1.
 *
 *  Se o digrafo _não_ é um DAG, ORDER() RETORNA -1.
 */
vertex order(Topological ts, Bool init){
    vertex item = -1;
    if(ts->has_cycle)return item;
    if(init == TRUE){
        item = ts->order[ts->order_next++];
    }
    
    else{
        if(ts->order_next< ts->order_size ){
            item = ts->order[ts->order_next++];
        }
    }
    return item;
    //return itens(ts->order, init);
    //return -1;
}

/*-----------------------------------------------------------*/
/* 
 *  CYCLE(TS, INIT)
 * 
 *  RECEBE uma representação topológica TS de um digrafo e um Bool INIT.
 *
 *  Se INIT é TRUE,  CYCLE() RETORNA um vértice em um ciclo do digrafo.
 *  Se INIT é FALSE, CYCLE() RETORNA o vértice  no ciclo que é sucessor do 
 *                   último vértice retornado; se todos os vértices no ciclo já 
 *                   foram retornados, a função retorna -1.
 *
 *  Se o digrafo é um DAG, CYCLE() RETORNA -1.
 *
 */
vertex cycle(Topological ts, Bool init){
    if(ts->is_dag)return -1;
    return itens(ts->cycle, init);
    //return -1;
}


/*------------------------------------------------------------*/
/* 
 * Implementaçao de funções administrativas: têm o modificador 
 * static.
 */


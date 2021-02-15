/*
 * MAC0323 Algoritmos e Estruturas de Dados II
 * 
 * ADT Digraph implementada atrevés de vetor de listas de adjacência.
 * As listas de adjacência são bag de ints que são mais restritos 
 * que as bags genéricas do EP12. Veja a api bag.h e simplifique 
 * o EP12 de acordo. 
 *  
 * Busque inspiração em: 
 *
 *    https://algs4.cs.princeton.edu/42digraph/ (Graph representation)
 *    https://algs4.cs.princeton.edu/42digraph/Digraph.java.html
 * 
 * DIGRAPH
 *
 * Digraph representa um grafo orientado de vértices inteiros de 0 a V-1. 
 * 
 * As principais operações são: add() que insere um arco no digrafo, e
 * adj() que itera sobre todos os vértices adjacentes a um dado vértice.
 * 
 * Arcos paralelos e laços são permitidos.
 * 
 * Esta implementação usa uma representação de _vetor de listas de adjacência_,
 * que  é uma vetor de objetos Bag indexado por vértices. 

 * ATENÇÃO: Por simplicidade esses Bag podem ser int's e não de Integer's.
 *
 * Todas as operações consomen no pior caso tempo constante, exceto
 * iterar sobre os vértices adjacentes a um determinado vértice, cujo 
 * consumo de tempo é proporcional ao número de tais vértices.
 * 
 * Para documentação adicional, ver 
 * https://algs4.cs.princeton.edu/42digraph, Seção 4.2 de
 * Algorithms, 4th Edition por Robert Sedgewick e Kevin Wayne.
 *
 */

/* interface para o uso da funcao deste módulo */
#include "digraph.h"


#include "bag.h"     /* add() e itens() */
#include <stdio.h>   /* fopen(), fclose(), fscanf(), ... */
#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debuging */
#endif

/*----------------------------------------------------------*/
/* 
 * Estrutura básica de um Digraph
 * 
 * Implementação com vetor de listas de adjacência.
 */
struct digraph {
	int V;           		// number of vertices in this digraph
    int E;                 	// number of edges in this digraph
    Bag *adj;    			// adj[v] = adjacency list for vertex v
    int *indegree;        	// indegree[v] = indegree of vertex v

};

/*------------------------------------------------------------*/
/* 
 * Protótipos de funções administrativas: tem modificador 'static'
 * 
 */

/*-----------------------------------------------------------*/
/*
 *  newDigraph(V)
 *
 *  RECEBE um inteiro V.
 *  RETORNA um digrafo com V vértices e 0 arcos.
 * 
 */
Digraph newDigraph(int V) {
	Digraph d = emalloc(sizeof(struct digraph ));
    if (V < 0){
    	ERROR("Number of vertices in a Digraph must be nonnegative \n");
    	return NULL;
    }
    d->V = V;
    d->E = 0;
    d->indegree = emalloc( V * sizeof(int));
    d->adj = emalloc( V * sizeof(Bag));
    for (int v = 0; v < V; v++) {
        d->adj[v] = newBag();
    }

    return d;
}

/*-----------------------------------------------------------*/
/*
 *  cloneDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA um clone de G.
 * 
 */
Digraph cloneDigraph(Digraph G){
	int pInt;
    Digraph new =  newDigraph(G->V); 		                      			
    for (int v = 0; v < new->V; v++)
    	new->indegree[v] = G->indegree[v];			
    for (int v = 0; v < G->V; v++){				
        for (pInt = itens(G->adj[v], TRUE); pInt != -1; pInt = itens(G->adj[v], FALSE)){
        	addEdge(new, v, pInt);
        }
    }
    
    return new;
}

/*-----------------------------------------------------------*/
/*
 *  reverseDigraph(G)
 *
 *  RECEBE um digrafo G.
 *  RETORNA o digrafo R que é o reverso de G: 
 *
 *      v-w é arco de G <=> w-v é arco de R.
 * 
 */
Digraph reverseDigraph(Digraph G){

	Digraph reverse = newDigraph(G->V);
    for (int v = 0; v < G->V; v++) {
    	for (int pInt = itens(G->adj[v], TRUE); pInt != -1; pInt = itens(G->adj[v], FALSE))
            addEdge(reverse, pInt, v);
    }
    return reverse;
}

/*-----------------------------------------------------------*/
/*
 *  readDigraph(NOMEARQ)
 *
 *  RECEBE uma stringa NOMEARQ.
 *  RETORNA o digrafo cuja representação está no arquivo de nome NOMEARQ.
 *  O arquivo contém o número de vértices V, seguido pelo número de arestas E,
 *  seguidos de E pares de vértices, com cada entrada separada por espaços.
 *
 *  Veja os arquivos  tinyDG.txt, mediumDG.txt e largeDG.txt na página do 
 *  EP e que foram copiados do algs4, 
 * 
 */
Digraph readDigraph(String nomeArq){
	FILE * file;
	file = fopen( nomeArq , "r");
	int V, ne ,line, line1;
	fscanf(file,"%d", &V); 
	Digraph N = newDigraph(V);
	fscanf(file,"%d", &ne);
	for(int i = 0; i < ne ; i++){
		fscanf(file," %d %d\n", &line, &line1);
		addEdge(N,line,line1);
	}
	fclose(file);
	return N;
}


/*-----------------------------------------------------------*/
/*
 *  freeDigraph(G)
 *
 *  RECEBE um digrafo G e retorna ao sistema toda a memória 
 *  usada por G.
 *
 */
void freeDigraph(Digraph G){

	for(int i = 0; i < G->V ; i++)freeBag(G->adj[i]);
	free(G->indegree);
    free(G->adj);
    free(G);


}    

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: 
 *
 *     - vDigraph(), eDigraph(): número de vértices e arcos
 *     - addEdge(): insere um arco
 *     - adj(): itera sobre os vizinhos de um dado vértice
 *     - outDegree(), inDegree(): grau de saída e de entrada
 *     - toString(): usada para exibir o digrafo 
 */

/*-----------------------------------------------------------*/
/* 
 *  VDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de vertices.
 *
 */
int vDigraph(Digraph G){
	return G->V;
    
}

/*-----------------------------------------------------------*/
/* 
 *  EDIGRAPH(G)
 *
 *  RECEBE um digrafo G e RETORNA seu número de arcos (edges).
 *
 */
int eDigraph(Digraph G){
	return G->E;
}

/*-----------------------------------------------------------*/
/*
 *  addEdge(G, V, W)
 * 
 *  RECEBE um digrafo G e vértice V e W e INSERE o arco V-W  
 *  em G.
 *
 */

// throw an IllegalArgumentException unless {@code 0 <= v < V}
Bool validateVertex(Digraph G , vertex v) {
    if (v < 0 || v >= G->V){
    	ERROR("VALOR INVALIDO");
    	return FALSE;
    }
    return TRUE;
}

void addEdge(Digraph G, vertex v, vertex w){

    if( validateVertex(G,v) && validateVertex(G,w) ){
    	add(G->adj[v], w);
    	G->indegree[w]++;
    	G->E++;
    }
}    


/*-----------------------------------------------------------*/
/* 
 *  ADJ(G, V, INIT)
 * 
 *  RECEBE um digrafo G, um vértice v de G e um Bool INIT.
 *
 *  Se INIT é TRUE,  ADJ() RETORNA o primeiro vértice na lista de adjacência de V.
 *  Se INIT é FALSE, ADJ() RETORNA o sucessor na lista de adjacência de V do 
 *                   último vértice retornado.
 *  Se a lista de adjacência de V é vazia ou não há sucessor do último vértice 
 *  retornada, ADJ() RETORNA -1.
 *
 *  Se entre duas chamadas de ADJ() a lista de adjacência de V é alterada, 
 *  o comportamento é  indefinido. 
 *  
 */
int adj(Digraph G, vertex v, Bool init){
    if(!validateVertex(G , v)){
    	ERROR("VALOR INVALIDO");
    	return -1;
    }
    int item = itens(G->adj[v], init);
	return item;
    
}

/*-----------------------------------------------------------*/
/*
 *  outDegree(G, V)
 * 
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos saindo de V.
 *
 */
int outDegree(Digraph G, vertex v){
    //validateVertex(G , v);
    return size(G->adj[v]);
}

/*-----------------------------------------------------------*/
/*
 *  inDegree(G, V)
 * 
 *  RECEBE um digrafo G e vértice V.
 *  RETORNA o número de arcos entrando em V.
 *
 */
int inDegree(Digraph G, vertex v){
    //validateVertex(G , v);
    return G->indegree[v];
}



/*-----------------------------------------------------------*/
/*
 *  toString(G)
 * 
 *  RECEBE um digrafo G.
 *  RETORNA uma string que representa G. Essa string será usada
 *  para exibir o digrafo: printf("%s", toString(G)); 
 *    
 *  Sugestão: para fazer esta função inspire-se no método 
 *  toString() da classe Digraph do algs4.
 */
String toString(Digraph G){
    String str = emalloc(sizeof(String));
    String s = emalloc(sizeof(sizeof(long int) + strlen(" vertices, ") + sizeof(long int) + strlen(" edges ") + strlen("\n") + 1)
     +  2*G->V * sizeof(String) + G->E *sizeof(String) + strlen("\n") );
   	sprintf(s, "%d vertices, %d edges \n", G->V , G->E);
    for (int v = 0; v < G->V; v++) {
    	sprintf(str, "%d: ", v);
        strcat(s,str);
        int pInt;
        for (pInt = itens(G->adj[v], TRUE); pInt != -1; pInt = itens(G->adj[v], FALSE)){
        	sprintf(str, "%d ", pInt);
        	strcat(s,str);
        }
        strcat(s,"\n");
    }
    free(str);
    return s;
}

/*------------------------------------------------------------*/
/* 
 * Implementaçao de funções administrativas: têm o modificador 
 * static.
 */

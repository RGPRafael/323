/*
 * MAC0323 Estruturas de Dados e Algoritmo II
 * 
 * Tabela de simbolos implementada atraves de uma BST rubro-negra
 *
 *     https://algs4.cs.princeton.edu/33balanced/RedBlackBST.java.html
 * 
 * As chaves e valores desta implementação são mais ou menos
 * genéricos
 */

/* interface para o uso da funcao deste módulo */
#include "redblackst.h"  

#include <stdlib.h>  /* free() */
#include <string.h>  /* memcpy() */
#include "util.h"    /* emalloc(), ecalloc() */

#undef DEBUG
#ifdef DEBUG
#include <stdio.h>   /* printf(): para debug */
#endif

/*
 * CONSTANTES 
 */
#define RED   TRUE
#define BLACK FALSE 

/*----------------------------------------------------------*/
/* 
 * Estrutura Básica da Tabela de Símbolos: 
 * 
 * implementação com árvore rubro-negra
 */

typedef int (*func)(const void *key1, const void *key2);


typedef struct node Node;

//typedef struct redBlackST *RedBlackST;

func compara = NULL ;
/*----------------------------------------------------------*/
/* 
 * Estrutura de um nó da árvore
 *
 */
struct node {
    const void *key;           // key
    void *val;         // associated data
    Node *left, *right;  // links to left and right subtrees
    Bool color;     // color of parent link
    int size;          // subtree count   
    //func comp;
};

struct redBlackST {
    Node *root;
};

/*------------------------------------------------------------*/
/* 
 *  Protótipos de funções administrativas.
 * 
 *  Entre essa funções estão isRed(), rotateLeft(), rotateRight(),
 *  flipColors(), moveRedLeft(), moveRedRight() e balance().
 * 
 *  Não deixe de implmentar as funções chamadas pela função 
 *  check(): isBST(), isSizeConsistent(), isRankConsistent(),
 *  is23(), isBalanced().
 *
 */

/*---------------------------------------------------------------*/
static Bool isBST(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isSizeConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isRankConsistent(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool is23(RedBlackST st);

/*---------------------------------------------------------------*/
static Bool isBalanced(RedBlackST st);

/*-----------------------------------------------------------*/
/*
 *  initST(COMPAR)
 *
 *  RECEBE uma função COMPAR() para comparar chaves.
 *  RETORNA (referência/ponteiro para) uma tabela de símbolos vazia.
 *
 *  É esperado que COMPAR() tenha o seguinte comportamento:
 *
 *      COMPAR(key1, key2) retorna um inteiro < 0 se key1 <  key2
 *      COMPAR(key1, key2) retorna 0              se key1 == key2
 *      COMPAR(key1, key2) retorna um inteiro > 0 se key1 >  key2
 * 
 *  TODAS OS OPERAÇÕES da ST criada utilizam a COMPAR() para comparar
 *  chaves.
 * 
 */
RedBlackST initST(int (*compar)(const void *key1, const void *key2)){
    RedBlackST rs  = (RedBlackST)malloc(sizeof(struct redBlackST));
    //rs->root = (Node*) malloc(sizeof(Node));
    rs->root = NULL;
    //rs->root->comp = compar;
    compara = compar;
    return rs;
}
        

Node *new_node(const void *key, size_t sizeKey, const void *val, size_t sizeVal, Bool color, int size) {
    Node *n;
    n = (Node*) malloc(sizeof(Node));
    n->key = emalloc(sizeKey);
    memcpy((void *)n->key, key, sizeKey);
    n->val = emalloc(sizeVal);
    n->color = color;
    n->size = size;
    n->left =  NULL; 
    n->right = NULL;
    //n->comp = compara;
    return n;
}

int size_aux(Node *x){
    if (x == NULL) return 0;
    //printf("\nsize: %d\n",x->size);
    return x->size;
}


/*-----------------------------------------------------------*/
/*
 *  freeST(ST)
 *
 *  RECEBE uma RedBlackST  ST e devolve ao sistema toda a memoria 
 *  utilizada por ST.
 *
 */
void  freeST(RedBlackST st){
    free(st);
}

/***************************************************************************
*  Red-black tree helper functions.
***************************************************************************/


// flip the colors of a node and its two children
void flipColors(Node *h) {
    // h must have opposite color of its two children
    // assert (h != null) && (h.left != null) && (h.right != null);
    // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
    //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
    h->color = !h->color;
    h->left->color = !h->left->color;
    h->right->color = !h->right->color;
}


// make a left-leaning link lean to the right
Node *rotateRight(Node *h) {
    // assert (h != null) && isRed(h.left);
    Node *x = h->left;
    h->left = x->right;
    x->right = h;
    x->color = x->right->color;
    x->right->color = RED;
    x->size = h->size;
    h->size = size_aux(h->left) + size_aux(h->right) + 1;
    return x;
}

// make a right-leaning link lean to the left
Node *rotateLeft(Node *h) {
    // assert (h != null) && isRed(h.right);
    Node *x = h->right;
    h->right = x->left;
    x->left = h;
    x->color = x->left->color;
    x->left->color = RED;
    x->size = h->size;
    h->size = size_aux(h->left) + size_aux(h->right) + 1;
    return x;
}

Bool isRed(Node *x) {
    if (x == NULL) return FALSE;
    return x->color == RED;
}

// Assuming that h is red and both h.right and h.right.left
// are black, make h.right or one of its children red.

Node *moveRedRight(Node *h) {
    flipColors(h);
    if (isRed(h->left->left)) { 
        h = rotateRight(h);
        flipColors(h);
    }
    return h;
}


// Assuming that h is red and both h.left and h.left.left
// are black, make h.left or one of its children red.
Node *moveRedLeft(Node *h) {
    flipColors(h);
    if (isRed(h->right->left)) { 
        h->right = rotateRight(h->right);
        h = rotateLeft(h);
        flipColors(h);
    }
    return h;
}

/*------------------------------------------------------------*/
/*
 * OPERAÇÕES USUAIS: put(), get(), contains(), delete(),
 * size() e isEmpty().
 */

/*-----------------------------------------------------------*/
// insert the key-value pair in the subtree rooted at h
Node *aux_put(Node *h, const void *key, size_t sizeKey, const void *val, size_t sizeVal) { 
    
    if (h == NULL) return new_node(key, sizeKey, val, sizeVal, RED, 1);
    //printf("%s\n",h->key);

    int cmp = compara(key, h->key);
    if      (cmp < 0){ 
        h->left  = aux_put(h->left,  key, sizeKey, val, sizeVal );
        //printf("h->left : %s\n",h->left->key); 
    }
    else if (cmp > 0) {
        h->right = aux_put(h->right, key, sizeKey, val, sizeVal ); 
        //printf("h->right : %s\n",h->right->key); 

    }
    else{
        //h->key   = key;       
        h->val   = (void*)val;
    }
    
    // fix-up any right-leaning links
    if (isRed(h->right) && !isRed(h->left))         h = rotateLeft(h);
    if (isRed(h->left)  &&  isRed(h->left->left))   h = rotateRight(h);
    if (isRed(h->left)  &&  isRed(h->right))        flipColors(h);
    h->size = size_aux(h->left) + size_aux(h->right) + 1;
    return h;
    
}
/*------------------------------------------------------------*/

/*
 * OPERAÇÕES PARA TABELAS DE SÍMBOLOS ORDENADAS: 
 * min(), max(), rank(), select(), deleteMin() e deleteMax().
 */

/*-----------------------------------------------------------*/
/*
 *  MIN(ST)
 * 
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da menor chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void *min(RedBlackST st){

    /*public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    } 

    // the smallest key in subtree rooted at x; NULL if no such key
    private Node min(Node x) { 
        // assert x != NULL;
        if (x.left == NULL) return x; 
        else                return min(x.left); 
    } */

    return NULL;
}


/*-----------------------------------------------------------*/
/*
 *  MAX(ST)
 * 
 *  RECEBE uma tabela de símbolos ST e RETORNA uma cópia/clone
 *  da maior chave na tabela.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
void * max(RedBlackST st){
    
    /*public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    } 

    // the largest key in the subtree rooted at x; NULL if no such key
    private Node max(Node x) { 
        // assert x != NULL;
        if (x.right == NULL) return x; 
        else                 return max(x.right); 
    } */

    return NULL;
}

/*
 *  deleteMIN(ST)
 * 
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à menor chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void deleteMin(RedBlackST st){
}


/*-----------------------------------------------------------*/
/*
 *  deleteMAX(ST)
 * 
 *  RECEBE uma tabela de símbolos ST e remove a entrada correspondente
 *  à maior chave.
 *
 *  Se ST está vazia, faz nada.
 *
 */
void deleteMax(RedBlackST st){
}


// restore red-black tree invariant
Node *balance(Node *h) {
    // assert (h != null);

    if (isRed(h->right))                        h = rotateLeft(h);
    if (isRed(h->left) && isRed(h->left->left)) h = rotateRight(h);
    if (isRed(h->left) && isRed(h->right))      flipColors(h);

    h->size = size_aux(h->left) + size_aux(h->right) + 1;
    return h;
}

// the smallest key in subtree rooted at x; null if no such key
Node *min_no(Node *x) { 
    // assert x != null;
    if (x->left == NULL) return x; 
    else                 return min_no(x->left); 
} 

// delete the key-value pair with the minimum key rooted at h
Node *deleteMin_aux(Node *h) { 
    if (h->left == NULL)
        return NULL;

    if (!isRed(h->left) && !isRed(h->left->left))
        h = moveRedLeft(h);

    h->left = deleteMin_aux(h->left);
    return balance(h);
}

// delete the key-value pair with the given key rooted at h
Node *delete_node(Node *h, void *key) { 

    int cmp = compara(key, h->key);
    if (cmp < 0)  {
        if (!isRed(h->left) && !isRed(h->left->left))
            h = moveRedLeft(h);
        h->left = delete_node(h->left, key);
    }
    else {
        if (isRed(h->left))
            h = rotateRight(h);
        cmp = compara(key, h->key);
        if (cmp == 0 && (h->right == NULL))
            return NULL;
        if (!isRed(h->right) && !isRed(h->right->left))
            h = moveRedRight(h);
        cmp = compara(key, h->key);
        if (cmp == 0) {
            Node *x = min_no(h->right);
            h->key = x->key;
            h->val = x->val;
            h->right = deleteMin_aux(h->right);
        }
        else h->right = delete_node(h->right, key);
    }
    return balance(h);
}


/* 
 *  DELETE(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 * 
 *  Se KEY está em ST, remove a entrada correspondente a KEY.
 *  Se KEY não está em ST, faz nada.
 *
 */
void delete(RedBlackST st, const void *key){
    if (key == NULL) ERROR("argument to delete() is NULL");
        if (!contains(st, key)) return;

        // if both children of root are black, set root to red
        if (!isRed(st->root->left) && !isRed(st->root->right))
            st->root->color = RED;

        st->root = delete_node(st->root, (void*) st->root->key);
        if (!isEmpty(st)) st->root->color = BLACK;

}   

/*
 *  put(ST, KEY, NKEY, VAL, NVAL)
 * 
 *  RECEBE a tabela de símbolos ST e um par KEY-VAL e procura a KEY na ST.
 *
 *     - se VAL é NULL, a entrada da chave KEY é removida da ST  
 *  
 *     - se KEY nao e' encontrada: o par KEY-VAL é inserido na ST
 *
 *     - se KEY e' encontra: o valor correspondente é atualizado
 *
 *  NKEY é o número de bytes de KEY e NVAL é o número de bytes de NVAL.
 *
 *  Para criar uma copia/clone de KEY é usado o seu número de bytes NKEY.
 *  Para criar uma copia/clode de VAL é usado o seu número de bytes NVAL.
 *
 */
/*-----------------------------------------------------------*/

void  put(RedBlackST st, const void *key, size_t sizeKey, const void *val, size_t sizeVal){
        
    if (key == NULL) ERROR("first argument to put() is NULL");
    if (val == NULL) {
        delete(st,key);
        return;
    }
    //printf("\nCHAMANDO PUT\n\n");
    st->root = aux_put(st->root, key, sizeKey, val, sizeVal );
    st->root->color = BLACK;
    
    // assert check();
}
    
/*-----------------------------------------------------------*/
/*
 *  get(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *
 *     - se KEY está em ST, RETORNA NULL;
 *
 *     - se KEY não está em ST, RETORNA uma cópia/clone do valor
 *       associado a KEY.
 * 
 */
void *get(RedBlackST st, const void *key){
    
    //public Value get(Key key) 
    if (key == NULL) ERROR("argument to get() is NULL");
    //return get_value(st->root, key);
    return NULL;
}


// value associated with the given key in subtree rooted at x; NULL if no such key
/*
.......get_value(Node x, Key key) {
    while (x != NULL) {
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x = x.left;
        else if (cmp > 0) x = x.right;
        else              return x.val;
    }
    return NULL;
}
*/



/*-----------------------------------------------------------*/
/* 
 *  CONTAINS(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 * 
 *  RETORNA TRUE se KEY está na ST e FALSE em caso contrário.
 *
 */
Bool contains(RedBlackST st, const void *key){


    return get(st, key) != NULL;
    //return FALSE;
}


/*-----------------------------------------------------------*/
/* 
 *  SIZE(ST)
 *
 *  RECEBE uma tabela de símbolos ST.
 * 
 *  RETORNA o número de itens (= pares chave-valor) na ST.
 *
 */
int size(RedBlackST st){
    // number of node in subtree rooted at x; 0 if x is NULL
    return size_aux(st->root);
}


/*-----------------------------------------------------------*/
/* 
 *  ISEMPTY(ST, KEY)
 *
 *  RECEBE uma tabela de símbolos ST.
 * 
 *  RETORNA TRUE se ST está vazia e FALSE em caso contrário.
 *
 */
Bool isEmpty(RedBlackST st){
    return st->root == NULL;
}


/*-----------------------------------------------------------*/
/*
 *  RANK(ST, KEY)
 * 
 *  RECEBE uma tabela de símbolos ST e uma chave KEY.
 *  RETORNA o número de chaves em ST menores que KEY.
 *
 *  Se ST está vazia RETORNA NULL.
 *
 */
int rank(RedBlackST st, const void *key){

    /*public int rank(Key key) {
        if (key == NULL) throw new IllegalArgumentException("argument to rank() is NULL");
        return rank(key, root);
    } 

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        if (x == NULL) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.left); 
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right); 
        else              return size(x.left); 
    } 
    */
    return 0;
} 


/*-----------------------------------------------------------*/
/*
 *  SELECT(ST, K)
 * 
 *  RECEBE uma tabela de símbolos ST e um inteiro K >= 0.
 *  RETORNA a (K+1)-ésima menor chave da tabela ST.
 *
 *  Se ST não tem K+1 elementos RETORNA NULL.
 *
 */
void *select(RedBlackST st, int k){

    /*public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        Node x = select(root, k);
        return x.key;
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != NULL;
        // assert k >= 0 && k < size(x);
        int t = size(x.left); 
        if      (t > k) return select(x.left,  k); 
        else if (t < k) return select(x.right, k-t-1); 
        else            return x; 
    } */

    return NULL;
}

/*-----------------------------------------------------------*/
/* 
 *  KEYS(ST, INIT)
 * 
 *  RECEBE uma tabela de símbolos ST e um Bool INIT.
 *
 *  Se INIT é TRUE, KEYS() RETORNA uma cópia/clone da menor chave na ST.
 *  Se INIT é FALSE, KEYS() RETORNA a chave sucessora da última chave retornada.
 *  Se ST está vazia ou não há sucessora da última chave retornada, KEYS() retorna NULL.
 *
 *  Se entre duas chamadas de KEYS() a ST é alterada, o comportamento é 
 *  indefinido. 
 *  
 */
void * keys(RedBlackST st, Bool init){
    return NULL;
}



/*------------------------------------------------------------*/
/* 
 * Funções administrativas
 */

/***************************************************************************
 *  Utility functions.
 ***************************************************************************/

/*
 * HEIGHT(ST)
 * 
 * RECEBE uma RedBlackST e RETORNA a sua altura. 
 * Uma BST com apenas um nó tem altura zero.
 * 
 */
int height(RedBlackST st)
{
        return 0;
}


/***************************************************************************
 *  Check integrity of red-black tree data structure.
 ***************************************************************************/

/*
 * CHECK(ST)
 *
 * RECEBE uma RedBlackST ST e RETORNA TRUE se não encontrar algum
 * problema de ordem ou estrutural. Em caso contrário RETORNA 
 * FALSE.
 * 
 */
Bool check(RedBlackST st){
    /*

    if (!isBST(st))            ERROR("check(): not in symmetric order");
    if (!isSizeConsistent(st)) ERROR("check(): subtree counts not consistent");
    if (!isRankConsistent(st)) ERROR("check(): ranks not consistent");
    if (!is23(st))             ERROR("check(): not a 2-3 tree");
    if (!isBalanced(st))       ERROR("check(): not balanced");
    return isBST(st) && isSizeConsistent(st) && isRankConsistent(st) && is23(st) && isBalanced(st);

    */
    return FALSE;
}


/* 
 * ISBST(ST)
 * 
 * RECEBE uma RedBlackST ST.
 * RETORNA TRUE se a árvore é uma BST.
 * 
 */
static Bool isBST(RedBlackST st) {
    /*
    private boolean isBST() {
        return isBST(root, NULL, NULL);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is NULL, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == NULL) return true;
        if (min != NULL && x.key.compareTo(min) <= 0) return false;
        if (max != NULL && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    } 
    */
    return FALSE;
}


/* 
 *  ISSIZECONSISTENT(ST) 
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se para cada nó h
 *  vale que size(h) = 1 + size(h->left) + size(h->right) e 
 *  FALSE em caso contrário.
 */
static Bool isSizeConsistent(RedBlackST st) {
    /*
    // are the size fields correct?
    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == NULL) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    } 
    */
    return FALSE;
}


/* 
 *  ISRANKCONSISTENT(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se seus rank() e
 *  select() são consistentes.
 */  
/* check that ranks are consistent */
static Bool isRankConsistent(RedBlackST st) {
    /*// check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }*/
    return FALSE;
}

/* 
 *  IS23(ST)
 *
 *  RECEBE uma RedBlackST ST e RETORNA FALSE se há algum link RED
 *  para a direta ou se ha dois links para esquerda seguidos RED 
 *  Em caso contrário RETORNA TRUE (= a ST representa uma árvore 2-3). 
 */
static Bool is23(RedBlackST st){
    /*
    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == NULL) return true;
        if (isRed(x.right)) return false;
        if (x != root && isRed(x) && isRed(x.left))
            return false;
        return is23(x.left) && is23(x.right);
    } 
    */
    return FALSE;
}


/* 
 *  ISBALANCED(ST) 
 * 
 *  RECEBE uma RedBlackST ST e RETORNA TRUE se st satisfaz
 *  balanceamento negro perfeiro.
 */ 
static Bool isBalanced(RedBlackST st){
    /*
    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        Node x = root;
        while (x != NULL) {
            if (!isRed(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        if (x == NULL) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.left, black) && isBalanced(x.right, black);
    } 
    */
    return FALSE;
}


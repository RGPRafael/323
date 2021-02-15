/****************************************************************
    Nome:Rafael Gonçalves Pereira da Silva
    NUSP:9009600

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
      Aparentemente leva muito tempo para verificar quais sao as
      permutalções validas quando o n é maior ou igual a 11, provavelmente devido
      ao algoritmo implementado dado ser simples e ingenuo mas que foi a unica ideia
      que surgiu, conclui-se que se tivesse ido a monitorias talvez esse ep tivesse
      sido feito de maneira mais satisfatoria.

****************************************************************/

/******************************************************************************
 *  Compilation:  javac-algs4 STPerms.java
 *  Execution:    java STPerms n s t opcao
 *
 *  Enumera todas as (s,t)-permutações das n primeiras letras do alfabeto.
 *  As permutações devem ser exibidas em ordem lexicográfica.
 *  Sobre o papel da opcao, leia o enunciado do EP.
 *
 *  % java STPerms 4 2 2 0
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  % java STPerms 4 2 2 1
 *  4
 *  % java STPerms 4 2 2 2
 *  badc
 *  bdac
 *  cadb
 *  cdab
 *  4
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

public class STPerms {
      private static int count = 0; // contador de combinações
      private static int opcao = 0;
      // 0 imprimir as combinações e o número de combinações (default)
      // 1 imprimir apenas o número de combinações
      //public  static String perm1(String s) { return perm1("", s); }

      public static void printc(char[] c){
        StdOut.print("--> : ");
        for(int j = 0; j < c.length; j++)StdOut.print(c[j]);
        StdOut.println();
      }

      public static String StringToChar(int n ){
        String s1 = "abcdefghijklmnopqrstuvwxyz";
        s1 = s1.substring(0,n);
        return(s1);
      }

      public static int VerificaC(char[] c){
        int i = 0;
        for(int j = 0, k = 1; k< c.length; j++, k++){
          if(c[j] < c[k])i++;
        }
        if(i == c.length-1)return(1);
        return(0);
      }

      public static int VerificaD(char[] c){
        int i = 0;
        for(int j = 0, k = 1; k< c.length; j++, k++){
          if(c[j] > c[k])i++;
        }
        if(i == c.length-1) return(1);
        return(0);
      }

      public static int substringC ( String str, char []c, int left, int right, int pos, int k){
        int var = 0;
        if( pos == k){
          if(VerificaC(c) != 0) return(1);
      	}
        for(int i = left; i <= right && pos < k; i++){
          c[pos] = str.charAt(i);
          var = var + substringC(str, c, i+1, right, pos+1, k);
        }
        return(var);
      }

      public static int substringD(String str, char []c, int left, int right, int pos, int k){
        int contador = 0;
        if( pos == k){
          if(VerificaD(c)!= 0) return(1);
        }
        for(int i = left; i <= right && pos < k; i++){
          c[pos] = str.charAt(i);
          contador = contador + substringD(str, c, i+1, right, pos+1, k);
        }
        return(contador);
      }

      public static void permutacao(String p, int s, int t) {
        permutacao("", p, s, t);

      }
      private static void permutacao(String prefix, String p, int s,int t) {
        int n = p.length();
        if (n == 0){
          char[] c = new char[s+1];
          char[] d = new char[t+1];
          int var = substringC(prefix, c, 0, prefix.length() - 1, 0, s + 1);
          int var1 = substringD(prefix, d, 0, prefix.length() - 1, 0, t + 1);
          if(var == 0 && var1 == 0 ){
            if(opcao != 1) StdOut.println(prefix);
            count++;
          }
        }
        else{
          for (int i = 0; i < n; i++)
            permutacao(prefix + p.charAt(i), p.substring(0, i) + p.substring(i+1, n), s, t);
        }
      }
      public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int s = Integer.parseInt(args[1]);
        int t = Integer.parseInt(args[2]);
        opcao = Integer.parseInt(args[3]);
        String c = StringToChar(n);
        permutacao(c,s,t);
        if(opcao != 0 )StdOut.println(count);
    }
}

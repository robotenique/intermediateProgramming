import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class MeuSeparateChainingHashST<Key, Value> {
    //Esses valores são todas as possíveis dimensões da tabela de hash.
    private static final int[] PRIMES = {
        7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
        32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
        8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
        536870909, 1073741789, 2147483647
    };
    // capacidade inicial
    private static final int INIT_CAPACITY = PRIMES[0];
    // limite inferior default para o fator de carga
    private static final double ALFAINF_DEFAULT = 2;
    // limite superior default para o fator de carga
    private static final double ALFASUP_DEFAULT = 10;
    // NOTA: indice na tabela de primos correspondente ao valor de 'm'
    private int iPrimes = 0;
    //       alfaSup é o limite superior para o fator de carga.
    //       Usado no método put().
    private final double alfaSup;
    //       alfaInf é o limite inferior para o fator de carga.
    //       Usado no método delete().
    private final double alfaInf;
    private int n; // number of key-value pairs
    private int m; // hash table size

    private SequentialSearchST<Key, Value>[] st; // array of linked-list symbol tables

    public MeuSeparateChainingHashST() {
        this(INIT_CAPACITY, ALFAINF_DEFAULT, ALFASUP_DEFAULT);
    }
    public MeuSeparateChainingHashST(int m) {
        this(m, ALFAINF_DEFAULT, ALFASUP_DEFAULT);
    } 

    public MeuSeparateChainingHashST(double alfaInf, double alfaSup) {
        this(INIT_CAPACITY, alfaInf, alfaSup);
    } 
    
    /** 
     * Construtor.
     *
     * Cria uma tabela de hash vazia com PRIMES[iPrimes] listas sendo
     * que iPrimes >= 0 e
     *         PRIMES[iPrimes-1] < m <= PRIMES[iPrimes]
     * (suponha que PRIMES[-1] = 0).
     * 
     * Além disso a tabela criada será tal que o fator de carga alfa
     * respeitará
     *
     *            alfaInf <= alfa <= alfaSup
     *
     * A primeira desigualdade pode não valer quando o tamanho da tabela
     * é INIT_CAPACITY.
     *
     * Pré-condição: o método supõe que alfaInf < alfaSup.
     */

    private int bSearch(int[] arr, int lo, int hi, int x){
        if(lo > hi)
            return hi;
        int mid = (lo + hi)/2;
        if(x < arr[mid]) return bSearch(arr, lo, mid  - 1, x);
        else if(x > arr[mid]) return bSearch(arr, mid + 1, hi, x);
        return mid;
    }

    public MeuSeparateChainingHashST(int m, double alfaInf, double alfaSup) {
        iPrimes = calculatePRIMESpos(m);
        this.m = PRIMES[iPrimes];
        this.alfaSup = alfaSup;
        this.alfaInf = alfaInf;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
        for (int i = 0; i < m; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    }

    private int calculatePRIMESpos(int m) {
        int k =  bSearch(PRIMES, 0, PRIMES.length, m);
        if(PRIMES[k] != m && k + 1 == PRIMES.length)
            throw new UnsupportedOperationException("The size is too big!");
        return PRIMES[k] == m ? k : k + 1;
    }

    private void resize(int k) {
        int length = k == -1 ? 0 : PRIMES[k];
        MeuSeparateChainingHashST<Key, Value> temp = new MeuSeparateChainingHashST<>(length, alfaInf, alfaSup);
        for (int i = 0; i < m; i++)
            for (Key key : st[i].keys())
                temp.put(key, st[i].get(key));
        this.m  = temp.m;
        this.n  = temp.n;
        this.iPrimes = temp.iPrimes;
        this.st = temp.st;
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % m;
    } 

    public int size() {
        return n;
    } 

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    } 

    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    private double alpha() {
        double t = (float)n;
        return t/m;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
        if (alpha() > alfaSup) resize(iPrimes+1);
        int i = hash(key);
        if (!st[i].contains(key)) n++;
        st[i].put(key, val);}

    // delete key (and associated value) if key is in the table
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        int i = hash(key);
        if (st[i].contains(key)) n--;
        st[i].delete(key);
        if (m > INIT_CAPACITY && alpha() < alfaInf) resize(iPrimes - 1);
    } 

    // return keys in symbol table as an Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    } 

    public int sizeST() {
        return m;
    } 

    public int maxLista() {
        int max = 0;
        for (int i = 0; i < st.length; i++)
            if(max < st[i].size())
                max = st[i].size();
        return max;
    }

    /** Exercício 3.4.30 S&W
     *  TAREFA
     *  Teste do Chi quadrado (chi-square statistic).
     *  Este método deve retorna o valor de chi^2 dado por
     *
     *  (m/n)((f_0-n/m)^2 + (f_1-n/m)^2 + ... + (f_{m-1}-n/m)^2)
     * 
     *  onde f_i é o número de chaves na tabela com valor de hash i.
     *  Este mecanismo fornece uma maneira de testarmos a hipótese 
     *  de que a função de hash produz valores aleatórios. 
     *  Se isto for verdade, para n > c*m, o valor calculado deveria 
     *  estar no intervalo [m-sqrt(n),m+sqrt(n)] com probabilidade 1-1/c  
     */
    public double chiSquare() {
        double sum = 0;
        double y = alpha();
        for (int i = 0; i < m; i++)
            sum += Math.pow(st[i].size() - y, 2);
        return (1.0/y)*sum;
    }
    
   /***********************************************************************
    *  Unit test client.
    *  Altere à vontade, pois este método não será corrigido.
    ***********************************************************************/
   public static void main(String[] args) {
       if (args.length != 3) {
           showUse();
           return;
       }

       String s;
       double alfaInf = Double.parseDouble(args[0]);
       double alfaSup = Double.parseDouble(args[1]);
       String fileName = args[2];

       //=========================================================
       // Testa SeparateChainingHashST
       In in = new In(fileName);

       // crie a ST
       SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();

       // dispare o cronometro
       Stopwatch sw = new Stopwatch();

       // povoe a ST com palavras do arquivo
       StdOut.println("Criando a ALGS4 com as palavras do arquivo '" + args[2] + "' ...");
       while (!in.isEmpty()) {
           // Read and return the next line.
           String linha = in.readLine();
           String[] chaves = linha.split("\\W+");
           for (int i = 0; i < chaves.length; i++) {
               if (!st.contains(chaves[i])) {
                   st.put(chaves[i], 1);
               }
               else {
                   st.put(chaves[i], st.get(chaves[i])+1);
               }
           }
       }

       StdOut.println("Hashing com ALGS4");
       StdOut.println("ST criada em " + sw.elapsedTime() + " segundos");
       StdOut.println("ST contÃ©m " + st.size() + " itens");
       in.close();

       //=================================================================================
       StdOut.println("\n=============================================");

       // reabra o arquivo
       in = new In(fileName);

       // crie uma ST
       MeuSeparateChainingHashST<String, Integer> meuST = new MeuSeparateChainingHashST<String, Integer>(alfaInf, alfaSup);
       // dispare o cronometro
       sw = new Stopwatch();

       // povoe  a ST com palavras do arquivo
       StdOut.println("Criando a SUA com as palavras do arquivo '" + args[2] + "' ...");
       while (!in.isEmpty()) {
           // Read and return the next line.
           String linha = in.readLine();
           String[] chaves = linha.split("\\W+");
           for (int i = 0; i < chaves.length; i++) {
               if (!meuST.contains(chaves[i])) {
                   meuST.put(chaves[i], 1);
               }
               else {
                   meuST.put(chaves[i], meuST.get(chaves[i])+1);
               }
           }
       }

       // sw.elapsedTime(): returns elapsed time (in seconds) since
       // this object was created.
       int n = meuST.size();
       int m = meuST.sizeST();
       double chi2 = meuST.chiSquare();
       StdOut.println("Hashing com SUA");
       StdOut.println("ST criada em " + sw.elapsedTime() + " segundos");
       StdOut.println("ST contém " + n + " itens");
       StdOut.println("Tabela hash tem " + m + " listas");
       StdOut.println("Maior comprimento de uma lista " + meuST.maxLista());
       StdOut.println("Fator de carga (= n/m) = " + (double) n/m);
       StdOut.printf("Chi^2 = %.2f, [m-sqrt(m),m+sqrt(m)] = [%.2f, %.2f]\n",
               chi2, (m-Math.sqrt(m)), (m+Math.sqrt(m)));

       in.close();

       // Hmm. NÃ£o custa dar uma verificada ;-)
       for (String key: st.keys()) {
           if (!st.get(key).equals(meuST.get(key))) {
               StdOut.println("Opss... " + key + ": " + st.get(key) + " != " + meuST.get(key));
           }
       }
   }


    private static void showUse() {
        String msg = "Uso: meu_prompt> java MeuSeparateChainingHashST <alfa inf> <alfa sup> <nome arquivo>\n"
                + "    <alfa inf>: limite inferior para o comprimento mÃ©dio das listas (= fator de carga)\n"
                + "    <alfa sup>: limite superior para o comprimento mÃ©dio das listas (= fator de carga)\n"
                + "    <nome arquivo>: nome de um arquivo com um texto\n"
                + "          um ST serÃ¡ criada com as palavras do texto.";
        StdOut.println(msg);
    }
}



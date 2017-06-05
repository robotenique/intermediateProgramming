import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Queue;

public class MeuTST<Value extends Comparable<Value>> {
    
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    public MeuTST() {
    }

    public int size() {
        return n;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (val == null) {
            if(contains(key))
                delete(key); // MAC0323
            // throw new IllegalArgumentException("calls put() with null key"); MAC0323
        }
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }


    /**
     *  TAREFA: keysWithPrefixByValue():
     * 
     *  Cria e retorna uma coleção iterável de strings.
     *
     *  A coleção deve conter as strings que tem 'prefix' como 
     *  prefixo. Além disso os strings na coleção devem estar 
     *  em ordem decrescente de valor (val). 
     * 
     *  Sinta-se a vontade para: 
     *
     *     - criar métodos auxiliares; 
     *     - criar classes auxiliares; e
     *     - usar classes do algs4, e nesse caso não deixe 
     *       de colocar o import correspondente.
     *
     */

    // aux Class
    private class KeyVal implements Comparable<KeyVal>{
        public String key;
        public Value val;

        public KeyVal(String key, Value val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public int compareTo(KeyVal that) {
            if (this == that) return 0;
            return this.val.compareTo(that.val);
        }
    }

    // all keys starting with given prefix, ordered by value!
    public Iterable<String> keysWithPrefixByValue(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        MaxPQ<KeyVal> mpq = new MaxPQ<>();
        Node<Value> x = get(root, prefix, 0);
        if(x != null) {
            if (x.val != null) mpq.insert(new KeyVal(prefix, x.val));
            collect(x.mid, new StringBuilder(prefix), mpq);
        }
        Queue<String> queue = new Queue<>();
        for(KeyVal k : mpq)
            queue.enqueue(k.key);
        return queue;
    }

    // Modified collect that adds into a PriorityQueue
    private void collect(Node<Value> x, StringBuilder prefix, MaxPQ<KeyVal> mpq) {
        if (x == null) return;
        collect(x.left,  prefix, mpq);
        if (x.val != null) mpq.insert(new KeyVal(prefix.toString() + x.c, x.val));
        collect(x.mid,   prefix.append(x.c), mpq);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, mpq);
    }
    
    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.enqueue(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }

    
    public void delete(String key) {
        if(key == null)
            throw new NullPointerException("Null key to delete()!");
        root = delete(root, key, 0);
    }

    // Recursively deletes a key in the Trie
    private Node<Value> delete(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (d+1 == key.length()) {
            if (x.val != null) n--;
            x.val = null;
        }
        else {
            char c = key.charAt(d);
            if(c == x.c)
                x.mid = delete(x.mid, key, d+1);
            else if(c < x.c)
                x.left = delete(x.left, key, d);
            else
                x.right = delete(x.right, key, d);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        if(x.mid == null && x.left == null && x.right == null)
            return null;
        else
            return x;
    }


    /**
     * Unit tests the {@code TST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // read in the terms from a file

        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        MeuTST<Long> terms = new MeuTST<Long>();
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();  // read the next weight
            in.readChar();                // scan past the tab
            String query = in.readLine(); // read the next query
            terms.put(query, weight);     // construct the term
        }
        
        StdOut.println(terms.size() + " itens (= pares chave-valor) na TST");
        StdOut.println("Teste interativo. Digite algo e tecle ENTER. Tecle crtl+d para encerrar,");
        // read in queries from standard input and print out the matching terms
        StdOut.print(">>> ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Iterable<String> results = terms.keysWithPrefix(prefix);
            StdOut.println("----------------------");
            for (String key : results)
                StdOut.println("   '" + key + "' : " + terms.get(key));
            StdOut.println("----------------------");
            Iterable<String> resultsValue = terms.keysWithPrefixByValue(prefix);
            for (String key : resultsValue)
                StdOut.println("   '" + key + "' : " + terms.get(key));
            StdOut.print(">>> ");
        }


        // teste delete()
        StdOut.println("\niniciando teste de delete()...");
        MeuTST<Integer> st = new MeuTST<Integer>();
        st.put("are",12);
        st.put("by",4);
        st.put("sea",14);
        st.put("sells",11);
        st.put("she",10);
        st.put("shells",15);
        st.put("shore",7);
        st.put("surely",13);
        st.put("sur",0);
        st.put("the",8);
        
        StdOut.println(st.size() + " itens: ");
        for (String key : st.keys())
            StdOut.println("   '" + key + "' : " + st.get(key));
        
        st.delete("sea");
        StdOut.println("\n"+ st.size() + " itens depois de remover 'sea': ");
        for (String key : st.keys())
            StdOut.println("  '" + key + "' : " + st.get(key));

        st.delete("sea");
        StdOut.println("\n" + st.size() + " itens depois de remover 'sea' novamente: ");
        for (String key : st.keys())
            StdOut.println("   '" + key + "' : " + st.get(key));

        st.delete("are");
        st.delete("the");
        st.delete("by");
        StdOut.println("\n" + st.size() + " itens depois de remover 'are', 'the', 'by': ");
        for (String key : st.keys()) {
            StdOut.println("   '" + key + "' : " + st.get(key));
            st.delete(key);
        }

        StdOut.println("\n" + st.size() + " itens depois de remover... tudo: ");
        for (String key : st.keys()) {
            StdOut.println("   '" + key + "' : " + st.get(key));
        }

        StdOut.println("fim dos testes.");
    }
}

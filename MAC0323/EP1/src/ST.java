/******************************************************************************
 *  Juliano Garcia de Oliveira
 *  Compilação:  javac -Xlint ST.java
 *  Execução:    java -ea ST filename
 *  Dependências:
 *
 *  Programa lê o arquivo dado como argumento e cria um dicionário (= tabela de
 *  símbolos) em que as chaves são as palavras (String) no arquivo e o valor
 *  associado a cada chave é o número de ocorrências da palavra no arquivo (int).
 *
 *  O dicionário deve ser implementado através de um array de strings para as
 *  chaves e um array de inteiros para os valores. Implemente apenas busca
 *  remoção e inserção SEQUÊNCIAS, tudo a là MAC0110.
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ST {
    private String [] keys;
    private int [] values;
    private int n = 0;
    private int total = 0;
    private int max;
    private int countDeleted;
    // cria um dicionário vazio
    public ST() {
        keys = new String[2];
        values = new int[2];
        max = 2;
    }

    // Returns the value associated with the given key if the key is in the symbol table
    // and -1 if the key is not in the symbol table
    // Throws java.lang.NullPointerException - if key is null
    public int get(String key) {
        if (key == null) {
            throw new java.lang.NullPointerException("ST.get(): key is null");
        }
        for (int i = 0; i < n; i++)
            if(keys[i] != null && keys[i].equals(key))
                return values[i];

        return -1;
    }

    // Returns the index of the string 'key' in the keys array
    // If not in the array, return -1
    public int getPos(String key) {
        for (int i = 0; i < n; i++)
            if(keys[i] != null && keys[i].equals(key))
                return i;
        return -1;
    }

    // Insert the key into the symbol table and increase its value
    // Throws java.lang.NullPointerException - if key is null
    public void put(String key) {
        if (key == null) {
            throw new java.lang.NullPointerException("ST.put(): key is null");
        }
        int pos = getPos(key);
        // If the key isn't in the ST
        if(pos < 0) {
            if (n == max)
                resize(2 * n);
            keys[n] = key;
            values[n]++;
            n++;
            total++;
        }
        else
            values[pos]++;

    }


    // Removes the key and associated value from the symbol table (if the key is in the symbol table).
    // Throws java.lang.NullPointerException - if key is null
    public void delete(String key) {
        if (key == null) {
            throw new java.lang.NullPointerException("ST.delete(): key is null");
        }
        for (int i = 0; i < n; i++) {
            // If the number of deleted elements in greater then 3/4 of the max number of elements,
            // It will resize down the ST.
            if(keys[i] != null && keys[i].equals(key)) {
                keys[i] = null;
                values[i] = 0;
                countDeleted++;
                total--;
                if(countDeleted >= 0.75*max) {
                    countDeleted = 0;
                    resize(max / 2 + 1);
                }
                return;
            }
        }
    }

    // Does this symbol table contain the given key?
    // Throws  java.lang.NullPointerException - if key is null
    public boolean contains(String key) {
        if (key == null) {
            throw new java.lang.NullPointerException("ST.contains(): key is null");
        }
        for (String s : keys)
            if (s != null && s.equals(key))
                return true;

        return false;
    }

    // Returns the number of key-value pairs in this symbol table
    public int size() {
        return total;
    }

    // Is this symbol table empty?
    // Returns: true if this symbol table is empty and false otherwise
    public boolean isEmpty() {
        return total == 0;
    }

    // Returns the largest (= maior frequência) key in the symbol table
    // Throws java.util.NoSuchElementException - if the symbol table is empty
    public String max() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("ST.max(): Symbol Table is empty");
        int best = 0;
        int pos = 0;
        for (int i = 0; i < n; i++) {
            if(values[i] > best) {
                best = values[i];
                pos = i;
            }
        }
        return keys[pos];
    }

    // Returns a string representing the symbol table
    // Este string é usado quando utilizamos StdOut.print*() para exibir a tabela
    // Veja como um cliente utiliza este método no main()
    public String toString() {
        // To get better performance
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        for (int i = 0; i < n; i++) {
            if(keys[i] != null) {
                buf.append("'" + keys[i] + "' : " + values[i]);
                if (i != n - 1)
                    buf.append(",");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    // Return an array with the keys of the ST
    public String [] keys() {
        String [] top = new String[this.size()];
        for (int i = 0, j = 0; i < n; i++) {
            if(keys[i] != null)
                top[j++] = keys[i];
        }
        return top;
    }


    // move the symbol table to one of size k
    private void resize(int k) {
        String [] newKeys = new String[k];
        int [] newValues = new int[k];
        int j = 0;
        for (int i = 0; i < n; i++) {
            if(keys[i] != null) {
                newKeys[j] = keys[i];
                newValues[j++] = values[i];
            }
        }
        keys = newKeys;
        values = newValues;
        n = j;
        max = k;
    }

}



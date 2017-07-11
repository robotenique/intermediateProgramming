The **Burrows-Wheeler compression algorithm** consists of three algorithmic components, which are applied in succession:

1. **Burrows-Wheeler transform**. Given a typical English text file, transform it into a text file in which sequences of the same character occur near each other many times.

2. **Move-to-front encoding**. Given a text file in which sequences of the same character occur near each other many times, convert it into a text file in which certain characters appear more frequently than others.

3. **Huffman compression**. Given a text file in which certain characters appear more frequently than others, compress it by encoding frequently occurring characters with short codewords and rare ones with long codewords.


COMPRESS A FILE
---------------
To compress a given file "example.txt", run in the terminal:

```shell
$ java-algs4 BurrowsWheeler - < example.txt | java-algs4 MoveToFront - | java-algs4 edu.princeton.cs.algs4.Huffman  - > example.zipped```

UNCOMPRESS A FILE
-----------------
To decode a file, we just have to apply the inverse transformations in the inverse order:
```shell
$ java-algs4 edu.princeton.cs.algs4.Huffman + < example.zipped | java-algs4 MoveToFront + | java-algs4 BurrowsWheeler + > example.unzipped```

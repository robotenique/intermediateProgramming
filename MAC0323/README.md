# Algorithms and Data Structures

*We use the **algs4** library by Sedgewick & Wayne from a Princeton course.*

Here's a summary of each EP (programming exercise):

1. **ST.java**
    Implement a simple symbol table in Java using simple arrays;

2. **BulgingSquares.java**
    Implement a program to draw the following optical illusion, using StdDraw;
    ![bulging](https://s-media-cache-ak0.pinimg.com/564x/ee/82/58/ee8258f5118325b9b32e6deb7fff2941.jpg)

3. **LinkedListST.java**
    Implements an ordered symbol table using a linked list;

4. **Tail.java**
    Implements the [Tail](http://man7.org/linux/man-pages/man1/tail.1.html) application, to read the *n-th* last lines of file(s);

5. **Percolation**
    Implements a percolation model to check if a given ***n** x **n*** matrix percolates. It uses a union find data structure and has a visualizer to show the percolation interactively. This assignment can be seen [here!](http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/percolation.html)

6. **Deque and Randomized queues**
    Implements two simple data structures using array and doubly linked list, and a unit test. The assignment is [here!](http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/queues.html)

7. **8-puzzle Solver**
    Implements modelling and an AI to solve the 8-puzzle (actually any N-puzzle, depending on the time you want to wait :D). The main files developed are the 'Solver.java' and 'Board.java'. In my Solver I implemented the classic A* search algorithm, and the IDA* algorithm which is somewhat faster in bigger cases and don't use as much memory as the A* version. This assignment is [here!](http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/8puzzle.html)
    ![8puzzle](https://www.tjhsst.edu/~rlatimer/assignments2004/Prolog/week13-tut_files/8p2.jpg)

8. **Kd-Trees**

    Implements a Kd-Tree, a data structure similar to a symbol table but the keys are points in the 2D plane, and provides optimized operations to search nearest neighbors of a given point, and rectangle bounding of the point. The main files implemented are 'KdTreeST.java' and 'PointST.java'. PointST is a symbol table which have the same operations but it's implementations are brute force using a **Red Black Tree**. The time comparations of each type of implementation are in the 'readme.txt'. The 'BoidSimulator.java' uses the nearest neighbor operation to simulate birds flying, very cool :D. This assignment is described [here!](http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/kdtree.html)

    ![kdtree](http://homes.ieu.edu.tr/hakcan/projects/kdtree/files/kdtree.jpg)

9. **WordFinder.java**
    Implements a Word related counter using Hash tables. It receives an array of Strings, and the supported operations are: **getMax()** : Returns the word that is more frequent in the given Strings; **containedIn(a, b)** : Returns a word that appears both in the index *a* and in the index *b* of the initial String array; **appearsIn(s)** : Returns an array with the indexes of the initial array in which the string *s* appears.

10. **Hash collision: Separate Chaining**
    Implements a separate chaining Hash Table with methods to calculate the longest list in the chains, the average number of search hits and search miss, etc.

11. **Hash collision: Linear Probing**
    Implements a linear probing Hash Table with methods to calculate the largest cluster formed, the average number of search hits and search miss, etc.

12. **MeuTST.java**
    Implementation of a ternary search trie to work with Strings autocompletion. The ternary tries have the ability to delete words beyond the common search operations. The 'AutocompleteGUI.java' uses this ternary trie and a file to show the autocomplete working.

    ![ternarytrie](http://www.redditmirror.cc/cache/websites/www.pcplus.co.uk_8tw1x/www.pcplus.co.uk/files/pcp_images/PCP282theoryfigure2.png)

13. **Binary tree: BST x Red Black Trees**
    Implementation of a BST and a Red Black Tree with extra methods to compute the average number of nodes visited to insert a new element, and the average number of nodes visited when searching an existing element in the trees. These methods provide useful statistics to compare both implementations (the classic BST and the Red Black one).

    ![redblack](https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Red-black_tree_example.svg/500px-Red-black_tree_example.svg.png)

14. **Seam Carving**
    Implementation of the **Seam Carving** technique for resizing images. It basically calculates the energy of each pixel of the given image, then find (using Dijkstra / topological search) the *shortest path* with the minimum energy from one side to other of the image, then remove it. It's a very interesting assignment, and the main implementation is in the file *SeamCarver.java*. The complete assignment description is [here!](http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/seamCarving.html)

    ![seams](https://jeremykun.files.wordpress.com/2013/02/glacier_canyon_h_shr_seams.jpeg)

    My implementation of Seam Carving uses the topological sort to find the shortest path in a DAG (directed acyclic graph):

    ![dagsort](https://www.cs.rit.edu/~ark/351/dp/fig04.png)


15. **CBSPaths.java**

    In this assignment, we must find and print all safe cities for the executives of an industry to make a reunion. We receive by *stdin*: the number of cities (N), the number of flights (M) from city to city and the number of executives (K). Then, in the M following lines, we receive the list of fligts in the form : "fromCity" "toCity" time; And then, in the K following lines, the list of cities where the K executives start, and finally the last line is the start city from the police. A city is considered safe if *every* executive can get to that city in time *before* the police gets there, even if by just 1 unit of time. We measure the time by considering the flight time only. It is guaranteed that the cities and flights form a [Strong connected graph](http://mathworld.wolfram.com/StronglyConnectedDigraph.html). The solution I implemented is to calculate a Dijkstra from every starting city of the executives and compare to all cities. If the longest time of all the executives is less than the time from the police to get to a given city, then the city is safe for the reunion.

    ![strongly](https://upload.wikimedia.org/wikipedia/commons/5/5c/Scc.png)

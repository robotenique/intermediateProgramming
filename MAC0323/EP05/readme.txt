/******************************************************************************
 *  Name: Juliano Garcia de Oliveira
 *  NetID:
 *  Precept:
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Operating system: Debian 9 Stretch
 *  Compiler: javac-algs4
 *  Text editor / IDE: IntelliJ IDEA
 *
 *  Have you taken (part of) this course before: não
 *  Have you taken (part of) the Coursera course Algorithm, Part I: não
 *
 *  Hours to complete assignment (optional):
 *
 ******************************************************************************/


/******************************************************************************
 *  Describe how you implemented Percolation.java. How did you check
 *  whether the system percolates?
 *****************************************************************************/
Percolation.java foi implementado com apenas dois Union Find, um para calcular
a percolação em si, e outro apenas para impedir 'backwash'. A cada vez que um
sítio é aberto, é feita a conexão no union find se o vizinho dele também está
aberto. Foram criados 2 sítios virtuais (virt1 e virt2), e todos os sítios da
primeira linha que estão abertos são ligados ao virt1, e todos os sítios da
última linha são ligados ao virt2. Para a função "isFull" basta verificar se
um dado sítio está conectado ao topo (virt1) usando uma union find auxiliar,
e para verificar se o sistema percola basta verificar se virt1 e virt2 estão 
conectados;


/******************************************************************************
 *  Using Percolation with QuickFindUF.java,  fill in the table below such that
 *  the N values are multiples of each other.

 *  Give a formula (using tilde notation) for the running time (in seconds) of
 *  PercolationStats.java as a function of both N and T. Be sure to give both
 *  the coefficient and exponent of the leading term. Your coefficients should
 *  be based on empirical data and rounded to two significant digits, such as
 *  5.3*10^-8 * N^5.0 T^1.5.
 *****************************************************************************/

(keep T constant)
T = 50
 N          time (seconds)
------------------------------
50              0.121
100             1.285
150             5.489
200             22.333
250             44.719


(keep N constant)
N = 150
 T          time (seconds)
------------------------------
50              5.305
100             10.524
150             18.298
200             24.247
250             30.875


running time as a function of N and T:  ~TN⁴


/******************************************************************************
 *  Repeat the previous question, but use WeightedQuickUnionUF.java.
 *****************************************************************************/

(keep T constant)
T = 50
 N          time (seconds)
------------------------------
50              0.046
100             0.093
150             0.158
200             0.275
250             0.43


(keep N constant)
N = 150
 T          time (seconds)
------------------------------
50              0.172
100             0.287
150             0.421
200             0.494
250             0.619

running time as a function of N and T:  ~TN²

/**********************************************************************
 *  How much memory (in bytes) does a Percolation object (which uses
 *  WeightedQuickUnionUF.java) use to store an N-by-N grid? Use the
 *  64-bit memory cost model from Section 1.4 of the textbook and use
 *  tilde notation to simplify your answer. Briefly justify your
 *  answers.
 *
 *  Include the memory for all referenced objects (deep memory).
 **********************************************************************/

A memória é ~ 72 + 12n² bytes. O Percolation possui dois Union Find, que ocupa
32 + (n² + 2)*4 bytes, 4 inteiros (16 bytes), uma matriz de inteiros (n²) e
um overhead de 16 bytes.

/******************************************************************************
 *  After reading the course collaboration policy, answer the
 *  following short quiz. This counts for a portion of your grade.
 *  Write down the answers in the space below.
 *****************************************************************************/
1. c
2. b
3. b

1. How much help can you give a fellow student taking COS 226?
(a) None. Only the preceptors and lab TAs can help.
(b) You can discuss ideas and concepts but students can get help
    debugging their code only from a preceptor, lab TA, or
    student who has already passed COS 226.
(c) You can help a student by discussing ideas, selecting data
    structures, and debugging their code.
(d) You can help a student by emailing him/her your code.

2. What are the rules when partnering?
 (a) You and your partner must both be present while writing code.
     But after that only one person needs to do the analysis.
 (b) You and your partner must both be present while writing code
     and during the analysis, but, after that, only one person
     needs to be present while submitting the code and the
     readme.
 (c) You and your partner must both be present while writing code,
     during the analysis, and while submitting the code and the
     readme. Failure to do so is a violation of the course
     collaboration policy.

3. For any programming assignment, I am permitted to use code that
   I found on a website other than the COS 226 or algs4 website
   (e.g., on GitHub or StackOverflow):
 (a) Only when the online code was written by a person who does not
     and did not go to Princeton.
 (b) Only when the online code implemented different assignment
     specifications than the ones I'm currently working on.
 (c) Always, because online code is available to everyone.
 (d) Never, because such websites are an impermissible "outside
     source".

/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/




/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/


/******************************************************************************
 *  Describe any serious problems you encountered.
 *****************************************************************************/
Nenhum



/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 *****************************************************************************/
Bem legal de fazer, principalmente pra ficar desenhando na PercolationVisualizer.

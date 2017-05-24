/******************************************************************************
 *  Name: Juliano Garcia de Oliveira
 *  NetID: 9277086
 *  Precept:
 *
 *  Partner Name:
 *  Partner NetID:
 *  Partner Precept:
 *
 *  Hours to complete assignment (optional):
 *  8
 ******************************************************************************/



/******************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 *****************************************************************************/
O node é o sugerido pelo assignment. Cada node contém o ponto p, o valor val,
o retângulo que aquele ponto engloba, e ponteiros para os nós dá esquerda e direita.
Além disso, cada nó tem um campo para descrever a profundidade.
/******************************************************************************
 *  Describe your method for range search in a kd-tree.
 *****************************************************************************/
Os pontos internos ao retângulo recebido como argumento são armazenados em uma fila.
Começando pela raiz, o range search verifica se o retângulo passado como argumento
intercepta o retângulo representado pelo nó atual. Se sim, ele verifica se o retângulo
recebido como argumento contém o ponto do nó atual, e então adiciona o ponto se ele
estiver contido. Depois, ele chama recursivamente a função para o nó esquerdo
e para o nó direito.

/******************************************************************************
 *  Describe your method for nearest neighbor search in a kd-tree.
 *****************************************************************************/
Seja 'closest' o ponto mais próximo de p até o momento. No início, closest tem o
valor do ponto da raiz, e a busca funciona da seguinte forma:
Se o nó atual é nulo, retorna closest. Senão, ele verifica se a distância de p
até closest é maior que a distância do ponto do nó atual até p é maior. Se é maior,
atualizamos closest para receber o ponto atual.
Em seguida verificamos se a distância entre o closest e p é maior que a distância
do retângulo que engloba o ponto atual até p. Se for, verificamos se estamos em um
nível par da árvore. Se estamos em um nível par da árvore, verificamos se a coordenada
x de p é menor que a coordenada x do ponto atual, e se for fazemos a busca recursivamente
visitando primeiro a subárvore esquerda e depois a direita. Senão, fazemos na ordem contrária.
Se estamos em uma profundidade ímpar, fazemos a mesma coisa porém comparando os
valores de y.
Após isso, temos certeza que closest possui o ponto mais próximo, então retornamos o
closest.
/******************************************************************************
 *  Using the 64-bit memory cost model from the textbook and lecture,
 *  give the total memory usage in bytes of your 2d-tree data structure
 *  as a function of the number of points N. Use tilde notation to
 *  simplify your answer (i.e., keep the leading coefficient and discard
 *  lower-order terms).
 *
 *  Include the memory for all referenced objects (including
 *  Node, Point2D, and RectHV objects) except for Value objects
 *  (because the type is unknown). Also, include the memory for
 *  all referenced objects.
 *
 *  Justify your answer below.
 *
 *****************************************************************************/

bytes per Point2D: 32 bytes

bytes per RectHV: 32 bytes

bytes per KdTree of N points:   ~44+n*116

A classe em si: 16(overhead) + 8(ref. p/ root) + 4 (tamanho) + 16 (constantes)
Cada node : 16(overhead) + 40(ponto + ref.) + 40(rect + ref.) + 16(ref. p/ filhos) + 4(profundidade)



/******************************************************************************
 *  How many nearest neighbor calculations can your brute-force
 *  implementation perform per second for input100K.txt (100,000 points)
 *  and input1M.txt (1 million points), where the query points are
 *  random points in the unit square? Show the math how you used to determine
 *  the operations per second. (Do not count the time to read in the points
 *  or to build the 2d-tree.)
 *
 *  Repeat the question but with the 2d-tree implementation.
 *****************************************************************************/

                       calls to nearest() per second
                     brute force               2d-tree
                     ---------------------------------
input100K.txt              195                  22222

input1M.txt                9                    33898

Usei a classe Stopwatch para calcular o tempo. Fiz a operação nearest em
ambos usando 2000 pontos pré calculados e armazenados em uma fila, e no
final dividi 2000/t onde t é o tempo de execução calculado pelo stopwatch.

/******************************************************************************
 *  Known bugs / limitations.
 *****************************************************************************/
-

/******************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 *****************************************************************************/
-

/******************************************************************************
 *  Describe any serious problems you encountered.
 *****************************************************************************/
-

/******************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/
-





/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 *****************************************************************************/
-

/******************************************************************************
 *  Name: Juliano Garcia de Oliveira
 *  NetID: -
 *  Precept: -
 *
 *  Partner Name: -
 *  Partner NetID: -
 *  Partner Precept: -
 *
 *  Hours to complete assignment (optional): 4
 *
 ******************************************************************************/



/******************************************************************************
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 *****************************************************************************/
 Para a deque, eu utilizei uma lista duplamente ligada, simplesmente para manter a
 operação removeLast constante. Com uma lista duplamente ligada eu consigo recuperar o
 último penúltimo elemento e torná-lo o novo final da lista em tempo constante.
 A implementação da deque é simples, basta retirar o item do "b"(egin) ou do "e"(nd),
 e arrumar os ponteiros.

 Para implementar a randomized queue, usei um array, já que com uma lista ligada não
 consigo acessar diretamente um elemento, e então não seria tempo constante. Para
 inserir é simples, basta colocar na próxima posição livre. Já quando um item é removido,
 simplesmente ele é trocado pelo último item do array, e o indicador da próxima posição
 livre volta 1 posição, fazendo com que seja tempo constante.

/******************************************************************************
 *  How much memory (in bytes) do your data types use to store N items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 *****************************************************************************/

Randomized Queue:   ~  24 + 8n    bytes

Deque:              ~   20 + 64n bytes


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
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 *****************************************************************************/



/******************************************************************************
 *  Describe any serious problems you encountered.
 *****************************************************************************/







/******************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 *****************************************************************************/

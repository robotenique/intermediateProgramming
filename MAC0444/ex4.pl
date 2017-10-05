homem( americo ).
homem( daniel ).
homem( paulo ).
homem( carlos ).
homem( joaquim ).
homem( filipe ).
homem( garibaldo ).
mulher( teresa ).
mulher( sonia ).
mulher( ana ).
mulher( carla ).
mulher( barbara ).
mulher( maria ).
idade( garibaldo, 82).
idade( americo , 18).
idade( daniel , 60).
idade( paulo , 25).
idade( carlos , 37).
idade( joaquim , 80).
idade( filipe , 32).
idade( teresa , 18).
idade( sonia , 28).
idade( ana , 17).
idade( carla , 26).
idade( barbara , 51).
idade( maria , 79).
irmaos( americo , paulo ).
irmaos( carlos , sonia ).
irmaos( daniel, stalin ).
pai( carlos , teresa ).
pai( daniel , americo ).
pai( daniel , paulo ).
pai( joaquim , daniel ).
pai( stalin, lenin ).
mae(sonia, quimuleke).
mae( maria , daniel ).
mae( barbara , ana ).
casados( filipe , carla ).
casados( americo , teresa ).
casados( joaquim , maria ).
avof(M, P) :- mae(M, X), mulher(M), pai(X, P).
avof(M, P) :- mae(M, X), mulher(M), mae(X, P).
avom(H, P) :- pai(H, X), homem(H), pai(X, P).
avom(H, P) :- pai(H, X), homem(H), mae(X, P).
bisavof(M, P) :- mulher(M), mae(M, K), avof(K, P).
bisavof(M, P) :- mulher(M), mae(M, K), avom(K, P).
bisavom(H, P) :- homem(H), pai(H, K), avof(K, P).
bisavom(H, P) :- homem(H), pai(H, K), avom(K, P).
% primo_1 is a symmetric predicate
primo_1(P1, P2) :- P1 @< P2,  primo_1(P2, P1).
primo_1(P1, P2) :- pai(K, P1), pai(J, P2), irmaos(K, J).
primo_1(P1, P2) :- pai(K, P1), mae(J, P2),  irmaos(K, J).
primo_1(P1, P2) :- mae(K, P1), pai(J, P2), irmaos(K, J).
primo_1(P1, P2) :- mae(K, P1), mae(J, P2), irmaos(K, J).
% primo is a symmetric predicate
primo(A, B) :- A @< B, primo(B, A).
primo(A, B) :- pai(PA, A), pai(PB, B), primo(PA, PB).
primo(A, B) :- pai(PA, A), mae(MB, B), primo(PA, MB).
primo(A, B) :- mae(MA, A), pai(PB, B), primo(MA, PB).
primo(A, B) :- mae(MA, A), mae(MB, B), primo(MA, MB).
primo(A, B) :- primo_1(A, B).
maior_de_idade(P) :- idade(P, K), K >= 18.
% pessoas
pessoas(L) :- findall(P, homem(P) ; mulher(P), L).
% mais_velho
mais_velho(P) :- pessoas(L), max(L, P), !.
max([X],X) :- !.
max([X|Xs], X):- max(Xs,Y), idade(X, IDX), idade(Y, IDY), IDX >= IDY.
max([X|Xs], N):- max(Xs,N), idade(N, IDN), idade(X, IDX), IDN > IDX.
%lista_pessoas
lista_pessoas(L, S) :- match_mulher(S), findall(idade(P, K), (mulher(P), idade(P, K)), L), !.
lista_pessoas(L, S) :- match_homem(S), findall(idade(P, K), (homem(P), idade(P, K)), L), !.
match_mulher(S) :- S == m.
match_homem(S)  :- S == h.

result([_, E | L], [E | M]) :- !, result(L, M).
result(_, []).



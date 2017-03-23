#lang plai-typed
;The data structure - The core language
(define-type ArithC
    [numC (n : number)]
    [plusC (l : ArithC) (r : ArithC)]
    [multC (l : ArithC) (r : ArithC)])
; New Data structure - The surface language
(define-type ArithS
    [numS (n : number)]
    [plusS (l : ArithS) (r : ArithS)]
    [subS (l : ArithS) (r : ArithS)]
    [multS (l : ArithS) (r : ArithS)]
    [uminuS (e : ArithS)])
;The parser itself - TODO: Change it to READ the expression, and use ArithS
(define (parse [s : s-expression]) : ArithC
    (cond
        [(s-exp-number? s) (numC (s-exp->number s))]
        [(s-exp-list? s)
            (let ([sl (s-exp->list s)])
                (case (s-exp->symbol (first sl))
                    [(+) (plusC (parse (second sl)) (parse (third sl)))]
                    [(*) (multC (parse (second sl)) (parse (third sl)))]
                    [else (error 'parse "INVALID LIST INPUT!")]))]
        [else (error 'parse "INVALID INPUT!")]))
;The interpreter
(define (interp [a : ArithC]) : number
    (type-case ArithC a
        [numC (n) n]
        [plusC (l r) (+ (interp l) (interp r))]
        [multC (l r) (* (interp l) (interp r))]))
;Desugar Function a - b =  a + (-1*b)
(define (desugar [as : ArithS]) : ArithC
    (type-case ArithS as
        [numS (n) (numC n)]
        [plusS (l r) (plusC (desugar l) (desugar r))]
        [multS (l r) (multC (desugar l) (desugar r))]
        [subS (l r) (plusC (desugar l) (multC (numC -1) (desugar r)))]
        [uminuS (e) (multC (numC -1) (desugar e)) ]))

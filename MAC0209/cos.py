import math as m
import matplotlib.pyplot as plt

def calcErr(a, b):
    '''
    a, b - lists of the form [[t], [v], [x]]
    '''
    dif1 = dif2 = 0
    err = [[],[],[]]
    err[0] = list(a[0])
    for t, va, xa, vb, xb in zip(a[0], a[1], a[2], b[1], b[2]):
        err[1].append(abs(va - vb))
        err[2].append(abs(xa - xb))
        dif1 += (va - vb)**2 # Velocity difference
        dif2 += (xa - xb)**2 # Position difference
    print("\n###=====================================###")
    print("DESVIO PADRAO VELOCIDADE = ", m.sqrt(dif1/len(a[0])))
    print("DESVIO PADRAO POSIÇÕES = ", m.sqrt(dif2/len(a[0])))
    print("###=====================================###\n")
    return err


def plotGraph(eoRes, erRes, aRes, func):
    plt.figure(figsize=(10,6))
    func(eoRes[2], eoRes[1], label = "Euler Method")
    func(erRes[2], erRes[1], label = "Euler Richardson Method")
    func(aRes[2], aRes[1], label = "Analytical Method")
    plt.tight_layout(pad=3.08)
    plt.legend(loc="upper left")
    plt.title("Euler / Euler Cromer Method vs Analytic solution", fontsize=15)
    plt.show()
4

def main():
    '''
    User input
    v = v0 = float(input("Digite o valor de v0: "))
    t = t0 = float(input("Digite o valor de t0: "))
    y = y0 = float(input("Digite o valor de y0: "))
    deltaT = float(input("Digite o valor de deltaT: ")) #Seconds
    finalT = int(input("Digite o tempo final: "))
    '''
    # Manual input
    k = 1
    t = t0 = 0
    y = y0 = 0
    deltaT = 0.01 # Seconds
    finalT = 100


    ecRes = [[],[],[]]
    aRes = [[],[],[]]
    eoRes = [[],[],[]]
    erRes = [[],[],[]]

    # Euler solution
    while t <= finalT:
        y += m.cos(t)*deltaT
        t += deltaT
        eoRes[0].append(0)
        eoRes[1].append(y)
        eoRes[2].append(t)

    t = t0
    y = y0

    # Euler-Richardson solution
    while t <= finalT:
        y += m.cos(t + deltaT/2)*deltaT
        t += deltaT
        erRes[0].append(0)
        erRes[1].append(y)
        erRes[2].append(t)


    t = t0
    # Analytical solution
    #deltaT = 0.0001
    while t <= finalT:
        t += deltaT
        aRes[1].append(m.sin(t))
        aRes[2].append(t)

    initial = 0
    dif1 = 0
    dif2 = 0

    plotGraph(eoRes, erRes, aRes, plt.plot)
    #err = calcErr(ecRes, aRes)
    #plt.plot(err[0], err[2], label="Erro")
    plt.show()
if __name__ == '__main__':
    main()

'''
Approximate the equations in analytical form
'''
import matplotlib.pyplot as plt

def solveEq():
    initialX = 0
    finalX = 100
    deltaX = 0.5
    eRes = list([list(), list()])
    aRes = list([list(), list()])
    calcEuler(deltaX, initialX, finalX, eRes)
    calcAnalytic(deltaX, initialX, finalX, aRes)
    func = plt.plot if len(eRes[0]) < 2e4 else plt.scatter
    plotGraph(eRes, aRes, func)

def calcEuler(deltaX, x,  finalX, stateVector):
    y = 0
    while x <= finalX:
        y += 2*x*deltaX
        stateVector[0].append(x)
        stateVector[1].append(y)
        x += deltaX

def calcAnalytic(deltaX, x,  finalX, stateVector):
    y = 0
    while x <= finalX:
        y = x**2
        stateVector[0].append(x)
        stateVector[1].append(y)
        x += deltaX

def plotGraph(eRes, aRes, func):
	plt.figure(figsize=(10,6))
	func(eRes[0], eRes[1], label = "Euler Method")
	func(aRes[0], aRes[1], label = "Analytical Method")
	plt.legend(loc="upper left")
	plt.tight_layout(pad=3.08)
	plt.title(f"Euler Method vs Analytic solution "r"$\frac{dy}{dx} = 2x$", fontsize=15)
	plt.show()


def main():
    #alt = int(input("Equação (1 ou 2): "))
    solveEq()
if __name__ == '__main__':
    main()

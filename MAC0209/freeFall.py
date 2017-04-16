'''
Free fall simulator:
Simulates free fall and compares the results by using the Euler Method
and the analytical solution, calculating a plot of both curves and
printing the standart deviation.

Euler Method = https://en.wikipedia.org/wiki/Euler_method

'''

from __future__ import print_function
import sys
import math
import matplotlib.pyplot as plt
# Constant
g = 9.80665

def posA(y0, v0, t):
	return y0 + v0*t - (g*(t**2))/2


def velA(v0, t):
	return v0 - g*t

def eprint(*args, **kwargs):
	print(*args, file=sys.stderr, **kwargs)

def plotGraph(eRes, aRes, func):
	plt.figure(figsize=(10,6))
	func(eRes[0], eRes[2], label = "Euler Method")
	func(aRes[2], aRes[0], label = "Analytical Method")
	plt.legend(loc="upper left")
	plt.tight_layout(pad=3.08)
	plt.title("Euler Method vs Analytic solution", fontsize=30)
	plt.show()


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
	v = v0 = 10
	t = t0 = 0
	y = y0 = 100
	deltaT = 0.8 #Seconds
	finalT = 10


	eRes = [[t],[v],[y]]
	aRes = [[y],[v],[t]]

	while t <= finalT:
		y += v*deltaT
		v -= g*deltaT
		t += deltaT
		eRes[0].append(t)
		eRes[1].append(v)
		eRes[2].append(y)



	t = 0
	# Analytical solution
	while t <= finalT:
		aRes[0].append(posA(y0, v0, t))
		aRes[1].append(velA(v0, t))

		t += deltaT
		aRes[2].append(t)

	initial = 0
	dif1 = 0
	dif2 = 0
	for t, v, y, y1, v1, t1 in zip(eRes[0], eRes[1], eRes[2], aRes[0], aRes[1], aRes[2]):
		initial += 1
		dif1 += (v - v1)**2 # Velocity difference
		dif2 += (y - y1)**2 # Position difference


	print("\n###=====================================###\n")
	print("DESVIO PADRAO VELOCIDADE = ", math.sqrt(dif1/initial))
	print("DESVIO PADRAO POSIÇÕES = ", math.sqrt(dif2/initial))
	print("\n###=====================================###\n")
	# :)
	plotGraph(eRes, aRes, plt.plot if len(eRes[0]) < 2e4 else plt.scatter)

if __name__ == '__main__':
	main()

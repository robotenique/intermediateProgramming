'''
Euler-cromer and Euler algorithm simulating the Simple
Harmonic Motion!

eRes = tuple with the states of the Euler Cromer algorithm
eoRes = tuple with the states of the classic Euler algorithm
aRes = tuple with the states of the analytical calculation
'''

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


def posA(k, x0, v0, t):
	omega = m.sqrt(k) # unit mass
	p = x0*m.cos(omega*t) + (v0/omega)*m.sin(omega*t)
	return p

def velA(k, x0, v0, t):
	omega = m.sqrt(k) # unit mass
	v = -x0*omega*m.sin(omega*t) + v0*m.cos(omega*t)
	return v


def plotGraph(eoRes, eRes, aRes, func):
	plt.figure(figsize=(10,6))
	func(eoRes[0], eoRes[2], label = "Euler Method")
	func(eRes[0], eRes[2], label = "Euler Cromer Method")
	func(aRes[0], aRes[2], label = "Analytical Method")
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
	v = v0 = 0
	t = t0 = 0
	x = x0 = 1
	deltaT = 0.0001 # Seconds
	finalT = 10


	eRes = [[],[],[]]
	aRes = [[],[],[]]
	eoRes = [[],[],[]]

	# Euler-cromer solution
	while t <= finalT:
		v -= k*x*deltaT
		x += v*deltaT
		t += deltaT
		eRes[0].append(t)
		eRes[1].append(v)
		eRes[2].append(x)

	t = t0
	v = v0
	x = x0
	# Euler solution
	while t <= finalT:
		x += v*deltaT
		v -= k*x*deltaT
		eoRes[1].append(v)
		eoRes[2].append(x)
		t += deltaT
		eoRes[0].append(t)


	t = t0
	# Analytical solution
	#deltaT = 0.0001
	while t <= finalT:
		ini_tuple = (k, x0, v0, t)
		aRes[0].append(t)
		aRes[1].append(velA(*ini_tuple))
		aRes[2].append(posA(*ini_tuple))
		t += deltaT


	initial = 0
	dif1 = 0
	dif2 = 0

	plotGraph(eoRes, eRes, aRes, plt.plot)
	err = calcErr(eRes, aRes)
	plt.plot(err[0], err[2], label="Erro")
	plt.show()
if __name__ == '__main__':
	main()

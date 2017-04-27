'''
Euler-cromer and Euler algorithm simulating the Simple
Harmonic Motion!

ecRes = tuple with the states of the Euler Cromer algorithm
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


def plotGraph(eoRes, ecRes, erRes, aRes, func):
	plt.figure(figsize=(10,6))
	func(eoRes[0], eoRes[2], label = "Euler Method")
	func(ecRes[0], ecRes[2], label = "Euler cromer Method")
	func(erRes[0], erRes[2], label = "Euler Richardson Method")
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
	v = v0 = 300
	t = t0 = 0
	x = x0 = 1
	deltaT = 0.1 # Seconds
	finalT = 10


	ecRes = [[],[],[]]
	aRes = [[],[],[]]
	eoRes = [[],[],[]]
	erRes = [[],[],[]]

	# Euler solution
	while t <= finalT:
		x += v*deltaT
		v -= k*x*deltaT
		t += deltaT
		eoRes[1].append(v)
		eoRes[2].append(x)
		eoRes[0].append(t)

	t = t0
	v = v0
	x = x0

	# Euler-Cromer solution
	while t <= finalT:
		v -= k*x*deltaT
		x += v*deltaT
		t += deltaT
		ecRes[0].append(t)
		ecRes[1].append(v)
		ecRes[2].append(x)

	t = t0
	v = v0
	x = x0


	# Euler-Richardson solution
	while t <= finalT:
		vA = v - k*x*(1/2)*deltaT
		xA = x + vA*(1/2)*deltaT
		v -= k*xA*deltaT
		x += vA*deltaT
		t += deltaT
		erRes[0].append(t)
		erRes[1].append(v)
		erRes[2].append(x)


	t = t0
	# Analytical solution
	#deltaT = 0.0001
	while t <= finalT:
		t += deltaT
		ini_tuple = (k, x0, v0, t)
		aRes[0].append(t)
		aRes[1].append(velA(*ini_tuple))
		aRes[2].append(posA(*ini_tuple))


	initial = 0
	dif1 = 0
	dif2 = 0

	plotGraph(eoRes, ecRes, erRes, aRes, plt.plot)
	err = calcErr(ecRes, aRes)
	plt.plot(err[0], err[2], label="Erro")
	plt.show()
if __name__ == '__main__':
	main()

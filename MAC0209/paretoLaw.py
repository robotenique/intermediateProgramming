'''
Problem 1.1 of the book 'Introduction to Computer Simulation Methods',
chapter one: Distribution of Money
'''
__author__      = "Juliano Garcia de Oliveira"

import random as rnd
import matplotlib.pyplot as plt
import numpy as np
def createAgents(n, m0):
    # Create a dictionary with n pairs of 'agent : m0', and return it
    agents = dict(((x,m0) for x in range(n)))
    return agents

def executeTransactions(n, agents, t):
    '''
    Run a transaction t times. Each transaction does:
    -> Choose a random pair of agents
    -> They trade their money by a random amount
    '''
    for x in range(t):
        i = rnd.choice(list(agents.keys()))
        j = rnd.choice(list(agents.keys()))
        while i == j:
            j = rnd.choice(list(agents.keys()))
        money = agents[i] + agents[j]
        change = rnd.random()
        agents[i] = change*(money)
        agents[j] = (1 - change)*(money)

    return agents

def executeTransactionFrac(n, agents, t, frac):
    '''
    Run a transaction t times. Each transaction does:
    -> Choose a random pair of agents
    -> They trade their money by a random amount but save some money
    '''
    for x in range(t):
        i = rnd.choice(list(agents.keys()))
        j = rnd.choice(list(agents.keys()))
        while i == j:
            j = rnd.choice(list(agents.keys()))
        money = agents[i] + agents[j]
        change = rnd.random()
        deltaM = (1 - frac)*(change*agents[j] - (1 - change)*agents[i])
        agents[i] += deltaM
        agents[j] -= deltaM

    return agents




def showBarplot(agents):
    ab = dict()
    n = len(list(agents.keys()))
    ind = np.arange(n)
    fig, ax = plt.subplots()
    rect = ax.bar(ind, list(agents.values()))
    plt.show()

def showPiechart(agents, m0):
    cat1 = sum((1 for i in list(agents.values()) if i > 0.75*m0))
    cat2 = sum((1 for i in list(agents.values()) if i > 0.50*m0 and i <= 0.75*m0))
    cat3 = sum((1 for i in list(agents.values()) if i > 0.25*m0 and i <= 0.50*m0))
    cat4 = sum((1 for i in list(agents.values()) if i <= 0.25*m0))
    labels = 'Above 75%', 'From 50% to 75%', 'From 25% to 50%', 'Below 25%'
    sizes = [cat1, cat2, cat3, cat4]
    fig1, ax1 = plt.subplots()
    ax1.pie(sizes, labels=labels, autopct='%1.1f%%', shadow=True, startangle=90)
    ax1.axis('equal')
    plt.show()

def main():
    n = int(input("N = "))
    m0 = float(input("m0 = "))
    times = int(input("times = "))
    # Pareto Law - Simple Model
    t1 = createAgents(n, m0)
    t1 = executeTransactions(n, t1, times)
    showBarplot(t1)
    showPiechart(t1, m0)
    # Pareto Law - Savings Model
    frac = float(input("Fraction = "))
    t1 = createAgents(n, m0)
    t1 = executeTransactionFrac(n, t1, times, frac)
    showBarplot(t1)
    showPiechart(t1, m0)


if __name__ == '__main__':
    main()

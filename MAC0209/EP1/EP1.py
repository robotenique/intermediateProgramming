import matplotlib.cm as cm
import sys
from itertools import islice, chain
import json
import numpy as np
import matplotlib.pyplot as plt

# Number of lines for each JSON entry
ENTRY_LENGTH = 8
class Walker:
    # Constant / static variable - MAY BE USEFUL..
    SPACE = 30

    '''
    Walker attributes:

        name = the name of the Walker
        movType = "MRU" ou "MRUV"
        times = A dict with the movements!
    '''
    def __init__(self, name, movType, times):
        self.name = name
        self.movType = movType
        self.times = times


    def getVelocity(self):
        # Calcuate the mean velocity of a given Walker in a run
        meanVel = sum((self.__calculateVelocity(run) for run in self.times))/len(self.times)
        return meanVel

    def __calculateVelocity(self, run):
        msr = [float(m) for m in run['measures'].split("|") if m != '']
        # Alternate measure
        if(run['mType'] == 'A'):
            return sum((i/t for i, t in zip(range(5, 31, 5), msr)))/len(msr)
        else:
            # Takes the mean of both time measures of the same pos
            msr = [(i+j)/2 for i, j in zip(msr[:len(msr)//2],msr[len(msr)//2:])]
            # Makes deltaS/deltaT for every t and space, then takes the mean
            #print (sum((i/t for i, t in zip(range(10, 31, 10), msr)))/len(msr))
            return sum((i/t for i, t in zip(range(10, 31, 10), msr)))/len(msr)

    def __spaceF(self, run):
        simVel = 30/self.__finalTime(run)
        def f(t):
            return simVel*t
        return f

    def __timeList(self, run):
        msr = [float(m) for m in run['measures'].split("|") if m != '']
        if(run['mType'] == 'N'):
            msr = [(i+j)/2 for i, j in zip(msr[:len(msr)//2],msr[len(msr)//2:])]
        return msr

    def __spaceList(self, run):
        t = "t"
        if(run['mType'] == 'N'):
            spcList = list(range(10,35, 10))
            xticks = [t+str(i) for i in range(1,4)]
        else:
            spcList = list(range(5, 35, 5))
            xticks = [t+str(i) for i in range(1,7)]


        return [spcList, xticks]


    def __finalTime(self, run):
        msr = self.__timeList(run)
        return msr[len(msr) - 1]





    def plotGraph(self):
        for run in self.times:
            xt = self.__spaceF(run)
            x = np.arange(0, self.__finalTime(run), 0.3)
            y = list(map(xt, x))
            realX = self.__timeList(run)
            realY = self.__spaceList(run)[0]
            timeNames = self.__spaceList(run)[1]
            labelDict = {tValue : tName for tValue, tName in zip(realX, timeNames)}
            xticks = [int(i) for i in np.arange(0, self.__finalTime(run), 5)]
            xticks = sorted(xticks + list(labelDict.keys()))
            labels = [str(i) for i in xticks]
            labels = [labelDict.get(float(t), t) for t in labels]
            x
            colors = cm.rainbow(np.linspace(0, 1, len(xticks)))
            f, axarr = plt.subplots(2)
            axarr[0].plot(x, y)
            for i, j in zip(list(labelDict.keys()), realY):
                axarr[0].plot([i, i], [0, j], 'r--')
            axarr[0].scatter(realX, realY,color='red', marker="+")
            [t.set_color(i) for (i,t) in zip(['red']*len(labelDict), list(labelDict.values()))]

            axarr[0].set_title(run["csv"])
            axarr[0].set_xticklabels(labels)
            axarr[0].set_xticks(xticks)
            plt.show()

            #axarr[1].scatter(x, y)





'''
def fileBlock(f, n):

    # A generator which returns a block of 'n' lines of
    # a given file 'f', each time it is called, using iterators

    for line in f:
        yield ''.join(chain([line], islice(f, n - 1)))
'''


def addWalker(w, listWalkers):
    newWalker = Walker(w['walker'], w['movType'], w['times'])
    listWalkers.append(newWalker)

def main():
    # Parse a json file passed as argument
    if(len(sys.argv) == 2 and sys.argv[1][-5:] == ".json"):
        data = sys.argv[1]
    else:
        raise ValueError("You need to pass a single json file as argument!")

    listWalkers = list()

    with open(data) as f:
        for block in (''.join(chain([line], islice(f, ENTRY_LENGTH))) for line in f):
            w = json.loads(block)
            addWalker(w, listWalkers)

    for walker in listWalkers:
        print(f"Mean Velocity ({walker.name} - {walker.movType}) = {walker.getVelocity():{3}.{5}} m/s")
        walker.plotGraph()


if __name__ == '__main__':
    main()

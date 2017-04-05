from pathlib import Path
import pandas as pd
import matplotlib.pyplot as plt
import sys

class Walker:
    # Constant / static variable
    SPACE = 30

    '''
    Walker attributes:

        name = the name of the Walker
        movType = "MRU" ou "MRUV"
        times = A dict with the movements!
        ### THIS IS OLD ###
        timeMethod = "N" (normal) or "A" (alternate)
        isAlt = True if it's alternate measure, False Otherwise
        times = if isAlt:
                times = (a5, b10, a15, b20, a25, b30)
                else times =  ([a10, a20, a30], [b1, b20, b30])

        OBS: Pay attention to "times" type!
    '''
    def __init__(self, name, movType, timeMethod, isAlt, times):
        self.name = name
        self.movType = movType
        self.timeMethod = timeMethod
        self.times = times
        self.isAlt = isAlt

    def meanVelocity(self):
        if(self.isAlt)
            self.meanVelocityAlt()
        # Method : take the median between the two measures
        timeTaken = [(t + y)/2 for t, y in zip(self.times[0], self.times[1])]
        # Velocity in each point
        vel = [i/t for i, t in zip(range(10, 35, 10), timeTaken)]
        # Mean velocity
        meanVel = sum(vel)/len(vel)
        print(timeTaken)
        print(vel)
        print(meanVel)

    def meanVelocityAlt(self):
        # Velocity in each point
        vel = [i/t for i, t in zip(range(5, 35, 5), self.times)]
        # Mean Velocity
        meanVel = sum(vel)/len(vel)
        print(self.times)
        print(vel)
        print(meanVel)

def parseData(data):
    '''
    Parse a 'pup' file, and return a dict
    '''
    walker = ''
    for line in data:
        if()

def main():
    # Parse a pup file passed as argument
    if(len(sys.argv) == 2 and sys.argv[1][-4:] == ".pup")
        data = open(data, 'r')
    else
        raise ValueError("You need to pass a single \'.pup\' file as argument!")

    data = [line.split("\n") for line in data]
    dictWalkers = parseData(data)




if __name__ == '__main__':
    main()

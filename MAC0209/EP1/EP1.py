import sys
from itertools import islice, chain
import json

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
            #print (sum((i/t for i, t in zip(range(5, 31, 5), msr)))/len(msr))
            return sum((i/t for i, t in zip(range(5, 31, 5), msr)))/len(msr)
        else:
            # Takes the mean of both time measures of the same pos
            msr = [(i+j)/2 for i, j in zip(msr[:len(msr)//2],msr[len(msr)//2:])]
            # Makes deltaS/deltaT for every t and space, then takes the mean
            #print (sum((i/t for i, t in zip(range(10, 31, 10), msr)))/len(msr))
            return sum((i/t for i, t in zip(range(10, 31, 10), msr)))/len(msr)

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

if __name__ == '__main__':
    main()

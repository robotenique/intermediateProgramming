#python version = 3.6
import sys
import json
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
def main():
    mVel = mAcc = cMRU = cMRUV = 0
    # Parse a json file passed as argument
    if(len(sys.argv) == 2 and sys.argv[1][-5:] == ".json"):
        data = sys.argv[1]
    else:
        raise ValueError("You need to pass a single json file as argument!")

    #Creates a list of Walkers objects
    listWalkers = list()
    with open(data) as f:
        jsonData = json.loads(f.read())
    for j in jsonData:
        addWalker(j, listWalkers)

    #for each walker, plot the graphs and the info
    for w in listWalkers:
        cMRU += 1 if w.movType == "MRU" else 0
        cMRUV += 1 if w.movType == "MRUV" else 0
        mVel += w.getVelocity() if w.movType == "MRU" else 0
        mAcc += w.getAcceleration() if w.movType == "MRUV" else 0
        w.plotGraph()
        plotInfo(w = w)

    #Plot the mean acceleration and the mean velocity of everyone
    mVel /= cMRU
    mAcc /= cMRUV
    plotInfo(cTitle = "Resumo MRU", cMsg = f"Velocidade Média: {round(mVel, 3)} m/s")
    plotInfo(cTitle = "Resumo MRUV", cMsg = f"Aceleração Média: {round(mAcc, 3)} m/s²")


class Walker:
    '''
    Walker represents a person's experiment, and it's read from the json.
        -name = the name of the Walker
        -movType = "MRU" ou "MRUV"
        -times = A dict with the movements!
    '''
    # Class variables
    SPACE = 30
    XLIM = 60

    def __init__(self, name, movType, times):
        self.name = name
        self.movType = movType
        self.times = times

    def __estimateIniVel(self, run):
        '''
        Solves the system of equations
        ┌ a*t1^2 + b*t1 = s1
        ┤
        └ a*t2^2 + b*t2 = s2
        for a given run, finds the values for a and b,
        and returns the value of b
        '''
        times = self.__timeList(run)
        t1 = times[0]
        t2 = times[-1]
        s1 = 10 if run["mType"] == "N" else 5
        s2 = 30
        a = (s2*t1-s1*t2)/(t1*t2*(t2-t1))
        b = (s2-a*t2**2)/t2
        return b

    def __spaceF(self, run):
        '''
        Calculates a space funtion of a run. A space function f(t)
        is a function that returns the space associated with a given t.

        Note that this returns a closure.
        '''
        tf = self.__finalTime(run)
        if self.movType == "MRU":
            simVel = Walker.SPACE/tf
            return lambda t: simVel*t
        else:
            v0 = self.__estimateIniVel(run)
            simAccel = 2*(Walker.SPACE-v0*tf)/tf**2
            return lambda t: v0*t + simAccel*t**2/2
        return f

    def __velocityF(self, run):
        '''
        Calculates the velocity function v(t), that for a given t
        returns the velocity of that time.

        Note that this returns a closure.
        '''
        tf = self.__finalTime(run)
        v0 = self.__estimateIniVel(run)
        simAccel = 2*(Walker.SPACE-v0*tf)/tf**2
        return lambda t:simAccel*t

    def __timeList(self, run):
        '''
        Return the list of observed times. The length may vary
        if it's an Alternate or Normal read.
        '''
        msr = [float(m) for m in run['measures'].split("|") if m != '']
        #Takes the average of opposing time measurements
        if(run['mType'] == 'N'):
            msr = [(i+j)/2 for i, j in zip(msr[:len(msr)//2],msr[len(msr)//2:])]
        return msr

    def __spaceList(self, run):
        '''
        Return a list of spaces (e.g. [10, 20, 30]) and a list of space
        xticks (e.g. [t1, t2, t3]). The length changes if it's Alternate.
        '''
        if(run['mType'] == 'N'):
            spcList = list(range(10,35, 10))
            xticks = ["t"+str(i) for i in range(1,4)]
        else:
            spcList = list(range(5, 35, 5))
            xticks = ["t"+str(i) for i in range(1,7)]

        return [spcList, xticks]

    def __finalTime(self, run):
        '''
        Returns the final time of a run, given by the csv data.
        '''
        fTime = run["tcsv"][1] - run["tcsv"][0]
        return fTime

    def __plotCsv(self, run, axis):
        '''
        Receives a run, and plot the CSV in a given axis, using
        the pandas library.

        It removes labels, set a xlim, and add the legend.
        '''
        file = "./CSV/" + run['csv'] + ".csv"
        labels = ['Tempo', 'Fx', 'Fy', 'Fz', 'Fr']
        df = pd.read_csv(file, names=labels, sep=';', decimal=',')
        df.plot(ax=axis, x = 'Tempo')
        axis.set_yticklabels([])
        axis.set_xlabel("")
        axis.spines['right'].set_visible(False)
        axis.spines['top'].set_visible(False)
        axis.spines['bottom'].set_visible(False)
        axis.spines['left'].set_visible(False)
        axis.axis('off')
        axis.legend(loc='upper left')
        axis.set_xlim(0, np.max(np.asarray(df['Tempo'])))

    def __setAnnotations(self, **kwargs):
        '''
        Plot the measured times in the graph, with an arrow
        indicating each of them
        '''
        # Get arguments
        xObs = kwargs['xObs']
        yObs = kwargs['yObs']
        labels = kwargs['labels']
        values = kwargs['values']
        ax = kwargs['ax']
        # Observed time annotation
        for x, y, text, val in zip(xObs, yObs, labels, values):
            ax.annotate(text+f" ({round(val, 2)})",
            xy=(x, y + 1), xycoords='data',
            xytext=(-15, 25), textcoords='offset points',
            arrowprops=dict(facecolor='cyan', shrink=0.05),
            horizontalalignment='right', verticalalignment='bottom')


    def __calculateError(self, xObs, yObs, xt):
        '''
        Calculates the mean error of a list of points in the
        form (xObs, yObs) and the points (xObs, xt(xObs)), where xt
        is a function.
        '''
        # This is an abstract way of calculating the errors,
        # it can be used either with MRU or MRUV!
        error = [abs(y - xt(x)) for x, y in  zip(xObs, yObs)]
        return sum(error)/len(error)

    def getAcceleration(self):
        '''
        Returns the mean acceleartion of a Walker in all
        of the runs.
        Should only be used with MRUV movements!
        '''
        mAcc = [self.__velocityF(run)(1) for run in self.times]
        return sum(mAcc)/len(mAcc)

    def getVelocity(self):
        '''
        Returns the mean velocity of a Walker in all of the runs.
        Should only be used with MRU movements!
        '''
        mVel = [self.__spaceF(run)(1) for run in self.times]
        return sum(mVel)/len(mVel)

    def plotGraph(self):
        '''
        Plots the full simulation and data of an MRU movement in a matplotlib plot!
        '''
        if(self.movType == "MRUV"):
            self.__plotGraphMRUV()
            return

        for run in self.times:
            # Simulated: xt - space Function , (x, y) simulated graph
            xt = self.__spaceF(run)
            x = np.asarray([0, Walker.XLIM])
            y = list(map(xt, x))
            # Observed: (realX, realY) observed points in action!
            realX = self.__timeList(run)
            realY = self.__spaceList(run)[0]
            # Labels and Xticks
            timeNames = self.__spaceList(run)[1]
            xticks = [int(i) for i in np.arange(0, self.__finalTime(run), 5)]
            labels = [str(i) for i in xticks]
            # Create the plot, and do matplotlib stuff
            f, axarr = plt.subplots(nrows=2, ncols=1, figsize=(15, 10))
            ax1 = axarr[0]
            ax1.plot(x, y, label = "simulado")
            for i, j in zip(realX, realY):
                ax1.plot([i, i], [0, j], 'r--')
            ax1.scatter(realX, realY,color='red', marker="+", label = "observado")
            ax1.set_xlabel('tempo (s)', fontsize=13)
            ax1.set_ylabel('espaço (m)', fontsize=13)
            ax1.set_title(self.movType+" - "+run["csv"], fontsize=16, color="#000c3d")
            ax1.set_xticklabels(labels)
            ax1.set_xticks(xticks)
            ax1.spines['right'].set_visible(False)
            ax1.spines['top'].set_visible(False)
            ax1.set_xlim(0, self.__finalTime(run) + 1)
            ax1.set_ylim(0, Walker.SPACE + 1)
            err = self.__calculateError(realX, realY, xt)
            ax1.scatter(0, 0,  c = 'w', label=f"Velocidade Média: {round(xt(1), 3)} m/s")
            ax1.scatter(0, 0,  c = 'w', label=f"Erro: {round(err, 2)}")
            leg = axarr[0].legend(loc ="upper left")
            plt.draw()
            p = leg.get_window_extent()
            # Annotate our plot
            self.__setAnnotations( xObs = realX, yObs = realY, labels = timeNames,
                values = realX, ax = axarr[0])
            # Plot a CSV in the second axis
            self.__plotCsv(run, axarr[1])
            plt.autoscale(False)
            plt.tight_layout(pad=4, w_pad=0.5, h_pad=5.0)
            # Show the final plot! :)
            plt.show()

    def __plotGraphMRUV(self):
        '''
        Plots the full simulation and data of an MRUV movement in a matplotlib plot!
        '''
        for run in self.times:
            # Simulated: st - space Function , vt - velocity function
            # (xspa, y1) - simulated space graph, (xvel, y2) - simulated velocity graph
            st = self.__spaceF(run)
            vt = self.__velocityF(run)
            xspa = np.arange(0, Walker.XLIM, 0.01)
            xvel = np.asarray([0, Walker.XLIM])
            y1 = list(map(st, xspa))
            y2 = list(map(vt, xvel))
            # Observed: (realX, realY) observed points in action!
            realX = self.__timeList(run)
            realY = self.__spaceList(run)[0]
            # Labels and Xticks
            timeNames = self.__spaceList(run)[1]
            xticks = [int(i) for i in np.arange(0, self.__finalTime(run), 5)]
            labels = [str(i) for i in xticks]
            # Create the plot, and do matplotlib stuff
            f, axarr = plt.subplots(nrows=2, ncols=1, figsize=(15, 10))
            ax1 = axarr[0]
            ax2 = ax1.twinx() # secondary shared axis
            ax3 = axarr[1]
            ax1.plot(xspa, y1, label="espaço simulado")
            for i, j in zip(realX, realY):
                ax1.plot([i, i], [0, j], 'r--')
            ax1.scatter(realX, realY,color='red', marker="+", label="observado")
            ax1.set_xlabel('tempo (s)', fontsize=13)
            ax1.set_ylabel('espaço (m)', fontsize=13)
            ax1.set_title(self.movType+" - "+run["csv"], fontsize=16, color="#000c3d")
            ax1.set_xticks(xticks)
            ax1.set_xticklabels(labels)
            ax1.set_xlim(0, self.__finalTime(run) + 1)
            ax1.set_ylim(0, Walker.SPACE + 1)
            ax1.plot(0, 0,  'g-', label="velocidade simulada")
            ax1.tick_params('y', colors='b')
            ax2.plot(xvel, y2, 'g-')
            err = self.__calculateError(realX, realY, st)
            ax1.scatter(0, 0,  c = 'w', label=f"Aceleração Média: {round(vt(1), 3)} m/s²")
            ax1.scatter(0, 0,  c = 'w', label=f"Erro: {round(err, 2)}")
            ax1.spines['top'].set_visible(False)
            ax1.spines['top'].set_visible(False)
            ax1.legend(loc = "upper left")
            ax2.set_ylabel('velocidade (m/s)', fontsize=13)
            ax2.set_ylim(0, vt(self.__finalTime(run)) + 1)
            ax2.tick_params('y', colors='g')
            ft = self.__finalTime(run)
            # Annotate our plot
            self.__setAnnotations(xObs = realX, yObs = realY, labels = timeNames,
                values = realX, ax = ax1)
            self.__plotCsv(run, ax3)
            plt.setp(ax1.get_xticklabels(), visible=True)
            plt.setp(ax1.xaxis.get_label(), visible=True)
            plt.autoscale(False)
            ax1.spines['top'].set_visible(False)
            ax2.spines['top'].set_visible(False)
            plt.tight_layout(pad=4, w_pad=0.5, h_pad=5.0)
            # Show the final plot! :)
            plt.show()

def plotInfo(w = None, cTitle = "", cMsg = ""):
    '''
    Plot a info graphic. If w is a walker, then it plots the respective
    information:
        - Mean Velocity
        - Mean acceleration
    If it's not a walker, then it just plots the custom message with
    the custom title sent by the parameters
    '''
    plt.cla()
    plt.clf()
    plt.close()
    fig = plt.figure(figsize=(3.3,0.6))
    plt.axis('off')
    ax = fig.add_subplot(111)
    ax.axis([0, 10, 0, 10])
    plt.tight_layout(pad=0.42, w_pad=0.5, h_pad=0.5)
    if w != None:
        fig.suptitle(f"{w.movType} - {w.name}", fontsize=16, fontweight='bold')
        if w.movType == "MRU":
            ax.text(-0.6, -3.5, f'Velocidade média: {round(w.getVelocity(), 3)} m/s', fontsize=15)
        else:
            ax.text(-0.6, -3.5, f'Aceleração média: {round(w.getAcceleration(), 3)} m/s²', fontsize=15)
    else:
        fig.suptitle(cTitle, fontsize=16, fontweight='bold')
        ax.text(-0.6, -3.5, cMsg, fontsize=15)

    plt.show()

def addWalker(w, listWalkers):
    newWalker = Walker(w['walker'], w['movType'], w['times'])
    listWalkers.append(newWalker)

if __name__ == '__main__':
    main()

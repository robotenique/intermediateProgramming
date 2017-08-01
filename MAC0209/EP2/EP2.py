#python version = 3.6
import matplotlib.pyplot as plt
from matplotlib.animation import ArtistAnimation, FuncAnimation
from matplotlib.patches import RegularPolygon, Polygon
from matplotlib.collections import PatchCollection
from abc import ABC, abstractmethod
import numpy as np
import pandas as pd
import sys
import json
import bisect

g = 9.8

class Commons(ABC):

    legends = {
        "time" : "Tempo",
        "gfx"  : "Fgx",
        "gFy"  : "Fgy",
        "gFz"  : "Fgz",
        "Bx"   : "Fmx",
        "By"   : "Fmy",
        "Bz"   : "Fmz",
        "x"    : "Fmx",
        "y"    : "Fmy",
        "z"    : "Fmz",
        "MF"   : "FmR"
    }
    dirs = ["right", "left", "top", "bottom"]

    def __plotCsv(self, csv, axis, cols, res=""):
        """
        Plots columns cols from csv file into axis and add an resultant force
        column if necessary
        """
        self.legends["r"] = res
        # read csv file
        file = "./CSV/" + csv + ".csv"
        df = pd.read_csv(file, sep=';', decimal=',', usecols=cols)
        if res:
            # add resultant force
            v = list(df)
            df = df.assign(r=lambda row : np.sqrt(row[v[1]]**2 + row[v[2]]**2 + row[v[3]]**2))
        # customizing the graph
        df.rename(columns=self.legends, inplace=True)
        df.plot(x='Tempo', ax=axis)
        for d in self.dirs:
            axis.spines[d].set_visible(False)
        axis.axis('off')
        axis.legend(loc='upper left')
        axis.set_xlim(0, np.max(np.asarray(df['Tempo'])))

    def __calculateError(self, states, pos, times):
        """
        Calculates the deviation of y axis error at observed times
        """
        s = 0
        stimes = states[0]
        spos = states[1]
        n = len(pos)
        for t, p in zip(times, pos):
            i = bisect.bisect_left(stimes, t) - 1
            if stimes[i+1] == t:
                y = spos[i+1]
            else:
                m = (spos[i]-spos[i+1])/(stimes[i]-stimes[i+1])
                y = m*(t-stimes[i]) + spos[i]
            s += (p-y)**2
        return np.sqrt(s/n)

    @abstractmethod
    def plotGraph(self):
        pass

class Ramp(Commons):

    # class variables
    DT = 0.05
    THETA = 0.148353 # ramp inclination
    MI = 0.055 # friction coefficient

    def __init__(self, info):
        self.info = info

    def __statesEuler(self, theta, mi, dt):
        """
        Uses Euler algorithm and given ramp inclination (theta), kinetic
        friction coefficient (mi) and delta time (dt) to calculate space and
        speed of a sliding object
        """
        lstate = {"t" : 0, "s" : 0, "v" : 0}
        state = {"t" : 0, "s" : 0, "v" : 0}
        res = []
        for i in np.arange(0.0, 4, dt):
            state["s"] = lstate["s"] + lstate["v"]*dt
            state["v"] = lstate["v"] + g*dt*(np.sin(theta)-mi*np.cos(theta))
            state["t"] = lstate["t"] + dt
            res.append(list(lstate.values()))
            lstate = state.copy()
        return np.array(res).transpose()

    def __statesEulerCromer(self, theta, mi, dt):
        """
        Uses Euler-Cromer algorithm and given ramp inclination (theta), kinetic
        friction coefficient (mi) and delta time (dt) to calculate space and
        speed of a sliding object
        """
        lstate = {"t" : 0, "s" : 0, "v" : 0}
        state = {"t" : 0, "s" : 0, "v" : 0}
        res = []
        for i in np.arange(0.0, 4, dt):
            state["v"] = lstate["v"] + g*dt*(np.sin(theta)-mi*np.cos(theta))
            state["s"] = lstate["s"] + state["v"]*dt
            state["t"] = lstate["t"] + dt
            res.append(list(lstate.values()))
            lstate = state.copy()
        return np.array(res).transpose()

    def plotGraph(self):
        '''
        Plots the full simulation and data of a ramp sliding object in a
        matplotlib plot!
        '''
        statesE = self.__statesEuler(self.THETA, self.MI, self.DT)
        statesEC = self.__statesEulerCromer(self.THETA, self.MI, self.DT)
        for exp in self.info:
            csv = exp["csv"]
            times = exp["times"]
            obsTime = exp["obsTime"]
            f, axarr = plt.subplots(nrows=3, ncols=1, figsize=(15, 10))
            ax = axarr[0]
            # calculate space and speed
            realX = times
            realY = [2, 4]
            # plot informations
            ax.plot(statesE[0], statesE[1],
                    label="espaço simulado (m) (Euler)")
            ax.plot(statesE[0], statesE[2], "g-",
                    label="velocidade simulada (m/s) (Euler)")
            ax.plot(statesEC[0], statesEC[1], "m:",
                    label="espaço simulado (m) (Euler-Cromer)")
            ax.plot(statesEC[0], statesEC[2], "c:",
                    label="velocidade simulada (m/s) (Euler-Cromer)")
            for t,i in zip(times, range(1,3)):
                ax.plot([t, t], [0, i*2], "r--")
            ax.scatter(realX, realY, color='red', marker="+", label="observado")
            # customize the graph
            ax.spines['right'].set_visible(False)
            ax.spines['top'].set_visible(False)
            ax.set_xlim(0, obsTime)
            ax.set_ylim(0, 6)
            # add legends and error
            err = self._Commons__calculateError(statesE, realY, times)
            ax.scatter(0, 0,  c = 'w', label=f"Erro: {round(err, 2)}")
            ax.set_title(csv, fontsize=16, color="#000c3d")
            ax.set_xlabel('tempo (s)', fontsize=13)
            ax.legend(loc='upper left')
            # plot csvs
            self._Commons__plotCsv(csv, axarr[1], [0, 1, 2, 3], "FgR")
            self._Commons__plotCsv(csv, axarr[2], [0, 4, 5, 6], "FmR")
            plt.tight_layout(pad=4, w_pad=0.5, h_pad=5.0)
            plt.show()
        self.__showAnimation(statesE)

    def __showAnimation(self, states):
        """
        Shows the animation with simulated data given by states
        """
        f, ax = plt.subplots(figsize=(10.5, 5))
        # create objects to be plotted
        timeText = ax.text(0.05, 0.9, '', transform=ax.transAxes)
        velText = ax.text(0.05, 0.8, '', transform=ax.transAxes)
        rampPoints = [[0, 0], [7, 0], [7, 7*np.sin(self.THETA)]]
        box = RegularPolygon((6.4*np.cos(self.THETA),6.4*np.sin(self.THETA)), 4,
                             radius=0.4, orientation=np.pi/4+self.THETA)
        ramp = Polygon(rampPoints, closed=True, color="g", ec="k")
        ax.add_patch(box)
        ax.add_patch(ramp)
        # function that iterates through states and yield the time and box
        # position and speed
        def update():
            for time, space, vel in zip(states[0], states[1], states[2]):
                x = (6.4-space)*np.cos(self.THETA)
                y = (6.4-space)*np.sin(self.THETA)
                yield time, x, y, vel
                if space > 6:
                    break
        # function that plot each frame
        def plot(update):
            time, x, y, vel = update
            box.xy = (x, y)
            timeText.set_text(f"Time = {round(time, 2)}s")
            velText.set_text(f"Speed = {round(vel, 2)}m/s")
            return box, ramp, timeText, velText
        # create animation
        ani = FuncAnimation(f, plot, update, interval=1000*self.DT)
        # add legends
        ax.set_xlim(-1, 7)
        ax.set_ylim(0, 4)
        ax.set_xlabel('(m)', fontsize=13)
        ax.set_ylabel('(m)', fontsize=13)
        ax.set_title("Animação rampa", fontsize=16, color="#000c3d")
        plt.show()

class Pendulum(Commons):

    # class variables
    DT = 0.05
    THETA = np.pi/4 # inicial angle
    LENGTH = 1.5 # thread length
    GAMMA = 0.052 # damp coefficient

    def __init__(self, info):
        self.info = info

    def __statesEuler(self, itheta, length, dt):
        """
        Uses Euler algorithm and given initial angle (itheta), thread length
        (length) and delta time (dt) to calculate space and speed of a pendulum
        """
        lstate = {"t" : 0, "th" : itheta, "v" : 0}
        state = {"t" : 0, "th" : 0, "v" : 0}
        res = []
        for i in np.arange(0.0, 55, dt):
            state["th"] = lstate["th"] + lstate["v"]*dt
            state["v"] = lstate["v"] - ((g*np.sin(state["th"]))/length + self.GAMMA*lstate["v"])*dt
            state["t"] = lstate["t"] + dt
            res.append(list(lstate.values()))
            lstate = state.copy()
        return np.array(res).transpose()

    def __statesEulerCromer(self, itheta, length, dt):
        """
        Uses Euler-Cromer algorithm and given initial angle (itheta), thread
        length (length) and delta time (dt) to calculate space and speed of a
        pendulum
        """
        lstate = {"t" : 0, "th" : itheta, "v" : 0}
        state = {"t" : 0, "th" : 0, "v" : 0}
        res = []
        for i in np.arange(0.0, 55, dt):
            state["v"] = lstate["v"] - ((g*np.sin(lstate["th"]))/length +
                         self.GAMMA*lstate["v"])*dt
            state["th"] = lstate["th"] + state["v"]*dt
            state["t"] = lstate["t"] + dt
            res.append(list(lstate.values()))
            lstate = state.copy()
        return np.array(res).transpose()

    def plotGraph(self):
        '''
        Plots the full simulation and data of a pendular movement in a
        matplotlib plot!
        '''
        # calculate space and speed
        statesE = self.__statesEuler(self.THETA, self.LENGTH, self.DT)
        statesEC = self.__statesEulerCromer(self.THETA, self.LENGTH, self.DT)
        for exp in self.info:
            csv = exp["csv"]
            times = exp["times"]
            fTime = exp["fTime"]
            f, axarr = plt.subplots(nrows=2, ncols=1, figsize=(15, 10))
            ax = axarr[0]
            # plot space and speed
            ax.plot(statesE[0], statesE[1],
                    label="ângulo simulado (rad) (Euler)")
            ax.plot(statesE[0], statesE[2], "g-",
                    label="velocidade simulada (rad/s) (Euler)")
            ax.plot(statesEC[0], statesEC[1], "m:",
                    label="ângulo simulado (rad) (Euler-Cromer)")
            ax.plot(statesEC[0], statesEC[2], "c:",
                    label="velocidade simulada (rad/s) (Euler-Cromer)")
            ypos = [0]*len(times)
            ax.scatter(times, ypos, color="r", marker="x", label="observado")
            # customize the graph
            ax.spines['right'].set_visible(False)
            ax.spines['top'].set_visible(False)
            ax.set_xlim(0, fTime)
            ax.set_ylim(-2, 2)
            # add legends and error
            err = self._Commons__calculateError(statesE, ypos, times)
            ax.scatter(0, 0,  c = 'w', label=f"Erro: {round(err, 2)}")
            ax.set_title(csv, fontsize=16, color="#000c3d")
            ax.set_xlabel('tempo (s)', fontsize=13)
            ax.legend(loc='upper right')
            # plot csv
            self._Commons__plotCsv(csv, axarr[1], [0, 1, 2, 3], "FmR")
            plt.tight_layout(pad=4, w_pad=0.5, h_pad=5.0)
            plt.show()
        self.__showAnimation(statesE)

    def __showAnimation(self, states):
        """
        Shows the animation with simulated data given by states
        """
        f, ax = plt.subplots(figsize=(5, 5))
        # create objects to be plotted
        timeText = ax.text(0.05, 0.9, '', transform=ax.transAxes)
        velText = ax.text(0.05, 0.8, '', transform=ax.transAxes)
        bob, = ax.plot([], [], "bo", ms=10)
        thread, = ax.plot([], [], "b-")
        # function that iterates through states and yield the time and box
        # position and speed
        def update():
            for time, theta, vel in zip(states[0], states[1], states[2]):
                x = self.LENGTH*np.sin(theta)
                y = -self.LENGTH*np.cos(theta)
                yield time, x, y, vel
        # function that plot each frame
        def plot(update):
            time, x, y, vel = update
            thread.set_data([x, 0], [y, 0])
            bob.set_data([x], [y])
            timeText.set_text(f"Time = {round(time, 2)}s")
            velText.set_text(f"Speed = {round(vel, 2)}rad/s")
            return timeText, velText, bob, thread
        # create animation
        ani = FuncAnimation(f, plot, update, interval=1000*self.DT)
        # add legends
        ax.set_xlim(-1.5, 1.5)
        ax.set_ylim(-2, 1)
        ax.set_xlabel('(m)', fontsize=13)
        ax.set_ylabel('(m)', fontsize=13)
        ax.set_title("Animação pêndulo", fontsize=16, color="#000c3d")
        plt.show()

def main():
    if(len(sys.argv) == 2 and sys.argv[1][-5:] == ".json"):
        data = sys.argv[1]
    else:
        raise ValueError("You need to pass a single json file as argument!")
    # parse json file
    with open(data) as f:
        jsonData = json.loads(f.read())
    # create objects and plot their information
    p = Pendulum(jsonData["Pendulo"])
    p.plotGraph()
    r = Ramp(jsonData["Rampa"])
    r.plotGraph()




if __name__ == '__main__':
    main()

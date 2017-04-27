import matplotlib.pyplot as plt

class Movement(Object):
    def __init__(stateV, stateF, method=0, log=True):
        self.stateVector = stateV
        self.stateFunction = stateF
        self.log = log
        self.method = method
        if log:
            self.log_matrix = [[i] for i in self.stateVector]

    def apply_steps(self, n):
        if self.method == 0: self.__applyEuler(n)
        elif self.method == 1: self.__applyEuler_Cromer(n)
        elif self.method == 3: self.__applyEuler_Richardson(n)
        else: exit()


class SHO(Movement):
    '''
    State vector = t, v, x
    '''
    def __applyEuler(self, n):
        while()

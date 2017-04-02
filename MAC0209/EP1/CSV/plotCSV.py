from pathlib import Path
import pandas as pd
import matplotlib
import matplotlib.pyplot as plt
matplotlib.style.use('ggplot')
def main():
    arqs = [arq for arq in Path.cwd().iterdir() if arq.suffix == ".csv"]
    for csv in arqs:
        df = pd.read_csv(csv, names=['Tempo', 'Fx', 'Fy', 'Fz', 'Fr'], sep=';', decimal=',')
        df.plot(title=csv.name, x = 'Tempo')
        plt.legend(loc='upper right')
        plt.show()
main()

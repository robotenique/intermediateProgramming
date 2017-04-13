from pathlib import Path
import pandas as pd
import matplotlib.pyplot as plt
import sys
def main():
    plt.style.use('seaborn-deep')
    arg = sys.argv[1] if len(sys.argv) > 1 else "*.csv"
    arqs = list(Path('.').glob(arg))
    for csv in arqs:
        df = pd.read_csv(csv, names=['Tempo', 'Fx', 'Fy', 'Fz', 'Fr'], sep=';', decimal=',')
        df.plot(title=csv.name, x = 'Tempo')
        frame1 = plt.gca()
        frame1.axes.get_xaxis().set_visible(False)
        frame1.axes.get_yaxis().set_visible(False)
        frame1.spines['top'].set_visible(False)
        frame1.spines['right'].set_visible(False)
        frame1.spines['left'].set_visible(False)
        frame1.spines['bottom'].set_visible(False)
        plt.legend(loc='upper right')
        plt.show()
main()

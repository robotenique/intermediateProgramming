/**
 * Created by juliano on 4/5/17.
 */
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private int n;
    private int trials;
    private double[] means;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if(n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException("n and trials must be BIGGER than zero!");
       this.n = n;
       this.trials = trials;
       means = new double[trials];
        executeMonteCarlo();
    }

    private void executeMonteCarlo() {
        int pos;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while(!p.percolates()) {
                pos = StdRandom.uniform(n*n);
                p.open(pos/n, pos%n);
            }
            means[i] = p.numberOfOpenSites()/(double)(n*n);
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        double sum = 0;
        for (int i = 0; i < trials; sum += means[i++]);
        return sum/trials;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        double var = 0;
        double m = mean();
        for (int i = 0; i < trials; i++)
            var += Math.pow(means[i] - m, 2);
        var /= trials - 1;
        return Math.sqrt(var);
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        // Assuming we can approximate to gaussian distribution (t >= 30)
        double s = stddev();
        double m = mean();
        return m - (1.96 * s)/Math.sqrt(trials);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        // Assuming we can approximate to gaussian distribution (t >= 30)
        double s = stddev();
        double m = mean();
        return m + (1.96 * s)/Math.sqrt(trials);
    }

    public static void main(String[] args) {
        // Unit tests
        int n = 400;
        int trials = 400;
        PercolationStats sts = new PercolationStats(n, trials);
        StdOut.println("PercolationStats("+n+", "+trials+")");
        StdOut.println("mean()                  ="+sts.mean());
        StdOut.println("stddev()                ="+sts.stddev());
        StdOut.println("confidenceLow()         ="+sts.confidenceLow());
        StdOut.println("confidenceHigh()        ="+sts.confidenceHigh());
    }
}

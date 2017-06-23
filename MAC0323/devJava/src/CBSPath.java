import edu.princeton.cs.algs4.*;

import java.util.HashSet;

class EntryData{
    public String from;
    public String to;
    public int time;

    public EntryData(String from, String to, int time) {
        this.from = from;
        this.to = to;
        this.time = time;
    }
    public static void testEntry(int n, int m, int k, EntryData[] flights, String[] startCBS, String mpCity) {
        StdOut.printf("%d %d %d \n", n, m, k);
        for (EntryData flight : flights)
            StdOut.println(flight.from + " " + flight.to + " " + flight.time);
        for (String city : startCBS)
            StdOut.println(city);
        StdOut.println(mpCity);
    }

}

public class CBSPath {
    int n, m, k;
    EntryData[] flights;
    String[] startCBS;
    String mpCity;
    HashSet<String> cities;
    LinearProbingHashST<String, Integer> cityMap;

    public CBSPath(int n, int m, int k, EntryData[] flights, String[] startCBS, String mpCity, HashSet<String> cities) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.flights = flights;
        this.startCBS = startCBS;
        this.mpCity = mpCity;
        this.cities = cities;
        prepareST();
    }

    private void prepareST() {
        cityMap = new LinearProbingHashST<>();
        int i = 0;
        for(String node : cities) {
            if(!cityMap.contains(node))
                cityMap.put(node, i++);
        }
    }

    public void cbsAI() {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(n);
        BST<String, Integer> safeSpot = new BST<>();
        for(EntryData f : flights)
            graph.addEdge(new DirectedEdge(cityMap.get(f.from), cityMap.get(f.to), f.time));
        DijkstraSP[] execDijkstras = new DijkstraSP[k];
        DijkstraSP mpDijkstra = new DijkstraSP(graph, cityMap.get(mpCity));
        for (int i = 0; i < k; i++)
            execDijkstras[i] = new DijkstraSP(graph, cityMap.get(startCBS[i]));
        for(String city : cityMap.keys())
            if(maxDist(execDijkstras, cityMap.get(city)) < mpDijkstra.distTo(cityMap.get(city)))
                safeSpot.put(city, 0);
        if(safeSpot.size() > 0)
            for(String c: safeSpot.keys())
                StdOut.println(c);
        else
            StdOut.println("VENHA COMIGO PARA CURITIBA");
    }

    private double maxDist(DijkstraSP[] paths, int city) {
        double maxDist = 0;
        for(DijkstraSP dsp : paths)
            if(maxDist < dsp.distTo(city))
                maxDist = dsp.distTo(city);
        return maxDist;
    }

    public static void main(String[] args) {
        int n, m, k;
        EntryData[] flights;
        String[] startCBS;
        String mpCity;
        HashSet<String> cities;
        n = StdIn.readInt();
        m = StdIn.readInt();
        k = StdIn.readInt();
        cities = new HashSet<>();
        flights =  new EntryData[m];
        startCBS = new String[k];
        for(int i = 0; i < m; i++){
            String from = StdIn.readString();
            String to = StdIn.readString();
            int time = StdIn.readInt();
            cities.add(from);
            cities.add(to);
            flights[i] = new EntryData(from, to, time);
        }
        for (int i = 0; i < k; i++)
            startCBS[i] = StdIn.readString();
        mpCity = StdIn.readString();
        CBSPath saveCBS = new CBSPath(n, m, k, flights, startCBS, mpCity, cities);
        saveCBS.cbsAI();
        //StdOut.println("N = "+n+" Set size = "+cities.size());
        //EntryData.testEntry(n, m, k, flights, startCBS, mpCity);

    }
}

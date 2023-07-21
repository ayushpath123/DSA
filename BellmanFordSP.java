package DSProject;

import edu.princeton.cs.algs4.*;
import java.util.Arrays;
import java.util.Scanner;

public class BellmanFordSP {
    private double[] distTo;
    private DirectedEdge[] edgeTo;

    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        int V = G.V();
        distTo = new double[V];
        edgeTo = new DirectedEdge[V];

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[s] = 0.0;

        for (int pass = 0; pass < V - 1; pass++) {
            for (DirectedEdge e : G.edges()) {
                relax(e);
            }
        }

        // Check for negative cycles
        for (DirectedEdge e : G.edges()) {
            int v = e.from(), w = e.to();
            double weight = e.weight();
            if (distTo[w] > distTo[v] + weight) {
                throw new IllegalArgumentException("Negative cycle detected.");
            }
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        double weight = e.weight();
        if (distTo[w] > distTo[v] + weight) {
            distTo[w] = distTo[v] + weight;
            edgeTo[w] = e;
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V-1));
    }

    public static void main(String[] args) {
        int i;
        float[] a = new float[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 0 For Q5 Midsem and 1 For Datasets ");
        int l = sc.nextInt();
        String[] filePaths;
        int[] sourceVertices;

        if (l == 0) {
            filePaths = new String[]{
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\G1.txt",
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\G2.txt",
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\G3.txt"
            };
            sourceVertices = new int[]{0, 0, 0};
        } else if (l == 1) {
            filePaths = new String[]{
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\1.txt",
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\2.txt",
                    "C:\\Users\\ayush\\OneDrive\\Documents\\work\\DSProject\\3.txt"
            };
            sourceVertices = new int[]{0, 0, 0};
        } else {
            System.out.println("Invalid input for 'l'. Use 0 or 1.");
            return;
        }

        for (i = 0; i < 3; i++) {
            String input = filePaths[i];
            int source = sourceVertices[i];

            In in = new In(input);
            EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
            BellmanFordSP sp = new BellmanFordSP(G, source);

            double sum = 0.0;
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            int paths = G.V() - 1;
            double[] distances = new double[G.V()];

            for (int v = 0; v < G.V(); v++) {
                if (sp.hasPathTo(v)) {
                    double dist = sp.distTo(v);
                    distances[v] = dist;
                    sum += dist;
                    if (dist < min)
                        min = dist;
                    if (dist > max)
                        max = dist;
                }
            }

            double mean = sum / paths;
            a[i] = (float) mean;

            String rowData = String.format("Graph %d, Mean: %.2f, Median: %.2f, Min: %.2f, Max: %.2f",
                    (i + 1), mean, distances[paths / 2], min, max);
            System.out.println(rowData);
        }

        Arrays.sort(a);
        System.out.print("Mean = ");
        System.out.println((a[0] + a[1] + a[2]) / 3);
        System.out.print("Median = ");
        System.out.println(a[1]);
        System.out.print("Minimum = ");
        System.out.println(a[0]);
        System.out.print("Maximum = ");
        System.out.println(a[2]);
    }
}

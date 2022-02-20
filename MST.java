import java.util.*;

public class MST {

    private Random rand;
	private int numpoints;
	private int numtrials;
	private int dimension;
    private double[] dist;
    private boolean[] inTree;
	private double[][] locations;

	// CHANGE INF TO BE THE MAX POSSILE NORM, so probably sqrt(4)
	private static final double INF = Double.MAX_VALUE;

	public MST(int numpoints, int numtrials, int dimension) {
		this.numpoints = numpoints;
		this.numtrials = numtrials;
		this.dimension = dimension;
		this.dist = new double[numpoints];
        this.inTree = new boolean[numpoints];
		this.rand = new Random();
	}


	private void reinitRecords() {

        // Clear out MST records for new trial
        // TODO: move this into a new reset() function
        for (int i = 0; i < this.numpoints; i++) {
            this.dist[i] = INF;
            this.inTree[i] = false;
        }

		if (this.dimension != 0) {
			this.locations = new double[this.numpoints][this.dimension];

			for (int row = 0; row < numpoints; row++) {
				for (int col = 0; col < this.dimension; col++) {
					this.locations[row][col] = rand.nextDouble();
				}
			}
		}
	}

    private double calculateWeight(int v, int w) {
        // TODO: change this code

        // For 0-Dimension, weight is uniform r.v. from [0,1]
        if (this.dimension == 0) {
            return this.rand.nextDouble();
        }

        double res = 0;
        // Weight is Euclidean norm between nodes
        for (int d = 0; d < this.dimension; d++) {
            res += Math.pow(locations[v][d] - locations[w][d], 2);
        }
        return Math.sqrt(res);
    }

    // TODO: pass in numpoints, dimension, and numtrials instead of having them be object attributes
	public double runPrims() {

	    this.reinitRecords();

        // Min weight to 0th node is 0. This induces the 0th node as the starting point.
        this.dist[0] = 0;
        double totalWeight = 0;

	    // Keep building MST until we have a full MST
        for (int mstSize = 0; mstSize < this.numpoints; mstSize++) {
            // Find node closest to tree, i.e. "extract min from heap"
            int nextNode = 0;
            double minWeight = INF;
            // TODO: implement heap instead of linear search for min edge weight
            for (int v = 0; v < this.numpoints; v++) {
                if (this.dist[v] < minWeight && !this.inTree[v]) {
                    minWeight = this.dist[v];
                    nextNode = v;
                }
            }

            // Add closest node to MST
            this.inTree[nextNode] = true;
            // Each time we add a node, we increase the size of MST
            totalWeight += minWeight;

            // Loop through rest of graph and update weights since Tree has changed
            for (int w = 0; w < this.numpoints; w++) {
                if (!this.inTree[w]) {
                    this.dist[w] = Math.min(this.dist[w], this.calculateWeight(nextNode, w));
                }
            }
        }
        return totalWeight;
    }

    // TODO: pass in numpoints, dimension, and numtrials instead of having them be object attributes?
    public double simulateTrials() {

	    double totalWeight = 0;
        for (int trial = 0; trial < this.numtrials; trial++) {
            totalWeight += runPrims();
        }

        double avgWeight = totalWeight / this.numtrials;
        return avgWeight;
    }


	// TODO: rework main such that we have all the simulations done in one go
	public static void main(String[] args) {
        int points = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        int dimension = Integer.parseInt(args[2]);

        MST mst = new MST(points, trials, dimension);
		double averageSize = mst.simulateTrials();
		// TODO: switch out double for float
		System.out.printf("Average MST weight for dim: (%d) and points (%d) is (%f)\n", dimension, points, averageSize);
	}
}
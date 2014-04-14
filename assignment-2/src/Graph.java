import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Graph {
	public static ArrayList<HashSet<Link>> adj = new ArrayList<HashSet<Link>>();
	public static ArrayList<Integer> pq = new ArrayList<Integer>();

	public static void main(String[] args) {
		boolean runDiameter = false;
		boolean runDijkstra = false;
		boolean directed = true;
		boolean runPairs = false;
		int start = 0;
		String fileName = null;
		for (int i = 0; i < (args.length - 1); i++) {
			if (args[i].equals("-d") == true) {
				runDiameter = true;
			} else if (args[i].equals("-s") == true) {
				runDijkstra = true;
				start = Integer.parseInt(args[i + 1]);
				if (i == 1)
					directed = false;
			} else if (args[i].equals("-a") == true) {
				runPairs = true;
				if (i == 1)
					directed = false;
			}
		}
		for (String s : args) {
			if (s.indexOf(".") != -1) {
				fileName = s;
			}
		}
		readFile(fileName, directed);
		if (runDijkstra = true) {
			dijkstra(start);
		} else if (runPairs = true) {
			pairs();
		} else {
			diameter();
		}
	}

	public static void dijkstra(int s) {
		int[] pq = new int[adj.size()];
		int[] pred = new int[adj.size()];
		int[] dist = new int[adj.size()];
		for (HashSet<Link> u : adj) {
			Iterator<Link> itr = u.iterator();
			while (itr.hasNext()) {
				Link element = itr.next();
				pred[element.node] = 0;
				if (element.node != s) {
					dist[element.node] = Integer.MAX_VALUE;
				} else {
					dist[element.node] = 0;
				}
				pq[element.node] = dist[element.node];
			}
		}
		for (int i = 0; i < pq.length; i++) {
			int min = pq[0];
			int nd = 0;
			for (int j = 1; j < pq.length; j++) {
				if (pq[j] < min) {
					min = pq[j];
					nd = j;
				}
			}
			Link u = new Link(nd, min);
			for (int j = 0; j < adj.size(); j++) {
				HashSet<Link> v = adj.get(j);
				Iterator<Link> itr = v.iterator();
				while (itr.hasNext()) {
					Link element = itr.next();
					if (dist[element.node] > dist[u.node] + element.weight) {
						dist[element.node] = dist[u.node] + element.weight;
						pred[element.node] = u.node;
						pq[element.node] = dist[element.node];
					}

				}
			}
		}
		System.out.println("Predecessor matrix");
		for (int i = 0; i < pred.length; i++) {
			System.out.print(pred[i] + ",");
		}
		System.out.println();
		System.out.println("Distance matrix");
		for (int i = 0; i < dist.length; i++) {
			System.out.print(dist[i] + ",");
		}
	}

	public static void pairs() {
		int[][] pred = new int[adj.size()][adj.size()];
		int[][] dist = new int[adj.size()][adj.size()];
		for (HashSet<Link> u : adj) {
			Iterator<Link> itr = u.iterator();
			while (itr.hasNext()) {
				Link element = itr.next();
				for (HashSet<Link> v : adj) {
					Iterator<Link> itrtr = v.iterator();
					while (itrtr.hasNext()) {
						Link elem = itrtr.next();
						pred[element.node][elem.node] = 0;
						dist[element.node][elem.node] = 0;
					}
				}
			}
		}
		for (HashSet<Link> u : adj) {
			Iterator<Link> itr = u.iterator();
			while (itr.hasNext()) {

			}
		}
	}

	public static void diameter() {

	}

	public static void readFile(String f_name, boolean dir) {
		try {
			FileInputStream fstream = new FileInputStream(f_name);
			DataInputStream inp = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(inp));
			String strLine = "";
			String[] array;
			while ((strLine = br.readLine()) != null) {
				array = strLine.split(" ");
				int node_a = Integer.parseInt(array[0]);
				int node_b = Integer.parseInt(array[1]);
				int weight = Integer.parseInt(array[2]);
				if (node_a >= adj.size()) {
					for (int i = adj.size(); i <= node_a; i++) {
						adj.add(new HashSet<Link>());
					}
				}
				HashSet<Link> h = adj.get(node_a);
				Link a = new Link(node_b, weight);
				h.add(a);
				if (dir == false) {
					if (node_b >= adj.size()) {
						for (int i = adj.size(); i <= node_b; i++) {
							adj.add(new HashSet<Link>());
						}
					}
					HashSet<Link> d = adj.get(node_b);
					Link b = new Link(node_a, weight);
					d.add(b);
				}
			}
			inp.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}

class Link {
	int node;
	int weight;

	public Link(int node_b, int weight1) {
		node = node_b;
		weight = weight1;
	}
}

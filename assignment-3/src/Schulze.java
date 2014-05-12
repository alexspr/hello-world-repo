import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

public class Schulze {
	static ArrayList<HashSet<Integer>> wins = new ArrayList<HashSet<Integer>>();

	public static void main(String[] args) {
		int noc = 0;// Number of candidates
		int nob = 0;// Number of ballots
		JSONArray candidates = new JSONArray();
		JSONArray ballots = new JSONArray();
		try {
			FileInputStream fin = new FileInputStream(new File(args[0]));
			JSONTokener tokener = new JSONTokener(fin);
			JSONObject elections = new JSONObject(tokener);
			candidates = elections.getJSONArray("candidates");
			noc = candidates.length();
			ballots = elections.getJSONArray("ballots");
			elections.getJSONArray("ballots");
			nob = ballots.length();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.getMessage();
		}
		for (int i = 0; i < noc; i++) {
			wins.add(new HashSet<Integer>());
		}
		int[][] p = new int[noc][noc];
		for (int i = 0; i < noc; i++) {
			for (int j = 0; j < noc; j++) {
				p[i][j] = 0;
			}
		}
		for (int i = 0; i < nob; i++) {
			JSONArray ballot = ballots.getJSONArray(i);
			int[] b = new int[ballot.length()];
			for (int k = 0; k < b.length; k++) {
				b[k] = ballot.getInt(k);
			}
			for (int j = 0; j < b.length; j++) {
				int f = b[j];
				for (int k = j + 1; k < b.length; k++) {
					int s = b[k];
					p[f][s] = p[f][s] + 1;
				}

			}
		}
		int[][] w = new int[noc][noc];
		for (int i = 0; i < noc; i++) {
			for (int j = 0; j < noc; j++) {
				w[i][j] = p[i][j];
			}
		}
		for (int i = 0; i < noc; i++) {
			for (int j = noc - 1; j >= i; j--) {
				if (p[i][j] > p[j][i]) {
					p[i][j] = p[i][j] - p[j][i];
					p[j][i] = Integer.MAX_VALUE;
				} else if (p[i][j] < p[j][i]) {
					p[j][i] = p[j][i] - p[i][j];
					p[i][j] = Integer.MAX_VALUE;
				} else {
					p[j][i] = Integer.MAX_VALUE;
					p[i][j] = Integer.MAX_VALUE;
				}
			}
		}
		int[][] s = new int[noc][noc];
		int[][] pred = new int[noc][noc];
		for (int i = 0; i < noc; i++) {
			for (int j = 0; j < noc; j++) {
				if (w[i][j] > w[j][i]) {
					s[i][j] = w[i][j] - w[j][i];
					pred[i][j] = i;
				} else {
					s[i][j] = Integer.MIN_VALUE;
					pred[i][j] = -1;
				}

			}
		}
		for (int k = 0; k < noc; k++) {
			for (int i = 0; i < noc; i++) {
				if (i != k) {
					for (int j = 0; j < noc; j++) {
						if (j != i) {
							int min = Math.min(s[i][k], s[k][j]);
							if (s[i][j] < min) {
								s[i][j] = min;
								pred[i][j] = pred[k][j];
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < noc; i++) {
			for (int j = 0; j < noc; j++) {
				if (i != j) {
					if (s[i][j] > s[j][i]) {
						HashSet<Integer> h = wins.get(i);
						h.add(j);
					}
				}
			}
		}
		for (int i = 0; i < noc; i++) {
			HashSet<Integer> h = wins.get(i);
			System.out.print(candidates.getString(i) + " =" + " " + h.size() + " [");
			int f = 0;
			for (Integer u : h) {
				if (f == 0) {
					System.out.print(candidates.getString(u));
				} else {
					System.out.print("," + candidates.getString(u));
				}
				f++;
			}
			System.out.println("]");
		}
	}
}

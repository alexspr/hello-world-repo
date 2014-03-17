import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;
import java.util.ArrayList;

public class StockSpan {

	public static ArrayList<String> dates = new ArrayList<String>();
	public static ArrayList<Double> values = new ArrayList<>();

	public static void main(String[] args) {
		StockSpan obj = new StockSpan();
		obj.readFile();
		String arg = args[0];
		if (args[0].equals("-n")) {
			naive(arg);
		} else if (args[0].equals("-s")) {
			stack(arg);
		} else {
			long start = System.currentTimeMillis();
			for (int i = 1; i <= 100; i++)
				naive(arg);
			long elapsedTimeMillis = System.currentTimeMillis() - start;
			System.out.println("Naive implementation took: "
					+ elapsedTimeMillis + "millis");
			start = System.currentTimeMillis();
			for (int i = 1; i <= 100; i++)
				stack(arg);
			elapsedTimeMillis = System.currentTimeMillis() - start;
			System.out.println("Stack implementation took: "
					+ elapsedTimeMillis + "millis");
		}
	}

	public static void naive(String arg) {
		int[] span = new int[dates.size()];
		Double[] vals = values.toArray(new Double[values.size()]);
		String[] dts = dates.toArray(new String[dates.size()]);
		for (int i = 0; i <= dates.size() - 1; i++) {
			int c = 1;
			boolean end = false;
			while (((i - c) + 1 >= 0) && (end == false)) {
				if (vals[(i - c) + 1] <= vals[i])
					c++;
				else
					end = true;
			}
			span[i] = c - 1;
		}
		if (!arg.equals("-b")) {
			for (int i = 0; i <= span.length - 1; i++) {
				System.out.printf(dts[i]);
				System.out.println("," + span[i]);
			}
		}
	}

	public static void stack(String arg) {
		Double[] vals = values.toArray(new Double[values.size()]);
		String[] dts = dates.toArray(new String[dates.size()]);
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(0);
		int[] span = new int[dates.size()];
		for (int i = 1; i < dates.size(); i++) {
			while ((!stk.empty()) && (vals[stk.peek()] <= vals[i])) {
				stk.pop();
			}
			if (stk.empty())
				span[i] = i++;
			else
				span[i] = i - stk.peek();
			stk.push(i);

		}
		if (!arg.equals("-b")) {
			for (int i = 0; i < span.length; i++) {
				System.out.printf(dts[i]);
				System.out.println("," + span[i]);
			}
		}
	}

	public void readFile() {

		String csvFile = "DJIA.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int flag = 1;// Η flag ειναι μεταβλητη που χρησημοποιω για να
						// μην διαβαζει ο reader την πρωτη σειρα που ειναι
						// το DATE και το VALUE

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] dj = line.split(cvsSplitBy);
				if (flag != 1) {// Την πρωτη φορα που θα εκτελεστει η επαναληψη
								// το προγραμμα δεν θα μπει στην if και ετσι δεν
								// θα διαβασει την πρωτη σειρα
					String date = dj[0];
					double value = Double.parseDouble(dj[1]);
					values.add(value);
					dates.add(date);
				}
				flag = 0;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

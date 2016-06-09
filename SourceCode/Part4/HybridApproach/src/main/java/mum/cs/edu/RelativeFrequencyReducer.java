package mum.cs.edu;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class RelativeFrequencyReducer extends Reducer<Pair, IntWritable, Text, MyMapWritable> {

	int total;
	MyMapWritable myMap;
	String pairKey;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		myMap = new MyMapWritable();
		total = 0;
		pairKey = null;
	}

	@Override
	public void reduce(Pair key, Iterable<IntWritable> counts, Context context)
			throws IOException, InterruptedException {
		int s = 0;
		for (IntWritable count : counts) {
			s += count.get();
		}
		if (pairKey != null && !pairKey.equals(key.getKey().toString())) {
			// Emit the data
			for (Writable k : myMap.keySet()) {
				DoubleWritable result = new DoubleWritable(((IntWritable) myMap.get(k)).get() / (double) total);
				myMap.put(k, result);
			}
			context.write(new Text(pairKey), myMap);
			// Reset all params
			myMap = new MyMapWritable();
			total = 0;
		}

		pairKey = key.getKey().toString();
		total += s;
		myMap.put(new Text(key.getValue()), new IntWritable(s));

	}

	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		for (Writable k : myMap.keySet()) {
			DoubleWritable result = new DoubleWritable(((IntWritable) myMap.get(k)).get() / (double) total);
			myMap.put(k, result);
		}
		context.write(new Text(pairKey), myMap);
	}
}

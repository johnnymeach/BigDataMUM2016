package mum.cs.edu;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class RelativeFrequencyReducer extends Reducer<Pair, IntWritable, Pair, DoubleWritable> {
	private int total = 0;

	@Override
	public void reduce(Pair key, Iterable<IntWritable> counts, Context context)
			throws IOException, InterruptedException {
		int s = 0;
		for (IntWritable count : counts) {
			s += count.get();
		}
		if (key.getValue().toString().trim().equals("*")) {
			total = s;
		} else {
			context.write(key, new DoubleWritable(s / (double) total));
		}
	}
}

package mum.cs.edu;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class RelativeFrequencyReducer extends Reducer<Text, MyMapWritable, Text, MyMapWritable> {

	@Override
	public void reduce(Text key, Iterable<MyMapWritable> mapList, Context context)
			throws IOException, InterruptedException {
		int total = 0;
		MyMapWritable myMap = new MyMapWritable();
		for (MyMapWritable map : mapList) {
			for (Writable val : map.keySet()) {
				if (myMap.get(val) != null) {
					int oldVal = ((IntWritable) myMap.get(val)).get();
					int value = ((IntWritable) map.get(val)).get() + oldVal;
					myMap.put(val, new IntWritable(value));
				} else {
					int newVal = ((IntWritable) map.get(val)).get();
					myMap.put(val, new IntWritable(newVal));
				}
				total += ((IntWritable) map.get(val)).get();
			}
		}

		for (Writable data : myMap.keySet()) {
			DoubleWritable output = new DoubleWritable(((IntWritable) myMap.get(data)).get() / (double) total);
			myMap.put(data, output);
		}
		context.write(key, myMap);
	}
}

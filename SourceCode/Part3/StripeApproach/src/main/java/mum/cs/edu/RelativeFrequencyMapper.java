package mum.cs.edu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RelativeFrequencyMapper extends Mapper<LongWritable, Text, Text, MyMapWritable> {
	private static final Pattern WORD_BOUNDARY = Pattern.compile("\\s*\\b\\s*");
	private HashMap<String, MyMapWritable> H;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		H = new HashMap<String, MyMapWritable>();
	}

	@Override
	public void map(LongWritable offset, Text lineText, Context context) throws IOException, InterruptedException {
		String line = lineText.toString();
		System.out.println(line);
		String[] fields = WORD_BOUNDARY.split(line);
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isEmpty() || fields[i] == null)
				continue;
			String w = fields[i];
			MyMapWritable myMap;

			if (H.get(w) == null) {
				myMap = new MyMapWritable();
			} else {
				myMap = H.get(w);
			}

			for (int j = i + 1; j < fields.length; j++) {
				String u = fields[j];
				if (u.isEmpty())
					continue;
				if (w.equals(u))
					break;
				else {
					int counter;
					Text key = new Text(u);
					if (myMap.get(key) == null)
						counter = 1;
					else {
						counter = ((IntWritable) myMap.get(key)).get() + 1;
					}
					myMap.put(key, new IntWritable(counter));
				}

			}
			H.put(w, myMap);
		}
	}

	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		for (Map.Entry<String, MyMapWritable> entry : H.entrySet()) {
			String key = entry.getKey();
			MyMapWritable value = entry.getValue();
			context.write(new Text(key), value);
			System.out.println(key.toString() + " " + value);
		}
	}

}

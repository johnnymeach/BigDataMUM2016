package mum.cs.edu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RelativeFrequencyMapper extends Mapper<LongWritable, Text, Pair, IntWritable> {
	private HashMap<Pair, Integer> H;
	private static final Pattern WORD_BOUNDARY = Pattern.compile("\\s*\\b\\s*");

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		H = new HashMap<Pair, Integer>();
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
			for (int j = i + 1; j < fields.length; j++) {
				String u = fields[j];
				if (u.isEmpty())
					continue;
				if (w.equals(u))
					break;
				
				Pair pair = new Pair(w, u);
				if (H.get(pair) != null) {
					int value = (int) H.get(pair);
					H.put(pair, value + 1);
				} else {
					H.put(pair, new Integer(1));
				}
			}
		}
	}

	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {
		for (Map.Entry<Pair, Integer> entry : H.entrySet()) {
			Pair key = entry.getKey();
			Integer value = entry.getValue();
			context.write(key, new IntWritable(value));
			System.out.println(key.toString() + "   " + value);
		}
	}

}

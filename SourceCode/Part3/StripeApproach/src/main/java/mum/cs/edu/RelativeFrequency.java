package mum.cs.edu;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class RelativeFrequency extends Configured implements Tool {

	private static final Logger LOG = Logger.getLogger(RelativeFrequency.class);

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new RelativeFrequency(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf(), "relativestripe");
		job.setJarByClass(this.getClass());
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(RelativeFrequencyMapper.class);
		job.setReducerClass(RelativeFrequencyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MyMapWritable.class);
		return job.waitForCompletion(true) ? 0 : 1;
	}

}

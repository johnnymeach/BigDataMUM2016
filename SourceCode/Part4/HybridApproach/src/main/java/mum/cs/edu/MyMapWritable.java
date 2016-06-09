package mum.cs.edu;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

public class MyMapWritable extends MapWritable {
	@Override
	public String toString() {
		return entrySet() +""; 
	}
}
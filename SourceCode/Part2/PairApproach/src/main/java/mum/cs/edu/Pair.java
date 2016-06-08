package mum.cs.edu;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class Pair implements WritableComparable<Pair> {
	private Text key;
	private Text value;

	public Pair() {
		key = new Text();
		value = new Text();
	}

	public Pair(String key, String value) {
		this.key = new Text(key);
		this.value = new Text(value);
	}

	public void readFields(DataInput input) throws IOException {
		key.readFields(input);
		value.readFields(input);
	}

	public void write(DataOutput output) throws IOException {
		key.write(output);
		value.write(output);
	}

	public int compareTo(Pair pair) {
		int valueCompare = key.compareTo(pair.key);
		if (valueCompare != 0)
			return valueCompare;
		else if (value.toString() == "*")
			return -1;
		else if (pair.value.toString() == "*")
			return 1;
		else
			return value.compareTo(pair.value);
	}

	public Text getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "(" + key + ", " + value + ")";
	}

	public Text getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}

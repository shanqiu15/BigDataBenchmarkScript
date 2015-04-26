/*
 * $SPARK_HOME/bin/spark-submit --class DateFilter ~/Desktop/JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar  hdfs://localhost:8020/tmp/benchmark/text/tiny/uservisits yyyy-MM-dd 1980-01-01 1980-04-01
 * */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class DateFilter {
	public static JavaRDD<String[]> filter(JavaRDD<String> uservisit,
			String[] args) throws Exception {
		JavaRDD<String[]> uservisitTable = uservisit
				.map(new Function<String, String[]>() {
					public String[] call(String s) {
						return s.split(",");
					}
				});

		final SimpleDateFormat format = new SimpleDateFormat(args[0]);
		final Date start_date = format.parse(args[1]);
		final Date end_date = format.parse(args[2]);

		JavaRDD<String[]> visitRDD = uservisitTable
				.filter(new Function<String[], Boolean>() {
					public Boolean call(String[] x) throws ParseException {
						Date visitDate = format.parse(x[2]);
						return (visitDate.compareTo(start_date) >= 0)
								&& (visitDate.compareTo(end_date) <= 0);
					}
				});
		return visitRDD;
	}

	public static void main(String[] args) throws Exception {
		SparkConf conf = new SparkConf().setAppName("Java_Spark");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> uservisit = sc.textFile(args[0]);
		String[] par = Arrays.copyOfRange(args, 1, 4);
		JavaRDD<String[]> visitRDD = DateFilter.filter(uservisit, par);

		System.out.println("Here are the first 10 results:");
		for (String[] line : visitRDD.take(10)) {
			StringBuffer record = new StringBuffer();
			for (String i : line) {
				record.append(i);
				record.append(" ");
			}
			System.out.println(record.toString());
		}
	}
}

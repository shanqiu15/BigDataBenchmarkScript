/**
 * $SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/tiny/rankings amplab/text/tiny/uservisits 100 0 9  1980-01-01 1990-01-01
 * 
 * */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class BenchmarkJavaSpark {
	public static void main(final String[] args) throws Exception {
		SparkConf conf = new SparkConf().setAppName("Java_Spark");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> ranking = sc.textFile(args[0]);
		JavaRDD<String> uservisit = sc.textFile(args[1]);

		@SuppressWarnings("serial")
		JavaRDD<String[]> rankingTable = ranking
				.map(new Function<String, String[]>() {
					public String[] call(String s) {
						return s.split(",");
					}
				});

		JavaRDD<String[]> uservisitTable = uservisit
				.map(new Function<String, String[]>() {
					public String[] call(String s) {
						return s.split(",");
					}
				});

		// Java Spark Scan Query
		long ScanQueryStartTime = System.nanoTime();
		JavaRDD<String[]> resultRDD = rankingTable
				.filter(new Function<String[], Boolean>() {
					public Boolean call(String[] s) {
						return Integer.parseInt(s[1]) > Integer
								.parseInt(args[2]);
					}
				});
		// resultRDD.count();
		System.out.println("10 first results: ");
		for (String[] line : resultRDD.take(10)) {
			System.out.println(line[0] + " " + line[1] + " " + line[2]);
		}
		double ScanQueryRunningTime = (double) (System.nanoTime() - ScanQueryStartTime)
				/ Math.pow(10, 9);

		// Java Spark Aggregation Query
		long AggregationQueryStartTime = System.nanoTime();
		JavaPairRDD<String, Double> uservisitPairs = uservisitTable
				.mapToPair(new PairFunction<String[], String, Double>() {
					@SuppressWarnings("unchecked")
					public Tuple2<String, Double> call(String[] s)
							throws Exception {
						// TODO Auto-generated method stub
						return new Tuple2(s[0].substring(
								Integer.parseInt(args[3]),
								Integer.parseInt(args[4])), Double
								.parseDouble(s[3]));
					}
				});

		JavaPairRDD<String, Double> aggregationResult = uservisitPairs
				.reduceByKey(new Function2<Double, Double, Double>() {
					public Double call(Double x, Double y) throws Exception {
						return x + y;
					}
				});
		// aggregationResult.count();

		System.out.println("Aggregation of records:");
		for (Tuple2<String, Double> line : aggregationResult.take(10)) {
			System.out.println(line._1 + " " + line._2);
		}
		double AggregationQueryRunningTime = (double) (System.nanoTime() - AggregationQueryStartTime)
				/ Math.pow(10, 9);

		// Java Spark Join Query
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		final Date start_date = format.parse(args[5]);
		final Date end_date = format.parse(args[6]);
		long JoinQueryStartTime = System.nanoTime();
		JavaRDD<String[]> visitRDD = uservisitTable
				.filter(new Function<String[], Boolean>() {
					public Boolean call(String[] x) throws ParseException {
						Date visitDate = format.parse(x[2]);
						return (visitDate.compareTo(start_date) >= 0)
								&& (visitDate.compareTo(end_date) <= 0);
					}
				});
		PairFunction<String[], String, String[]> rankingKeyData = new PairFunction<String[], String, String[]>() {
			public Tuple2<String, String[]> call(String[] x) {
				return new Tuple2(x[0], x);
			}
		};
		JavaPairRDD<String, String[]> rankingPairs = rankingTable
				.mapToPair(rankingKeyData);

		PairFunction<String[], String, String[]> visitKeyData = new PairFunction<String[], String, String[]>() {
			public Tuple2<String, String[]> call(String[] x) {
				return new Tuple2(x[1], x);
			}
		};
		JavaPairRDD<String, String[]> visitPairs = visitRDD
				.mapToPair(visitKeyData);

		JavaPairRDD<String, Tuple2<String[], String[]>> joinTable = visitPairs
				.join(rankingPairs);

		JavaRDD<Tuple2<String, Tuple2<Double, Double>>> sourceIPAsKey = joinTable
				.map(new Function<Tuple2<String, Tuple2<String[], String[]>>, Tuple2<String, Tuple2<Double, Double>>>() {
					public Tuple2<String, Tuple2<Double, Double>> call(
							Tuple2<String, Tuple2<String[], String[]>> t) {
						return new Tuple2(t._2._1[0], new Tuple2(Double
								.parseDouble(t._2._2[1]), Double
								.parseDouble(t._2._1[3])));
					}
				});
		JavaPairRDD<String, Tuple2<Double, Double>> IPKeyPairs = JavaPairRDD
				.fromJavaRDD(sourceIPAsKey);
		JavaPairRDD<String, Tuple2<Tuple2<Double, Double>, Integer>> groupSourceIP = IPKeyPairs
				.mapValues(new Function<Tuple2<Double, Double>, Tuple2<Tuple2<Double, Double>, Integer>>() {
					public Tuple2<Tuple2<Double, Double>, Integer> call(
							Tuple2<Double, Double> t) throws Exception {
						// TODO Auto-generated method stub
						return new Tuple2(t, 1);
					}
				});
		JavaPairRDD<String, Tuple2<Tuple2<Double, Double>, Integer>> reduceByKey = groupSourceIP
				.reduceByKey(new Function2<Tuple2<Tuple2<Double, Double>, Integer>, Tuple2<Tuple2<Double, Double>, Integer>, Tuple2<Tuple2<Double, Double>, Integer>>() {
					public Tuple2<Tuple2<Double, Double>, Integer> call(
							Tuple2<Tuple2<Double, Double>, Integer> t1,
							Tuple2<Tuple2<Double, Double>, Integer> t2)
							throws Exception {
						// TODO Auto-generated method stub
						return new Tuple2(new Tuple2(t1._1._1 + t2._1._1,
								t1._1._2 + t2._1._2), t1._2 + t2._2);
					}
				});

		JavaRDD<Tuple2<Double, Tuple2<Double, String>>> reduceResult = reduceByKey
				.map(new Function<Tuple2<String, Tuple2<Tuple2<Double, Double>, Integer>>, Tuple2<Double, Tuple2<Double, String>>>() {
					public Tuple2<Double, Tuple2<Double, String>> call(
							Tuple2<String, Tuple2<Tuple2<Double, Double>, Integer>> t)
							throws Exception {
						// TODO Auto-generated method stub
						return new Tuple2(t._2._1._2, new Tuple2(t._2._1._1
								/ t._2._2, t._1));
					}
				});
		JavaPairRDD<Double, Tuple2<Double, String>> result = JavaPairRDD
				.fromJavaRDD(reduceResult);
		JavaPairRDD<Double, Tuple2<Double, String>> sortingResult = result
				.sortByKey(false);

		// System.out.println("Here are some examples:");
		for (Tuple2<Double, Tuple2<Double, String>> line : sortingResult
				.take(1)) {
			System.out.println(line._2._2 + " " + line._1 + " " + line._2._1);
		}
		double JoinQueryRunningTime = (double) (System.nanoTime() - JoinQueryStartTime)
				/ Math.pow(10, 9);

		System.out.println("The scan query running time: "
				+ ScanQueryRunningTime + "s");
		System.out
				.println("The AggregationQueryRunningTime query running time: "
						+ AggregationQueryRunningTime + "s");
		System.out.println("The join query running time: "
				+ JoinQueryRunningTime + "s");
	}
}

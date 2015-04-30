/*
 * $SPARK_HOME/bin/spark-submit --class <classname> <jar_file_path> <file_path> <column_index> <definition of the expression>
 * For example: 
 *            $SPARK_HOME/bin/spark-submit --class JavaScan ~/Desktop/JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://localhost:8020/tmp/benchmark/text/tiny/rankings 1 -pn "x" -pt "int" "x > 50"
 * mvn assembly:assembly  
 * */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.codehaus.commons.compiler.IExpressionEvaluator;

public class JavaScan {
	public static void main(String[] args) throws Exception {
		final String[] janinoExp = { "-pn", "x", "-pt", "int", "x > 50" };

		SparkConf conf = new SparkConf().setAppName("Java_Spark");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(args[0]);

		@SuppressWarnings("serial")
		JavaRDD<String[]> rows = lines.map(new Function<String, String[]>() {
			public String[] call(String s) {
				return s.split(",");
			}
		});
		StringExpression exp = new StringExpression(janinoExp);

		IExpressionEvaluator ee = exp.getExpression();
		Class[] parameterTypes = exp.getParameterType();
		JavaRDD<String[]> resultRDD = rows.filter(new Conditions(ee,
				parameterTypes, 1));

		System.out.println("Samples of result records: ");
		for (String[] line : resultRDD.take(10)) {
			System.out.println(line[0] + " " + line[1] + " " + line[2]);
		}
		// System.out.println(resultRDD.count());
	}
}

# BigDataBenchmarkScript
Build the package with all dependencies
```
mvn assembly:assembly
```


Run Java benchmark
'''
$SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/tiny/rankings amplab/text/tiny/uservisits 50 1980-01-01 1980-04-01
'''


Run Python benchmark script
```
python SparkQueryBenchmark.py amplab/text/tiny/rankings 50 amplab/text/tiny/uservisits 1 9 1980-01-01 1980-04-01
```

Run Scala benchmark
```
sbt clean package
```

```
$SPARK_HOME/bin/spark-submit --class ScalaQuery target/scala-2.11/benchmark_scala_2.11-0.0.1.jar amplab/text/tiny/rankings 50 10 amplab/text/tiny/uservisits 0 9 10 1980-01-01 1980-04-01
```









Command to run the JavaQuery:
```
$SPARK_HOME/bin/spark-submit --class <classname> <jar_file_path> <file_path> <column_index> <definition of the expression>
```
```
Usage:
  ExpressionDemo { <option> } <expression> { <parameter-value> }
Compiles and evaluates the given expression and prints its value.
Valid options are
 -et <expression-type>                        (default: any)
 -pn <comma-separated-parameter-names>        (default: none)
 -pt <comma-separated-parameter-types>        (default: none)
 -te <comma-separated-thrown-exception-types> (default: none)
 -di <comma-separated-default-imports>        (default: none)
 -help
The number of parameter names, types and values must be identical.

```

Example:
```
$SPARK_HOME/bin/spark-submit --class JavaScan ~/Desktop/JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://localhost:8020/tmp/benchmark/text/tiny/rankings 1 -pn "x" -pt "int" "x > 50"
```

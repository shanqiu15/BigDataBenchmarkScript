mvn clean assembly

$SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/tiny/rankings amplab/text/tiny/uservisits 50 1980-01-01 1980-04-01

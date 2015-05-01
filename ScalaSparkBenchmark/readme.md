sbt clean package

$SPARK_HOME/bin/spark-submit --class ScalaQuery target/scala-2.11/benchmark_scala_2.11-0.0.1.jar amplab/text/tiny/rankings 100 amplab/text/tiny/uservisits 0 9 1980-01-01 1990-01-01

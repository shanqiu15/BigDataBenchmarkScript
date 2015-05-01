#########################

##########Python#########

#########################
```
python SparkQueryBenchmark.py amplab/text/tiny/rankings 100 amplab/text/tiny/uservisits 0 6 1980-01-01 1990-01-01
```

```
****************Tiny*******************
The execution time of the scan query is  2.63883185387 s
The execution time of the aggregate query is  1.56001591682 s
The execution time of the join query is  0.800143957138 s
***************************************
```

```
python SparkQueryBenchmark.py amplab/text/amplab-1node/text/1node/rankings 100 amplab/text/amplab-1node/text/1node/uservisits 0 6 1980-01-01 1990-01-01
```

```
****************1 node****************
The execution time of the scan query is  1.63474798203 s
The execution time of the aggregate query is  37.4453129768 s
The execution time of the join query is  249.311662912 s
**************************************
```

```
python SparkQueryBenchmark.py amplab/text/amplab-5nodes/text/5nodes/rankings 100 amplab/text/amplab-5nodes/text/5nodes/uservisits 0 6 1980-01-01 1990-01-01
```

```
*****************5 nodes**************
The execution time of the scan query is  13.1509649754 s
The execution time of the aggregate query is  150.91179204 s
The execution time of the join query is  1297.20703197 s
**************************************
```


##########################

##########JAVA############

##########################

```
$SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/tiny/rankings amplab/text/tiny/uservisits 100 0 6 1980-01-01 1990-01-01
```

```
******************Tiny*******************
The scan query running time: 2.109618403s
The aggregation query running time: 1.443105954s
The join query running time: 0.936174552s
*****************************************
```

```
$SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/amplab-1node/text/1node/rankings amplab/text/amplab-1node/text/1node/uservisits 100 0 6 1980-01-01 1990-01-01
```

```
*****************1 node********************
The scan query running time: 1.301711854s
The aggregation query running time: 21.601940295s
The join query running time: 155.154520489s
*******************************************
```

```
$SPARK_HOME/bin/spark-submit --class BenchmarkJavaSpark JavaSparkBenchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar amplab/text/amplab-5nodes/text/5nodes/rankings amplab/text/amplab-5nodes/text/5nodes/uservisits 100 0 6  1980-01-01 1990-01-01
```

```
***************5 nodes********************
The scan query running time: 1.18050955s
The aggregation query running time: 66.838207917s
The join query running time: 684.924778199s
******************************************
```


############################

###########SCALA############

############################

```
$SPARK_HOME/bin/spark-submit --class ScalaQuery target/scala-2.11/benchmark_scala_2.11-0.0.1.jar amplab/text/tiny/rankings 100 amplab/text/tiny/uservisits 0 6 1980-01-01 1990-01-01
```

```
******************Tiny*********************
Scan query running time: 2.203572966s
Aggregation query running time: 1.401222391s
Join query running time: 2.310737497s
*******************************************
```

```
$SPARK_HOME/bin/spark-submit --class ScalaQuery target/scala-2.11/benchmark_scala_2.11-0.0.1.jar amplab/text/amplab-1node/text/1node/rankings 100 amplab/text/amplab-1node/text/1node/uservisits 0 6 1980-01-01 1990-01-01
```

```
******************1 node*******************
Scan query running time: 1.278213784s
Aggregation query running time: 23.957651269s
Join query running time: 167.660045705s
*******************************************
```


```
$SPARK_HOME/bin/spark-submit --class ScalaQuery target/scala-2.11/benchmark_scala_2.11-0.0.1.jar amplab/text/amplab-5nodes/text/5nodes/rankings 100 amplab/text/amplab-5nodes/text/5nodes/uservisits 0 6 1980-01-01 1990-01-01
```

```
******************5 node*******************
Scan query running time: 1.440812309s
Aggregation query running time: 65.024718615s
Join query running time: 681.216359976s
*******************************************
```


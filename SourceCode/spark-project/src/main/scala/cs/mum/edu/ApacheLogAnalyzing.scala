package cs.mum.edu

import cs.mum.edu.util.ApacheAccessLog
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Sokly on 6/10/16.
  */

object ApacheLogAnalyzing {
  def main(args: Array[String]) {
    val sparkConfig = new SparkConf().setAppName("Apache Log Analyzing")
    val sc = new SparkContext(sparkConfig)
    val output = StringBuilder.newBuilder
    // Check for the empty input
    if (args.length == 0) {
      System.out.println("No input file specified!")
      System.exit(-1)
    }

    val inputData = args(0)
    val logText = sc.textFile(inputData).map(ApacheAccessLog.parseLogLine).cache()
    //    println(s"Total Records:${logText.count()}")
    output.append("Total Access Log Records: %s \n".format(logText.count()))

    // Compute Request Method to Count
    val httpMethods = logText.map(log => log.method).cache()
    val count = httpMethods.map(m => (m, 1)).reduceByKey(_ + _).sortByKey(true).take(100)
    //    println(s"""Request HTTP Method counts: ${count.mkString("[", ",", "]")}""")
    output.append("Each method request to the server.\n")
    count.foreach(num => {
      output.append("\t - HTTP Method : %s requested %s time(s)\n".format(num._1, num._2))
    })

    // Compute Response Code to Count.
    val httpResponseCode = logText
      .map(log => (log.responseCode, 1))
      .reduceByKey(_ + _).sortByKey(true)
      .take(100)
    println(s"""Response code counts: ${httpResponseCode.mkString("[", ",", "]")}""")
    output.append("Each response code from the server.\n")
    httpResponseCode.foreach(num => {
      output.append("\t - Response Code : %s happened %s time(s)\n".format(num._1, num._2))
    })

    //Save output to text file in output folder
    if (args(1) != "") {
      //convert result to RDD form to convert to text file
      val result = sc.parallelize(output.split('\n'))
      result.saveAsTextFile(args(1))
    }
    sc.stop()

  }
}

package org.elasticsearch;

import scala.reflect.ClassTag
import org.apache.spark.SparkContext
import org.apache.spark.TaskContext
import org.apache.spark.rdd.RDD
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._
import org.elasticsearch.hadoop.cfg.PropertiesSettings
import org.elasticsearch.hadoop.cfg.PropertiesSettings
import org.elasticsearch.hadoop.cfg.Settings
import org.elasticsearch.spark.cfg.SparkSettingsManager
import org.elasticsearch.spark.rdd.EsRDDWriter
import org.elasticsearch.spark.api.java.JavaEsSpark
import org.elasticsearch.spark.rdd.EsRDDFunctions


package object spark {

  implicit def sparkContextFunctions(sc: SparkContext)= new SparkContextFunctions(sc)

  class SparkContextFunctions(sc: SparkContext) extends Serializable {
    def esRDD() = EsRDDFunctions.esRDD(sc)
    def esRDD(resource: String) = EsRDDFunctions.esRDD(sc, resource)
    def esRDD(resource: String, query: String) = EsRDDFunctions.esRDD(sc, resource, query)
    def esRDD(params: scala.collection.Map[String, String]) = EsRDDFunctions.esRDD(sc, params)
  }
  
  implicit def sparkRDDFunctions[T : ClassTag](rdd: RDD[T]) = new SparkRDDFunctions[T](rdd)

  class SparkRDDFunctions[T : ClassTag](rdd: RDD[T]) extends Serializable {
    def saveToEs(resource: String) { EsRDDFunctions.saveToEs(rdd, resource) }
    def saveToEs(resource: String, params: scala.collection.Map[String, String]) { EsRDDFunctions.saveToEs(rdd, resource, params) }
    def saveToEs(params: scala.collection.Map[String, String]) { EsRDDFunctions.saveToEs(rdd, params)    }
  }
    
  implicit def sparkStringJsonRDDFunctions(rdd: RDD[String]) = new SparkJsonRDDFunctions[String](rdd)
  implicit def sparkByteArrayJsonRDDFunctions(rdd: RDD[Array[Byte]]) = new SparkJsonRDDFunctions[Array[Byte]](rdd)

  class SparkJsonRDDFunctions[T : ClassTag](rdd: RDD[T]) extends Serializable {
    def saveJsonToEs(resource: String) { EsRDDFunctions.saveJsonToEs(rdd, resource) }
    def saveJsonToEs(resource: String, params: scala.collection.Map[String, String]) { EsRDDFunctions.saveJsonToEs(rdd, resource, params) }
    def saveJsonToEs(params: scala.collection.Map[String, String]) { EsRDDFunctions.saveJsonToEs(rdd, params) }
  }
}
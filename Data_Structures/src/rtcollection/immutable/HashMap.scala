package rtcollection.immutable

import scala.collection.mutable
/**
  * Hash map implementation.
  */
case class KVPair[K, V](key: K, value: V)

class HashMap[K, V] {
  var currentPopulation = 0
  var innerArray: Array[mutable.ListBuffer[KVPair[K, V]]] = new Array(HashMap.INIT_ARR_SIZE)
  var insertions: mutable.ListBuffer[KVPair[K, V]] = mutable.ListBuffer()

  def +=(pair: KVPair[K, V]): Unit = {
    tryReload()
    insertIncrement(pair)
  }

  def -=(key : K) : Option[KVPair[K, V]] = {
    val index = hashIndex(key)
    val targetBucket = innerArray(index)
    val pulledPair = if(targetBucket != null) targetBucket.find(keyedWith(key)) else None
    pulledPair match {
      case Some(foundPair) => targetBucket -= foundPair
      case _ =>
    }
    pulledPair
  }

  def keyedWith(key : K) : KVPair[K, V] => Boolean = pair => pair.key.equals(key)

  def get(key: K): Option[V] = {
    val index: Int = hashIndex(key)
    val result = innerArray(index).find(_.key == key)

    if (result.isDefined) Some(result.get.value) else None
  }

  private def hashIndex(key: K): Int = {
    key.hashCode() % innerArray.length
  }

  private def atReload: Boolean = {
    val status: Float = currentPopulation / innerArray.length.toFloat

    status >= HashMap.LOAD_FACTOR
  }

  private def tryReload() = if (atReload) reload()

  private def reload(): Unit = {
    val size = innerArray.length * 2
    val newArray = new Array[mutable.ListBuffer[KVPair[K, V]]](size)

    innerArray = newArray

    val reHash: (KVPair[K, V]) => Unit = insertHash(newArray, _)

    insertions.foreach(reHash)
    innerArray = newArray
  }

  private def insertIncrement(pair: KVPair[K, V]): Unit = {
    insertHash(innerArray, pair)
    currentPopulation += 1
    insertions += pair
  }

  private def insertHash(array: Array[mutable.ListBuffer[KVPair[K, V]]], pair: KVPair[K, V]): Unit = {
    val index = hashIndex(pair.key)
    if (array(index) == null) array(index) = mutable.ListBuffer()
    array(index) += pair
  }
}

/**
  * Constants object for HashMap.
  */
object HashMap {
  val INIT_ARR_SIZE = 16
  val LOAD_FACTOR = 0.75f
}
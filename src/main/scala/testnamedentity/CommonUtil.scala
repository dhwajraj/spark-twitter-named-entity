package testnamedentity

import org.apache.spark.SparkContext
import org.apache.commons.lang.StringUtils
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import edu.stanford.nlp.process.TokenizerFactory
import edu.stanford.nlp.process.CoreLabelTokenFactory
import edu.stanford.nlp.process.PTBTokenizer
import edu.stanford.nlp.ling.CoreLabel

object CommonUtil {
  def textCleaning(s:String): String={
    var st=s
    st =st.replaceAll("[\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\+\\=\\-\\{\\}\\[\\]\\;\\:\\'\\\"\\<\\>\\,\\.\\/\\?]", "")
    //println(s+"\t"+st)
    st
  }
  
  def initModel(): MaxentTagger = {
    println("SentenceSplitterTransform: inside INIT: LOADING POS MODEL")
    new MaxentTagger("/Users/dhwaj/Downloads/gate-EN-twitter.model")
  }
  
  def getTokenizerFactory(): TokenizerFactory[CoreLabel] = {
    PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep,ptb3Escaping=false,ptb3Ellipsis=false,ptb3Dashes=false")
  }
  
  def isProperNoun(tag:String, word:String) : Boolean = {
    tag.startsWith("NNP")&&(!word.startsWith("http"))
  }
  
  def isSpecialAdjoiningTag(tag: String, word: String): Boolean = {
    var a = word.replaceAll("[\\,\\@\\&\\-\\(\\)\\\"\\']+", "")
    var ret = false
    if (a.trim().isEmpty())
      ret = true
    if (tag.equals(word))
      ret = true
    ret
  }
}
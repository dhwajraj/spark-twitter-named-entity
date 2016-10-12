package testnamedentity

import edu.stanford.nlp.ling.TaggedWord
import edu.stanford.nlp.process.DocumentPreprocessor
import java.io.StringReader
import scala.collection.mutable.HashSet
import com.google.inject.internal.ErrorHandler
import edu.stanford.nlp.ling.HasWord
import scala.collection.JavaConversions._

object NETransform {
  var tagger = CommonUtil.initModel()
  
  def extractNamedEntities(tweet:String)={

    if (tagger == null) {
      tagger = CommonUtil.initModel()
      if (tagger == null) {
        println("MaxentTagger initialization failed.. So, shutting down")
        scala.sys.exit()
      }
    }
    var neChunks = HashSet[String]()
    val documentPreprocessor = new DocumentPreprocessor(new StringReader(tweet))
    documentPreprocessor.setTokenizerFactory(CommonUtil.getTokenizerFactory())
    var it = documentPreprocessor.iterator()
    while (it.hasNext()) {
      var sentence = it.next()
      var namedEntityChunksPerSentence = getNamedEntityFromSentence(sentence)
      neChunks++=namedEntityChunksPerSentence
    }
    neChunks
  }
  
  private def getNamedEntityFromSentence(sentence: java.util.List[HasWord]):HashSet[String] = {
    var nnp = HashSet[String]()
    var tSentence: List[TaggedWord] = tagger.tagSentence(sentence).toList // java conversion
    //println(tSentence)
    
    var last="aa"
    var last_w="aa"
    var nnp_s0=""
    var sentenceLength = tSentence.length
    var ctr = 0
    for(tw <- tSentence){
      ctr=ctr+1
      var isCurrentNP=CommonUtil.isProperNoun(tw.tag, tw.word)
      var isLastNP = CommonUtil.isProperNoun(last, last_w)
      if (isCurrentNP || CommonUtil.isSpecialAdjoiningTag(tw.tag(), tw.value())) {
        if (isLastNP)
          nnp_s0 = nnp_s0 + " " + tw.value()
        else if(isCurrentNP)
          nnp_s0 = tw.value()
      } else if (isLastNP) {
        if(!nnp_s0.trim().isEmpty())
          nnp.add(nnp_s0.trim())
        nnp_s0 = ""
      }
      if (ctr == sentenceLength - 1) {
        if(!nnp_s0.trim().isEmpty())
          nnp.add(nnp_s0.trim())
        nnp_s0 = ""
      }
      last = tw.tag()
      last_w=tw.word()
    }
    nnp
  }
}
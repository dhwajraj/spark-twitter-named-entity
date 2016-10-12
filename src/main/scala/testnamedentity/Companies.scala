package testnamedentity


object Companies{
  
  def load() = {
    val source = scala.io.Source.fromURL(this.getClass.getResource("/companies.csv"))
    val companies = source.getLines().map(_.split("\\|")).collect {
      case Array(rank, name, url) => (name,BigDecimal(rank))
    }
    val top = companies.toVector.sortBy(_._2).map(_._1).take(200)
    println( top)
    top
  }
}

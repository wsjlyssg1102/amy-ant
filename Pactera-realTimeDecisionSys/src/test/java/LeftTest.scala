/**
  * Created by 28269 on 2017/5/27.
  */
object LeftTest  extends  App{


  def divideXByY(x: Int, y: Int): Either[String, Int] = {
    if (y == 0) Left("Dude, can't divide by 0")
    else Right(x / y)

  }
  println(divideXByY(1,0).left.get)

  println(divideXByY(1,1).right.get)

  divideXByY(1, 0) match {
    case Left(s) => println("Answer: " + s)
    case Right(i) => println("Answer: " + i)
  }
  divideXByY(1, 1) match {
    case Left(s) => println("Answer: " + s)
    case Right(i) => println("Answer: " + i)
  }
}

package akka.stream

import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


object StreamUtil {

  /**
    * send to all targets the given msg.
    * in this case, all received responses will collect to one [[Seq]] and return to origin(sender).
    *
    * @param origin       [[akka.actor.ActorRef]] ref where to send the response at the end
    * @param targets      [[List]] of [[ActorRef]] which refs have to ask the question
    * @param msg          [[Any]] message to ask to targets
    * @param usingThreads [[Option]] of [[Int]] specified count of threads to take, default is targets length
    * @param ec           [[scala.concurrent.ExecutionContext]] executioncontext for this routine
    * @param materializer [[akka.stream.Materializer]] materializer for this routine
    * @param timeout      [[akka.util.Timeout]] have much time could take for a request maximum
    */
  def collect(origin: ActorRef, targets: List[ActorRef], msg: Any, usingThreads: Option[Int] = None)
             (implicit ec: ExecutionContext, materializer: Materializer, timeout: Timeout): Unit =
    Source(targets)
      .mapAsync(usingThreads.getOrElse(targets.length))(_ ? msg)
      .runFold(Seq.empty[Any]) {
        case (result, response) => result :+ response
      } onComplete {
      case Success(result) => origin ! result
      case Failure(exception) => exception.printStackTrace()
    }

  /**
    * use this for collecting and processing the responses from targets.
    * in this implementation is no difference between fault and empty results at the origin visible.
    *
    * @param origin            [[akka.actor.ActorRef]] ref where to send the response at the end
    * @param targets           [[List]] of [[ActorRef]] which refs have to ask the question
    * @param question          [[Any]] message to ask to targets
    * @param filtering         [[scala.PartialFunction]] match case block for find the right responses
    * @param mapping           [[scala.PartialFunction]] partial block to map responses into [[scala.collection.immutable.Seq]] of [[T]]
    * @param responseMaker     [[scala.Function1]] factory to build the response
    * @param noContentResponse [[Any]] message to send to origin if fault occurred or no content available
    * @param usingThreads      [[Option]] of [[Int]] specified count of threads to take, default is targets length
    * @param ec                [[scala.concurrent.ExecutionContext]] executioncontext for this routine
    * @param materializer      [[akka.stream.Materializer]] materializer for this routine
    * @param timeout           [[akka.util.Timeout]] have much time could take for a request maximum
    * @tparam T content type
    */
  def collectAdvanced[T](origin: ActorRef, targets: List[ActorRef], question: Any,
                         filtering: PartialFunction[Any, Boolean],
                         mapping: PartialFunction[Any, Seq[T]],
                         responseMaker: Seq[T] => Any,
                         noContentResponse: Any,
                         usingThreads: Option[Int] = None)
                        (implicit ec: ExecutionContext,
                         materializer: Materializer,
                         timeout: Timeout): Unit = {
    Source(targets)
      .mapAsync(usingThreads.getOrElse(targets.length))(_ ? question)
      .filter(filtering)
      .map(mapping)
      .runFold(Seq.empty[T]) {
        case (result, chunk) => result ++ chunk
      } onComplete {
      case Success(result) if result.isEmpty => origin ! noContentResponse
      case Success(result) => origin ! responseMaker(result)
      case Failure(exception) =>
        exception.printStackTrace()
        origin ! noContentResponse
    }
  }

}

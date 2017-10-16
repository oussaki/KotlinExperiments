import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*
import java.util.concurrent.TimeUnit


suspend fun run() {
    delay(2000)
    println("run this")
}

fun <T> List<T>.getIfPresent(idx: Int) =
        if (idx >= size) {
            Optional.empty()
        } else {
            Optional.of(get(idx))
        }

fun `hello from the other side`() {

    val task = async(CommonPool) {
        delay(1900)
        println("AsyncResult")
        "task result"
    }

    var function = fun(x: Int) {
        x.inc()
        x.minus(4)
        println("x = $x ")
    }

    function.invoke(7)

    launch(CommonPool) {
        val timeout = withTimeoutOrNull(2, TimeUnit.SECONDS) { task.await() }
        println("Result is :".plus(timeout.toString()))
    }

}

fun main(args: Array<String>) {
    `hello from the other side`();

    async(CommonPool) {
        bg {
            "Async"
        }.await()
    }
    runBlocking {
        run()
    }
    println("start")
    launch(CommonPool) {
        delay(1000)
        println("Inside launch")
    }
    Thread.sleep(2000)
    println("end")


    Flowable.range(1, 20)
            .parallel()
            .runOn(Schedulers.computation())
            .map { t -> t * 4 }
            .sequential()
            .blockingSubscribe { t -> println("t=$t") }
}
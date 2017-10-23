import javafx.application.Application

fun main(args: Array<String>) {
    println("Kotlin Start")
    Application.launch(CommonPool) {
        delay(2000)
        println("Kotlin Inside")
    }
    println("Kotlin End")
}

fun doSomething(){
    delay(2000)
}
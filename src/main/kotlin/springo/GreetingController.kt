package springo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.springframework.data.repository.CrudRepository



@RestController
class GreetingController {

    val counter = AtomicLong()

    @Autowired
    lateinit internal var userRepo: UserRepository

    @RequestMapping("user/{name}/{email}/{phone}")
    fun user(@PathVariable(name = "name") name: String, @PathVariable(name = "email") email: String,
             @PathVariable(name = "phone") phone: String): User {
        return User(counter.incrementAndGet().toInt(), name, email, phone)
    }

    @RequestMapping("user/add/{name}/{email}/{phone}")
    fun addUser(@PathVariable(name = "name") name: String, @PathVariable(name = "email") email: String,
                @PathVariable(name = "phone") phone: String): User {
        var user = User(counter.incrementAndGet().toInt(), name, email, phone)
        userRepo.save(user)
        val aUser = userRepo.findOne(1).copy()
        print("User getted from db: ${aUser}")

        return user;
    }


    @RequestMapping("user/{id}")
    fun findUser(@PathVariable(name = "id") id: Int): Any {
        val user = userRepo!!.findOne(id)!!.copy()
        print("User getted from db: ${user!!}");
        if (user != null)
            return user
        else
            return "user not found"

    }


    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

}

internal interface UserRepository : CrudRepository<User, Int>

@Entity data class User(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Int? = null, val name: String? = null, val email: String? = null, val phone: String? = null)
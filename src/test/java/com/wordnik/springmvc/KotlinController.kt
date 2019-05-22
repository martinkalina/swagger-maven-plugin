package com.wordnik.springmvc

import com.wordnik.sample.model.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.Deprecated

@RestController
class KotlinController {

    @PostMapping("/userWithDefVal")
    fun post(@RequestBody body: User, @RequestParam param: String = "defaultValue"){

    }
}

package me.lucasmarques.challenge.planets

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/planets")
class PlanetController {

    @GetMapping
    fun hello(): String {
        return "Hello, World!!!"
    }

}
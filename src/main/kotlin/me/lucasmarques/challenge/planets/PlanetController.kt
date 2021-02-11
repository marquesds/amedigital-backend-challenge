package me.lucasmarques.challenge.planets

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/v1/planets")
class PlanetController {

    @Autowired
    lateinit var planetService: PlanetService

    @GetMapping
    fun getPlanets(@RequestParam("name") name: String?): List<PlanetDTO> {
        if (name.isNullOrEmpty()) {
            return planetService.list()
        }

        return listOf(planetService.findByName(name)!!)
    }

    @GetMapping("/{id}")
    fun getPlanetById(@PathVariable id: Long): PlanetDTO? {
        val planet = planetService.findById(id)
        planet?.let {
            return it
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun savePlanet(@RequestBody planetDTO: PlanetDTO): PlanetDTO? {
        return planetService.save(planetDTO)
    }

    @DeleteMapping
    fun deletePlanetByName(@RequestParam("name") name: String) {
        planetService.deleteByName(name)
    }

}
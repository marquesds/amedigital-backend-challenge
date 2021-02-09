package me.lucasmarques.challenge.planets.fixtures

import me.lucasmarques.challenge.planets.PlanetEntity
import me.lucasmarques.challenge.planets.PlanetRepository
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlanetFixture {

    @Autowired
    lateinit var repository: PlanetRepository

    fun fakeTatooineJsonResponse(): JSONObject {
        val films = listOf(
            "http://swapi.dev/api/films/1/",
            "http://swapi.dev/api/films/3/",
            "http://swapi.dev/api/films/4/",
            "http://swapi.dev/api/films/5/",
            "http://swapi.dev/api/films/6/"
        )

        val results = mapOf("results" to listOf(mapOf("films" to films)))
        return JSONObject(results)
    }

    fun savePlanets() {
        val planet1 = PlanetEntity(null, "Alderaan", "temperate", "grasslands, mountains", 2)
        val planet2 = PlanetEntity(null, "Yavin IV", "temperate, tropical", "jungle, rainforests", 1)
        val planet3 = PlanetEntity(null, "Hoth", "frozen", "tundra, ice caves, mountain ranges", 1)
        val planet4 = PlanetEntity(null, "Dagobah", "murky", "swamp, jungles", 3)
        val planet5 = PlanetEntity(null, "Bespin", "temperate", "gas giant", 1)

        repository.saveAll(listOf(planet1, planet2, planet3, planet4, planet5))
    }

}
package me.lucasmarques.challenge.planets

import io.mockk.every
import io.mockk.mockk
import khttp.responses.Response
import me.lucasmarques.challenge.ext.swapi.impl.StarWarsAPIClient
import me.lucasmarques.challenge.planets.fixtures.PlanetFixture
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@SpringBootTest
class PlanetServiceTest {

    @Autowired
    lateinit var planetService: PlanetService

    @Autowired
    lateinit var planetFixture: PlanetFixture

    @Test
    fun testShouldSaveNonexistentStarWarsPlanet() {
        val mockApi = mockk<StarWarsAPIClient>()
        val planetName = "Earth"

        every { mockApi.getResponse("/planets/?search=$planetName") } returns null

        val planetDTO = PlanetDTO(name = planetName, climate = "Arid", terrain = "Desert")
        val savedPlanet = planetService.save(planetDTO)

        expectThat(savedPlanet?.name).isEqualTo(planetDTO.name)
        expectThat(savedPlanet?.totalFilmsAppearing).isEqualTo(0)
    }

    @Test
    fun testShouldSaveExistentPlanet() {
        val mockResponse = mockk<Response>()
        val mockApi = mockk<StarWarsAPIClient>()

        val planetName = "Tatooine"

        every { mockResponse.statusCode } returns 200
        every { mockResponse.jsonObject } returns planetFixture.fakeTatooineJsonResponse()
        every { mockApi.getResponse("/planets/?search=$planetName") } returns mockResponse

        val planetDTO = PlanetDTO(name = planetName, climate = "Arid", terrain = "Desert")
        val savedPlanet = planetService.save(planetDTO)

        expectThat(savedPlanet?.name).isEqualTo(planetDTO.name)
        expectThat(savedPlanet?.totalFilmsAppearing).isEqualTo(5)
    }

    @Test
    fun testShouldListSavedPlanets() {
        planetFixture.savePlanets()
        val planets = planetService.list()

        expectThat(planets).hasSize(5)
        expectThat(planets[0].name).isEqualTo("Alderaan")
        expectThat(planets[5].name).isEqualTo("Bespin")
    }

    @Test
    fun testShouldNotFindPlanets() {
        val planets = planetService.list()
        expectThat(planets).hasSize(0)
    }

}
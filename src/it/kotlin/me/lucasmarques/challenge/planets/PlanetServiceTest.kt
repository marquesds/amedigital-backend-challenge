package me.lucasmarques.challenge.planets

import io.mockk.every
import io.mockk.mockk
import khttp.responses.Response
import me.lucasmarques.challenge.ext.swapi.impl.StarWarsAPIClient
import me.lucasmarques.challenge.planets.fixtures.PlanetFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@SpringBootTest
class PlanetServiceTest {

    @Autowired
    lateinit var planetService: PlanetService

    @Autowired
    lateinit var planetRepository: PlanetRepository

    @Autowired
    lateinit var planetFixture: PlanetFixture

    @BeforeEach
    fun before() {
        planetFixture.deletePlanets()
    }

    @Test
    fun testShouldSaveNonexistentStarWarsPlanet() {
        val planetName = "Earth"
        val mockApi = mockk<StarWarsAPIClient>()

        every { mockApi.getResponse("/planets/?search=$planetName") } returns null

        val planetDTO = PlanetDTO(name = planetName, climate = "Arid", terrain = "Desert")
        val savedPlanet = planetService.save(planetDTO)

        expectThat(savedPlanet?.name).isEqualTo(planetDTO.name)
        expectThat(savedPlanet?.totalFilmsAppearing).isEqualTo(0)
    }

    @Test
    fun testShouldSaveExistentPlanet() {
        val planetName = "Tatooine"

        val mockResponse = mockk<Response>()
        val mockApi = mockk<StarWarsAPIClient>()

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
        expectThat(planets[4].name).isEqualTo("Bespin")
    }

    @Test
    fun testShouldNotFindPlanets() {
        val planets = planetService.list()
        expectThat(planets).hasSize(0)
    }

    @Test
    fun testShouldFindPlanetByName() {
        val planetName = "Alderaan"
        planetFixture.savePlanets()
        val planetDTO = planetService.findByName(planetName)

        expectThat(planetDTO?.name).isEqualTo(planetName)
    }

    @Test
    fun testShouldNotFindNonexistentPlanetByName() {
        val planetName = "Earth"
        planetFixture.savePlanets()
        val planetDTO = planetService.findByName(planetName)

        expectThat(planetDTO).isNull()
    }

    @Test
    fun testShouldFindPlanetById() {
        val planetName = "Alderaan"
        planetFixture.savePlanets()
        val planetEntity = planetRepository.findByName(planetName)
        val planetDTO = planetService.findById(planetEntity!!.id!!)

        expectThat(planetDTO?.name).isEqualTo("Alderaan")
    }

    @Test
    fun testShouldNotFindNonexistentPlanetById() {
        val planetDTO = planetService.findById(8000)
        expectThat(planetDTO).isNull()
    }

    @Test
    fun testShouldDeletePlanetByName() {
        val planetName = "Alderaan"

        planetFixture.savePlanets()
        val affectedRows = planetService.deleteByName(planetName)

        val planetDTO = planetService.findByName(planetName)
        expectThat(affectedRows).isEqualTo(1)
        expectThat(planetDTO).isNull()
    }

    @Test
    fun testShouldNotDeleteNonexistentPlanetByName() {
        val affectedRows = planetService.deleteByName("Earth")

        expectThat(affectedRows).isEqualTo(0)
    }

}
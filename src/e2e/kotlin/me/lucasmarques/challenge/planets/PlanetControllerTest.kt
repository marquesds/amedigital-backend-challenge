package me.lucasmarques.challenge.planets

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import khttp.responses.Response
import me.lucasmarques.challenge.ext.swapi.impl.StarWarsAPIClient
import me.lucasmarques.challenge.planets.fixtures.PlanetFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanetControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var planetFixture: PlanetFixture

    @Autowired
    lateinit var planetRepository: PlanetRepository

    val json = jacksonObjectMapper()
    val planetsTypeRef = object : TypeReference<List<PlanetDTO>>() {}

    @BeforeEach
    fun before() {
        planetFixture.deletePlanets()
    }

    @Test
    fun testShouldListAllPlanets() {
        planetFixture.savePlanets()

        val result = restTemplate.getForEntity("/v1/planets/", String::class.java)
        val planets: List<PlanetDTO> = json.readValue(result.body, planetsTypeRef)

        expectThat(result.statusCode).isEqualTo(HttpStatus.OK)
        expectThat(planets).hasSize(5)
        expectThat(planets[0].name).isEqualTo("Alderaan")
        expectThat(planets[4].name).isEqualTo("Bespin")
    }

    @Test
    fun testShouldReturnEmptyListForNonexistentPlanets() {
        val result = restTemplate.getForEntity("/v1/planets/", List::class.java)

        expectThat(result.statusCode).isEqualTo(HttpStatus.OK)
        result.body?.let { expectThat(it.toList()).hasSize(0) }
    }

    @Test
    fun testShouldFindPlanetById() {
        planetFixture.savePlanets()

        val foundPlanet = planetRepository.findByName("Alderaan")?.toDTO()

        val result = restTemplate.getForEntity("/v1/planets/${foundPlanet?.id}", PlanetDTO::class.java)
        val planet: PlanetDTO = result.body!!

        expectThat(result.statusCode).isEqualTo(HttpStatus.OK)
        expectThat(planet.name).isEqualTo("Alderaan")
    }

    @Test
    fun testShouldNotFindNonexistentPlanetById() {
        val result = restTemplate.getForEntity("/v1/planets/8000", String::class.java)
        expectThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun testShouldSavePlanet() {
        val planetName = "Tatooine"

        val mockResponse = mockk<Response>()
        val mockApi = mockk<StarWarsAPIClient>()

        every { mockResponse.statusCode } returns 200
        every { mockResponse.jsonObject } returns planetFixture.fakeTatooineJsonResponse()
        every { mockApi.getResponse("/planets/?search=$planetName") } returns mockResponse

        val planetDTO = PlanetDTO(name = planetName, climate = "Arid", terrain = "Desert")
        val request = HttpEntity(planetDTO)

        val result = restTemplate.postForEntity("/v1/planets/", request, PlanetDTO::class.java)

        expectThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
    }

    @Test
    fun testShouldDeletePlanet() {
        planetFixture.savePlanets()
        val planetName = "Alderaan"
        restTemplate.delete("/v1/planets/?name=$planetName")
        val foundPlanet = planetRepository.findByName(planetName)

        expectThat(foundPlanet).isNull()
    }

}
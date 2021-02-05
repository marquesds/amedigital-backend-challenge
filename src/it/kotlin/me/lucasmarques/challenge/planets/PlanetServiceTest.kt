package me.lucasmarques.challenge.planets

import me.lucasmarques.challenge.ext.swapi.impl.StarWarsAPIClient
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
class PlanetServiceTest {

    @Autowired
    lateinit var planetService: PlanetService

    val mockStarWarsAPIClient: StarWarsAPIClient = Mockito.mock(StarWarsAPIClient::class.java)

    @Test
    fun testSavePlanet() {
        val planetDTO = PlanetDTO(name = "Tatooine", climate = "Arid", terrain = "Dessert", totalFilmsAppearing = 5)
        `when`(mockStarWarsAPIClient.getPlanetTotalFilmsAppearing(planetDTO.name)).thenReturn(5)
        val savedPlanet = planetService.save(planetDTO)

        expectThat(savedPlanet!!.name).isEqualTo(planetDTO.name)
    }

}
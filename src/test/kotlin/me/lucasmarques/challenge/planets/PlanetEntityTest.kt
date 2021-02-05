package me.lucasmarques.challenge.planets

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
class PlanetEntityTest {
    @Test
    fun testConvertEntityToDTO() {
        val planetEntity =
            PlanetEntity(name = "Tatooine", climate = "Arid", terrain = "Dessert", totalFilmsAppearing = 5)

        expectThat(planetEntity.toDTO())
            .isEqualTo(PlanetDTO(name = "Tatooine", climate = "Arid", terrain = "Dessert", totalFilmsAppearing = 5))
    }

}
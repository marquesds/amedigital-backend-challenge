package me.lucasmarques.challenge.planets

import org.springframework.context.annotation.Bean
import javax.persistence.*

@Entity(name = "planet")
data class PlanetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val climate: String,

    @Column(nullable = false)
    val terrain: String,

    @Column(nullable = false)
    val totalFilmsAppearing: Int
) {
    @Bean
    fun toDTO(): PlanetDTO {
        return PlanetDTO(
            name = this.name,
            climate = this.climate,
            terrain = this.terrain,
            totalFilmsAppearing = this.totalFilmsAppearing
        )
    }
}
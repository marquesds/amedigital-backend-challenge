package me.lucasmarques.challenge.planets

import me.lucasmarques.challenge.ext.swapi.IStarWarsAPIClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PlanetService @Autowired constructor(
    var starWarsAPIClient: IStarWarsAPIClient,
    var planetRepository: PlanetRepository
) {

    fun save(planet: PlanetDTO): PlanetDTO? {
        val totalFilmsAppearing = starWarsAPIClient.getPlanetTotalFilmsAppearing(planet.name)
        val entity = PlanetEntity(null, planet.name, planet.climate, planet.terrain, totalFilmsAppearing)
        return planetRepository.save(entity).toDTO()
    }

    fun list(): List<PlanetDTO> {
        return planetRepository.findAll().map { it.toDTO() }
    }

    fun findByName(name: String): PlanetDTO? {
        return planetRepository.findByName(name)?.toDTO()
    }

    fun findById(id: Long): PlanetDTO? {
        return planetRepository.findById(id).orElse(null)?.toDTO()
    }

    @Transactional
    fun deleteByName(name: String): Long {
        return planetRepository.deleteByName(name)
    }
}
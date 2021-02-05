package me.lucasmarques.challenge.planets

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanetRepository : CrudRepository<PlanetEntity, Long>{
}
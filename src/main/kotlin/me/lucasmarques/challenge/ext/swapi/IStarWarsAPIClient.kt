package me.lucasmarques.challenge.ext.swapi

interface IStarWarsAPIClient {
    fun getPlanetTotalFilmsAppearing(name: String): Int
}
package me.lucasmarques.challenge.ext.swapi.impl

import khttp.get
import khttp.responses.Response
import me.lucasmarques.challenge.ext.swapi.IStarWarsAPIClient
import me.lucasmarques.challenge.infra.Config
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class StarWarsAPIClient : IStarWarsAPIClient {

    @Autowired
    lateinit var config: Config

    override fun getPlanetTotalFilmsAppearing(name: String): Int {
        val planetsURI = "${config.swapiURL}/planets/?search=$name"
        val response: Response? = getResponse(planetsURI)

        response?.let {
            if (it.statusCode == HttpStatus.OK.value()) {
                val emptyFilms = JSONObject(mapOf("films" to emptyList<String>()))
                val result = it.jsonObject.getJSONArray("results").elementAtOrElse(0) { emptyFilms } as JSONObject
                return result.getJSONArray("films").length()
            }
        }

        return 0
    }

    private fun getResponse(uri: String): Response? {
        return try {
            get(uri)
        } catch (ex: Throwable) {
            null
        }
    }

}
package me.lucasmarques.challenge.infra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Value("\${config.swapi.url}")
    lateinit var swapiURL: String

}
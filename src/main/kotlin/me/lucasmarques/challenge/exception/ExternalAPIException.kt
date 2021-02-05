package me.lucasmarques.challenge.exception

import org.springframework.http.HttpStatus

data class ExternalAPIException(override val message: String, val statusCode: HttpStatus) : Exception(message)
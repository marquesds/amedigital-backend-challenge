package me.lucasmarques.challenge.health

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HealthCheckControllerTest {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testHealthCheckEndpoint() {
        val result = restTemplate.getForEntity("/health", String::class.java)

        expectThat(result.statusCode).isEqualTo(HttpStatus.OK)
        expectThat(result.body).isEqualTo("OK")
    }
}
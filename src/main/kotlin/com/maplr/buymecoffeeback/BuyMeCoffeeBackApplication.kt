package com.maplr.buymecoffeeback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
@ConfigurationPropertiesScan
class BuyMeCoffeeBackApplication

fun main(args: Array<String>) {
    runApplication<BuyMeCoffeeBackApplication>(*args)
}

package com.maplr.buymecoffeeback.coffee

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.Tailable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Controller
class CoffeeController(private val coffeeService: CoffeeService) {
    @MessageMapping("coffee.send")
    suspend fun buyCoffee(@Payload coffee: CoffeePurchaseDto) = coffeeService.buyCoffee(coffee)

    @MessageMapping("coffee.get-all")
    fun getAllCoffee() = coffeeService.getAllCoffee()
}

@Service
class CoffeeService(private val coffeeRepository: CoffeeRepository) {
    suspend fun buyCoffee(coffee: CoffeePurchaseDto) {
        coffeeRepository.save(coffee.fromDto())
    }

    fun getAllCoffee() = coffeeRepository.findWithTailableCursorBy().map { it.toDto() }
}

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class CoffeePurchaseDto(@ProtoNumber(1) val name: String, @ProtoNumber(2) val price: Double)

@Document
data class CoffeePurchaseObj(@Id val id: String? = null, val name: String, val price: Double)

fun CoffeePurchaseDto.fromDto() = CoffeePurchaseObj(name = name, price = price)
fun CoffeePurchaseObj.toDto() = CoffeePurchaseDto(name = name, price = price)

@Repository
interface CoffeeRepository : CoroutineCrudRepository<CoffeePurchaseObj, String> {
    @Tailable
    fun findWithTailableCursorBy(): Flow<CoffeePurchaseObj>
}

package com.maplr.buymecoffeeback.config

import io.rsocket.core.Resume
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.boot.rsocket.server.RSocketServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufDecoder
import org.springframework.http.codec.protobuf.KotlinSerializationProtobufEncoder
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor
import java.time.Duration

private const val DEFAULT_AUTHORITY_PREFIX = "SCOPE_"

@Configuration
class RSocketSecurityConfiguration(
    private val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties,
) {

    @Bean
    fun rSocketResume(): RSocketServerCustomizer {
        return RSocketServerCustomizer { rSocketServer ->
            rSocketServer.resume(
                Resume().sessionDuration(
                    Duration.ofMinutes(
                        60,
                    ),
                ),
            )
        }
    }

    @Bean
    fun rSocketStrategies(): RSocketStrategies {
        return RSocketStrategies.builder().encoders {
            it.add(KotlinSerializationProtobufEncoder())
        }.decoders {
            it.add(KotlinSerializationProtobufDecoder())
        }.build()
    }

    @Bean
    fun messageHandler(strategies: RSocketStrategies): RSocketMessageHandler =
        RSocketMessageHandler().apply {
            argumentResolverConfigurer.addCustomResolver(AuthenticationPrincipalArgumentResolver())
            rSocketStrategies = strategies
        }

    @Bean
    fun rSocketInterceptor(rSocket: RSocketSecurity): PayloadSocketAcceptorInterceptor? =
        rSocket
            .authorizePayload {
                it.anyRequest()
                    .permitAll()
                    .anyExchange()
                    .permitAll()
            }
            .build()
}

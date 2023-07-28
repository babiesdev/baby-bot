package io.babydevelopers.babybot.application.spring.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.babydevelopers.babybot.application.spring.chatgpt.ChatGptClient
import io.babydevelopers.babybot.application.spring.github.gateway.GithubClient
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.util.MimeType
import org.springframework.web.reactive.function.client.*
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.util.retry.Retry
import java.nio.charset.StandardCharsets
import java.time.Duration

@Configuration
class WebConfig {
    @Bean
    fun objectMapper() = ObjectMapper().registerModule(
        KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .configure(KotlinFeature.StrictNullChecks, false)
            .build(),
    ).setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy())

    @Bean
    fun exchangeStrategies(objectMapper: ObjectMapper) =
        ExchangeStrategies.builder().codecs { clientCodecConfigurer ->
            clientCodecConfigurer.customCodecs().register(
                Jackson2JsonDecoder(
                    objectMapper,
                    MimeType("text", "javascript", StandardCharsets.UTF_8),
                ),
            )
        }.build()

    @Bean
    fun webClient(
        exchangeStrategies: ExchangeStrategies,
    ) = WebClient
        .builder()
        .clientConnector(connector())
        .exchangeStrategies(exchangeStrategies)
        .filter(RetryFilter())
        .build()

    @Bean
    fun gptClient(webClient: WebClient): ChatGptClient {
        val factory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build()

        return factory.createClient(ChatGptClient::class.java)
    }

    @Bean
    fun githubClient(webClient: WebClient): GithubClient {
        val factory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(webClient))
            .build()

        return factory.createClient(GithubClient::class.java)
    }

    private fun connector() = ReactorClientHttpConnector(
        HttpClient.create(ConnectionProvider.newConnection()),
    )
}

class RetryFilter : ExchangeFilterFunction {
    override fun filter(request: ClientRequest, next: ExchangeFunction) =
        next.exchange(request)
            .retryWhen(
                Retry.fixedDelay(3, Duration.ofSeconds(60 * 2))
                    .doAfterRetry { log.warn { "Retrying" } },
            )
            .doOnError(Throwable::printStackTrace)

    companion object {
        private val log = LoggerFactory.getLogger(RetryFilter::class.java)
    }
}

package my.okhouse

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class Handler {
    @Bean
    fun uppercase(): (String) -> String {
        return { e -> e.uppercase()}
    }
}
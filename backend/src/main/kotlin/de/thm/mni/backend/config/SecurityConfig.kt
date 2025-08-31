package de.thm.mni.backend.config

import de.thm.mni.backend.auth.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Konfigurationsklasse für die Anwendungssicherheit unter Verwendung von Spring Security.
 *
 * Diese Klasse definiert die Sicherheitsrichtlinien, wie z.B. CORS-Konfiguration,
 * CSRF-Schutz und die Autorisierungsregeln für verschiedene API-Endpunkte.
 *
 * @param jwtAuthenticationFilter Ein benutzerdefinierter Filter zur Validierung von JWT-Tokens.
 */
@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    /**
     * Definiert die [SecurityFilterChain], die die gesamte HTTP-Sicherheit konfiguriert.
     *
     * @param http Die [HttpSecurity]-Instanz, die zum Aufbau der Filterkette verwendet wird.
     * @return Die konfigurierte [SecurityFilterChain].
     * @throws Exception Wenn bei der Konfiguration ein Fehler auftritt.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // CORS-Konfiguration aktivieren
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            // CSRF-Schutz deaktivieren, da wir zustandslose JWTs verwenden
            .csrf { it.disable() }
            // Standard-Formular-Login deaktivieren
            .formLogin { it.disable() }
            // Den JWT-Filter vor dem Standard-Authentifizierungsfilter hinzufügen
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            // Autorisierungsregeln für HTTP-Anfragen festlegen
            .authorizeHttpRequests {
                // OPTIONS-Anfragen für CORS Pre-Flight erlauben
                it.requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                // Authentifizierungs-API-Endpunkte (z.B. Login) öffentlich machen
                it.requestMatchers("/api/auth/**").permitAll()
                // POST-Anfragen zur Benutzerregistrierung erlauben
                it.requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                // Alle anderen /api/users-Endpunkte erfordern Authentifizierung
                it.requestMatchers("/api/users/**").authenticated()
                // GraphQL-Endpunkt erfordert Authentifizierung
                it.requestMatchers("/graphql").authenticated()
                // Alle verbleibenden Anfragen erfordern Authentifizierung
                it.anyRequest().authenticated()
            }
        return http.build()
    }

    /**
     * Erstellt eine CORS-Konfigurationsquelle.
     *
     * Diese Bean legt fest, welche Ursprünge, Methoden und Header für
     * Cross-Origin-Anfragen erlaubt sind.
     *
     * @return Die konfigurierte [CorsConfigurationSource].
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val cfg = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:4200") // Erlaube Anfragen von Angular-Entwicklungsserver
            allowedMethods = listOf("*") // Alle HTTP-Methoden erlauben
            allowedHeaders = listOf("*") // Alle Header erlauben
            allowCredentials = true // Erlaube die Übertragung von Cookies und Authentifizierungs-Headern
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", cfg)
        return source
    }
}

package de.thm.mni.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Die Haupt-Anwendungsklasse für das Backend.
 *
 * Diese Klasse ist der Startpunkt der Anwendung. Die `@SpringBootApplication`-Annotation
 * kombiniert `@Configuration`, `@EnableAutoConfiguration` und `@ComponentScan`
 * und ermöglicht es Spring Boot, die Anwendung automatisch zu konfigurieren und zu starten.
 */
@SpringBootApplication
class BackendApplication

/**
 * Die Hauptfunktion, die die Spring Boot-Anwendung startet.
 *
 * @param args Die Befehlszeilenargumente, die an die Anwendung übergeben werden.
 */
fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}

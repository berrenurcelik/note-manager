package de.thm.mni.backend.config

import de.thm.mni.backend.note.Note
import de.thm.mni.backend.note.repository.NoteRepository
import de.thm.mni.backend.notebook.Notebook
import de.thm.mni.backend.notebook.repository.NotebookRepository
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.Date

@Component
class DataLoader(
    private val userRepository: UserRepository,
    private val notebookRepository: NotebookRepository,
    private val noteRepository: NoteRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (userRepository.count() > 0) {
            return
        }

        val user1 = User(
            username = "admin",
            firstName = "Admin",
            lastName = "User",
            email = "admin@example.com",
            password = "123"
        )

        val user2 = User(
            username = "john.doe",
            firstName = "John",
            lastName = "D",
            email = "john.d@example.com",
            password = "123"
        )

        val user3 = User(
            username = "jane.smith",
            firstName = "Jane",
            lastName = "S",
            email = "jane.s@example.com",
            password = "123"
        )

        userRepository.saveAll(listOf(user1, user2, user3))

        val nb1User1 = Notebook(title = "Projektideen", createdAt = Date(), user_id = user1.username)
        val nb2User1 = Notebook(title = "Rezeptesammlung", createdAt = Date(), user_id = user1.username)
        val nb3User1 = Notebook(title = "Reiseplanung 2025", createdAt = Date(), user_id = user1.username)

        val nb1User2 = Notebook(title = "Work Notes", createdAt = Date(), user_id = user2.username)
        val nb2User2 = Notebook(title = "Hobby Projekte", createdAt = Date(), user_id = user2.username)
        val nb3User2 = Notebook(title = "Fitnessplan", createdAt = Date(), user_id = user2.username)

        val nb1User3 = Notebook(title = "Meeting Notizen", createdAt = Date(), user_id = user3.username)
        val nb2User3 = Notebook(title = "Rezeptideen", createdAt = Date(), user_id = user3.username)
        val nb3User3 = Notebook(title = "Urlaubsplanung", createdAt = Date(), user_id = user3.username)

        val notebooks = listOf(
            nb1User1, nb2User1, nb3User1,
            nb1User2, nb2User2, nb3User2,
            nb1User3, nb2User3, nb3User3
        )
        notebookRepository.saveAll(notebooks)

        val notes = mutableListOf<Note>()

        notes.addAll(listOf(
            Note(title = "Neue App-Idee", content = "Eine App, die Pflanzenarten erkennt, basierend auf Fotos.", user_id = user1.username, notebook = nb1User1),
            Note(title = "Feature Brainstorming", content = "Liste aller gewünschten Features für das Pflanzen-Scanner-Projekt.", user_id = user1.username, notebook = nb1User1),
            Note(title = "Technologie-Stack", content = "Überlegen, ob wir Flutter oder React Native verwenden wollen.", user_id = user1.username, notebook = nb1User1)
        ))

        notes.addAll(listOf(
            Note(title = "Lasagne", content = "Familienrezept mit hausgemachter Béchamelsauce und frischer Pasta.", user_id = user1.username, notebook = nb2User1),
            Note(title = "Kürbissuppe", content = "Herbstliche Suppe mit Muskat, Sahne und Kürbis.", user_id = user1.username, notebook = nb2User1),
            Note(title = "Pesto selber machen", content = "Basilikum, Pinienkerne, Parmesan und Olivenöl mixen.", user_id = user1.username, notebook = nb2User1)
        ))

        notes.addAll(listOf(
            Note(title = "Japan", content = "Geplante Reiseroute: Tokio → Kyoto → Osaka. Unterkünfte recherchieren.", user_id = user1.username, notebook = nb3User1),
            Note(title = "Packliste", content = "Reisepass, Kamera, Ladekabel, bequeme Schuhe.", user_id = user1.username, notebook = nb3User1),
            Note(title = "Budget", content = "Flüge, Hotels, Essen und Sightseeing kalkulieren.", user_id = user1.username, notebook = nb3User1)
        ))

        notes.addAll(listOf(
            Note(title = "Projekt X Planung", content = "Erforderliche Ressourcen, Deadlines und Verantwortliche definieren.", user_id = user2.username, notebook = nb1User2),
            Note(title = "Team Meeting Notizen", content = "Diskussion über aktuelle Herausforderungen und nächste Schritte.", user_id = user2.username, notebook = nb1User2),
            Note(title = "Feedbackrunde", content = "Feedback von Kunden zum Prototyp einholen.", user_id = user2.username, notebook = nb1User2)
        ))

        notes.addAll(listOf(
            Note(title = "Arduino Roboter bauen", content = "Materialliste, Schaltpläne und erste Tests dokumentieren.", user_id = user2.username, notebook = nb2User2),
            Note(title = "Modellbau Flugzeug", content = "Bauteile bestellen und Baufortschritt festhalten.", user_id = user2.username, notebook = nb2User2),
            Note(title = "3D Druck Projekte", content = "Ideen für neue Druckobjekte sammeln und priorisieren.", user_id = user2.username, notebook = nb2User2)
        ))

        notes.addAll(listOf(
            Note(title = "Trainingsplan Woche 1", content = "Montag: Cardio, Mittwoch: Krafttraining, Freitag: Yoga.", user_id = user2.username, notebook = nb3User2),
            Note(title = "Ernährungstipps", content = "Mehr Proteine, weniger Zucker, ausreichend Wasser trinken.", user_id = user2.username, notebook = nb3User2),
            Note(title = "Fortschrittsmessung", content = "Maße, Gewicht und persönliche Ziele regelmäßig dokumentieren.", user_id = user2.username, notebook = nb3User2)
        ))

        notes.addAll(listOf(
            Note(title = "Kundenmeeting 15.04.", content = "Anforderungen und Feedback vom Kunden aufnehmen.", user_id = user3.username, notebook = nb1User3),
            Note(title = "Team Retrospektive", content = "Was lief gut, was kann verbessert werden?", user_id = user3.username, notebook = nb1User3),
            Note(title = "Sprint Planung", content = "Aufgaben für den nächsten Sprint verteilen.", user_id = user3.username, notebook = nb1User3)
        ))

        notes.addAll(listOf(
            Note(title = "Vegane Brownies", content = "Rezept mit Avocado und Kichererbsen.", user_id = user3.username, notebook = nb2User3),
            Note(title = "Frühstücksideen", content = "Smoothies, Overnight Oats, Vollkornbrot-Variationen.", user_id = user3.username, notebook = nb2User3),
            Note(title = "Salatdressings", content = "Vinaigrette, Tahini-Dressing und Joghurtsaucen.", user_id = user3.username, notebook = nb2User3)
        ))

        notes.addAll(listOf(
            Note(title = "Italien Roadtrip", content = "Route: Rom → Florenz → Venedig. Hotels und Sehenswürdigkeiten.", user_id = user3.username, notebook = nb3User3),
            Note(title = "Packliste Strandurlaub", content = "Badeanzug, Sonnencreme, Bücher, Kopfhörer.", user_id = user3.username, notebook = nb3User3),
            Note(title = "Budgetplanung", content = "Flüge, Unterkunft, Verpflegung und Aktivitäten kalkulieren.", user_id = user3.username, notebook = nb3User3)
        ))

        noteRepository.saveAll(notes)
    }
}

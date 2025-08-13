package de.thm.mni.backend.config

import de.thm.mni.backend.note.model.Note
import de.thm.mni.backend.note.repository.NoteRepository
import de.thm.mni.backend.notebook.model.Notebook
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
        // Always load sample data for in-memory database
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

        val nb1User1 = Notebook(title = "Projektideen", createdAt = Date(), userId = user1.username)
        val nb2User1 = Notebook(title = "Rezeptesammlung", createdAt = Date(), userId = user1.username)
        val nb3User1 = Notebook(title = "Reiseplanung 2025", createdAt = Date(), userId = user1.username)

        val nb1User2 = Notebook(title = "Work Notes", createdAt = Date(), userId = user2.username)
        val nb2User2 = Notebook(title = "Hobby Projekte", createdAt = Date(), userId = user2.username)
        val nb3User2 = Notebook(title = "Fitnessplan", createdAt = Date(), userId = user2.username)

        val nb1User3 = Notebook(title = "Meeting Notizen", createdAt = Date(), userId = user3.username)
        val nb2User3 = Notebook(title = "Rezeptideen", createdAt = Date(), userId = user3.username)
        val nb3User3 = Notebook(title = "Urlaubsplanung", createdAt = Date(), userId = user3.username)

        val notebooks = listOf(
            nb1User1, nb2User1, nb3User1,
            nb1User2, nb2User2, nb3User2,
            nb1User3, nb2User3, nb3User3
        )
        notebookRepository.saveAll(notebooks)

        val notes = mutableListOf<Note>()

        notes.addAll(listOf(
            Note(title = "Neue App-Idee", content = "Eine App, die Pflanzenarten erkennt, basierend auf Fotos.", userId = user1.username, notebookId = nb1User1.id, notebook = nb1User1),
            Note(title = "Feature Brainstorming", content = "Liste aller gewünschten Features für das Pflanzen-Scanner-Projekt.", userId = user1.username, notebookId = nb1User1.id, notebook = nb1User1),
            Note(title = "Technologie-Stack", content = "Überlegen, ob wir Flutter oder React Native verwenden wollen.", userId = user1.username, notebookId = nb1User1.id, notebook = nb1User1)
        ))

        notes.addAll(listOf(
            Note(title = "Lasagne", content = "Familienrezept mit hausgemachter Béchamelsauce und frischer Pasta.", userId = user1.username, notebookId = nb2User1.id, notebook = nb2User1),
            Note(title = "Kürbissuppe", content = "Herbstliche Suppe mit Muskat, Sahne und Kürbis.", userId = user1.username, notebookId = nb2User1.id, notebook = nb2User1),
            Note(title = "Pesto selber machen", content = "Basilikum, Pinienkerne, Parmesan und Olivenöl mixen.", userId = user1.username, notebookId = nb2User1.id, notebook = nb2User1)
        ))

        notes.addAll(listOf(
            Note(title = "Japan", content = "Geplante Reiseroute: Tokio → Kyoto → Osaka. Unterkünfte recherchieren.", userId = user1.username, notebookId = nb3User1.id, notebook = nb3User1),
            Note(title = "Packliste", content = "Reisepass, Kamera, Ladekabel, bequeme Schuhe.", userId = user1.username, notebookId = nb3User1.id, notebook = nb3User1),
            Note(title = "Budget", content = "Flüge, Hotels, Essen und Sightseeing kalkulieren.", userId = user1.username, notebookId = nb3User1.id, notebook = nb3User1)
        ))

        notes.addAll(listOf(
            Note(title = "Projekt X Planung", content = "Erforderliche Ressourcen, Deadlines und Verantwortliche definieren.", userId = user2.username, notebookId = nb1User2.id, notebook = nb1User2),
            Note(title = "Team Meeting Notizen", content = "Diskussion über aktuelle Herausforderungen und nächste Schritte.", userId = user2.username, notebookId = nb1User2.id, notebook = nb1User2),
            Note(title = "Feedbackrunde", content = "Feedback von Kunden zum Prototyp einholen.", userId = user2.username, notebookId = nb1User2.id, notebook = nb1User2)
        ))

        notes.addAll(listOf(
            Note(title = "Arduino Roboter bauen", content = "Materialliste, Schaltpläne und erste Tests dokumentieren.", userId = user2.username, notebookId = nb2User2.id, notebook = nb2User2),
            Note(title = "Modellbau Flugzeug", content = "Bauteile bestellen und Baufortschritt festhalten.", userId = user2.username, notebookId = nb2User2.id, notebook = nb2User2),
            Note(title = "3D Druck Projekte", content = "Ideen für neue Druckobjekte sammeln und priorisieren.", userId = user2.username, notebookId = nb2User2.id, notebook = nb2User2)
        ))

        notes.addAll(listOf(
            Note(title = "Trainingsplan Woche 1", content = "Montag: Cardio, Mittwoch: Krafttraining, Freitag: Yoga.", userId = user2.username, notebookId = nb3User2.id, notebook = nb3User2),
            Note(title = "Ernährungstipps", content = "Mehr Proteine, weniger Zucker, ausreichend Wasser trinken.", userId = user2.username, notebookId = nb3User2.id, notebook = nb3User2),
            Note(title = "Fortschrittsmessung", content = "Maße, Gewicht und persönliche Ziele regelmäßig dokumentieren.", userId = user2.username, notebookId = nb3User2.id, notebook = nb3User2)
        ))

        notes.addAll(listOf(
            Note(title = "Kundenmeeting 15.04.", content = "Anforderungen und Feedback vom Kunden aufnehmen.", userId = user3.username, notebookId = nb1User3.id, notebook = nb1User3),
            Note(title = "Team Retrospektive", content = "Was lief gut, was kann verbessert werden?", userId = user3.username, notebookId = nb1User3.id, notebook = nb1User3),
            Note(title = "Sprint Planung", content = "Aufgaben für den nächsten Sprint verteilen.", userId = user3.username, notebookId = nb1User3.id, notebook = nb1User3)
        ))

        notes.addAll(listOf(
            Note(title = "Vegane Brownies", content = "Rezept mit Avocado und Kichererbsen.", userId = user3.username, notebookId = nb2User3.id, notebook = nb2User3),
            Note(title = "Frühstücksideen", content = "Smoothies, Overnight Oats, Vollkornbrot-Variationen.", userId = user3.username, notebookId = nb2User3.id, notebook = nb2User3),
            Note(title = "Salatdressings", content = "Vinaigrette, Tahini-Dressing und Joghurtsaucen.", userId = user3.username, notebookId = nb2User3.id, notebook = nb2User3)
        ))

        notes.addAll(listOf(
            Note(title = "Italien Roadtrip", content = "Route: Rom → Florenz → Venedig. Hotels und Sehenswürdigkeiten.", userId = user3.username, notebookId = nb3User3.id, notebook = nb3User3),
            Note(title = "Packliste Strandurlaub", content = "Badeanzug, Sonnencreme, Bücher, Kopfhörer.", userId = user3.username, notebookId = nb3User3.id, notebook = nb3User3),
            Note(title = "Budgetplanung", content = "Flüge, Unterkunft, Verpflegung und Aktivitäten kalkulieren.", userId = user3.username, notebookId = nb3User3.id, notebook = nb3User3)
        ))

        noteRepository.saveAll(notes)
    }
}

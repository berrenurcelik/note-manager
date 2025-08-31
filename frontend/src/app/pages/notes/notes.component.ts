import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { NoteService } from '../../services/note.service';
import { Note } from '../../models/note.model';
import { MatDialog } from '@angular/material/dialog';
import { NotebookService } from '../../services/notebook.service';
import { MatIconButton} from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { CreateNoteDialog } from '../shared/create-note-dialog/create-note-dialog';
import {MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';

/**
 * Der Component für die Notizen-Seite.
 * Dieser Component ist für das Anzeigen, Filtern und Verwalten von Notizen zuständig.
 * Er lädt Notizen entweder für ein spezifisches Notizbuch oder alle Notizen und ermöglicht
 * die Interaktion (Erstellen, Bearbeiten, Löschen) über einen Dialog.
 */
@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatIconButton, MatIcon, MatInput, MatFormField, MatLabel, MatSuffix],
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.css'],
})
export class NotesComponent implements OnInit {
  /**
   * Ein Array, das alle geladenen Notizen enthält.
   * @type {Note[]}
   */
  notes: Note[] = [];

  /**
   * Ein Array, das die Notizen nach dem Anwenden eines Suchfilters enthält.
   * @type {Note[]}
   */
  filteredNotes: Note[] = [];

  /**
   * Die Suchanfrage des Benutzers, um Notizen nach Titel zu filtern.
   * @type {string}
   */
  searchTitle = '';

  /**
   * Ein Flag, das den Ladestatus anzeigt. True, wenn Notizen geladen werden.
   * @type {boolean}
   */
  loading = false;

  /**
   * Eine Fehlermeldung, die bei einem fehlgeschlagenen Ladevorgang angezeigt wird.
   * @type {string}
   */
  error = '';

  /**
   * Die ID des aktuellen Notizbuchs, falls aus den Routenparametern vorhanden.
   * @type {string | null}
   */
  notebookId: string | null = null;

  /**
   * Der Titel des aktuellen Notizbuchs, standardmäßig 'Notizen'.
   * @type {string}
   */
  notebookTitle: string = 'Notizen';

  /**
   * Der Konstruktor, der die notwendigen Dienste injiziert.
   * @param {NoteService} noteService Der Dienst für die Notiz-Datenverwaltung.
   * @param {NotebookService} notebookService Der Dienst für die Notizbuch-Datenverwaltung.
   * @param {ActivatedRoute} route Der Dienst, um auf die Routenparameter zuzugreifen.
   * @param {MatDialog} dialog Der Dienst zum Öffnen von Dialogen.
   */
  constructor(
    private noteService: NoteService,
    private notebookService: NotebookService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  /**
   * Ein Angular-Lebenszyklus-Hook, der beim Initialisieren des Components aufgerufen wird.
   * Er abonniert die Routenparameter, um die Notizen basierend auf der Notizbuch-ID zu laden.
   */
  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.notebookId = params['notebookId'] || null;
      this.loadNotebookTitle();
      this.loadNotes();
    });
  }

  /**
   * Lädt den Titel des aktuellen Notizbuchs basierend auf der `notebookId`.
   * Setzt den Titel auf 'Notizen', falls keine ID vorhanden ist oder ein Fehler auftritt.
   * @private
   */
  private loadNotebookTitle() {
    if (!this.notebookId) {
      this.notebookTitle = 'Notizen';
      return;
    }

    this.notebookService.getNotebookById(this.notebookId).subscribe({
      next: (notebook) => (this.notebookTitle = notebook?.title || 'Notizen'),
      error: (err) => {
        console.error('Fehler beim Laden des Notizbuchs:', err);
        this.notebookTitle = 'Notizen';
      },
    });
  }

  /**
   * Lädt alle Notizen vom Server.
   * Wenn `notebookId` vorhanden ist, werden nur Notizen für dieses Notizbuch geladen.
   * @private
   */
  private loadNotes() {
    this.loading = true;
    this.noteService.getAllNotes(this.notebookId || undefined).subscribe({
      next: (notes) => {
        this.notes = notes;
        this.filteredNotes = notes;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Fehler beim Laden der Notizen';
        this.loading = false;
        console.error('Error loading notes:', err);
      },
    });
  }

  /**
   * Filtert die Notizen basierend auf dem `searchTitle`.
   * Die `filteredNotes` werden aktualisiert, um nur die Notizen anzuzeigen,
   * deren Titel die Suchanfrage enthält.
   */
  searchNotes() {
    if (this.searchTitle.trim()) {
      this.filteredNotes = this.notes.filter((note) =>
        note.title.toLowerCase().includes(this.searchTitle.toLowerCase())
      );
    } else {
      this.filteredNotes = this.notes;
    }
  }

  /**
   * Öffnet den Dialog zum Erstellen oder Bearbeiten einer Notiz.
   * Behandelt das Ergebnis des Dialogs, um die lokale Liste der Notizen zu aktualisieren.
   * @param {Note} [note] Das optionale Notiz-Objekt, das bearbeitet werden soll.
   */
  openNoteDialog(note?: Note) {
    const dialogRef = this.dialog.open(CreateNoteDialog, {
      width: '400px',
      data: note,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!result) return;

      if (result.deleted) {
        this.handleDelete(result.id);
      } else if (note) {
        this.handleUpdate(note.id, result);
      } else {
        this.handleCreate(result);
      }
    });
  }

  /**
   * Entfernt eine Notiz aus der lokalen `notes`-Liste und aktualisiert die `filteredNotes`.
   * @private
   * @param {string} id Die ID der zu löschenden Notiz.
   */
  private handleDelete(id: string) {
    this.notes = this.notes.filter((n) => n.id !== id);
    this.filteredNotes = this.notes;
  }

  /**
   * Aktualisiert eine Notiz in der lokalen Liste.
   * Die Notiz wurde bereits im Dialog aktualisiert und gespeichert.
   * @private
   * @param {string} id Die ID der aktualisierten Notiz.
   * @param {Note} updatedNote Die bereits aktualisierte Notiz aus dem Dialog.
   */
  private handleUpdate(id: string, updatedNote: Note) {
    const index = this.notes.findIndex((n) => n.id === id);
    if (index > -1) {
      this.notes[index] = updatedNote;
      this.filteredNotes = this.notes;
    }
  }

  /**
   * Erstellt eine neue Notiz, fügt sie zur lokalen Liste hinzu.
   * Die Notiz wurde bereits im Dialog erstellt und gespeichert.
   * @private
   * @param {Note} createdNote Die bereits erstellte Notiz aus dem Dialog.
   */
  private handleCreate(createdNote: Note) {
    this.notes.push(createdNote);
    this.filteredNotes = this.notes;
  }
}

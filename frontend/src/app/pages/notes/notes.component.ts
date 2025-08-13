import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { NoteService } from '../../services/note.service';
import { Note } from '../../models/note.model';

@Component({
  selector: 'app-notes.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css',
})
export class NotesComponent implements OnInit {
  notes: Note[] = [];
  filteredNotes: Note[] = [];
  newNoteTitle = '';
  newNoteContent = '';
  searchTitle = '';
  loading = false;
  error = '';
  notebookId: string | null = null;

  constructor(
    private noteService: NoteService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.notebookId = params['notebookId'] || null;
      this.loadNotes();
    });
  }

  loadNotes() {
    this.loading = true;
    this.noteService.getAllNotes(this.notebookId || undefined).subscribe({
      next: (notes) => {
        this.notes = notes;
        this.filteredNotes = notes;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Fehler beim Laden der Notizen';
        this.loading = false;
        console.error('Error loading notes:', error);
      },
    });
  }

  searchNotes() {
    if (this.searchTitle.trim()) {
      this.filteredNotes = this.notes.filter((note) =>
        note.title.toLowerCase().includes(this.searchTitle.toLowerCase())
      );
    } else {
      this.filteredNotes = this.notes;
    }
  }

  createNote() {
    if (!this.newNoteTitle.trim()) return;

    this.noteService
      .createNote({
        title: this.newNoteTitle,
        content: this.newNoteContent,
        notebookId: this.notebookId,
      })
      .subscribe({
        next: (note) => {
          this.notes.push(note);
          this.filteredNotes = this.notes;
          this.newNoteTitle = '';
          this.newNoteContent = '';
        },
        error: (error) => {
          this.error = 'Fehler beim Erstellen der Notiz';
          console.error('Error creating note:', error);
        },
      });
  }

  deleteNote(id: string) {
    if (!confirm('Sind Sie sicher, dass Sie diese Notiz löschen möchten?'))
      return;

    this.noteService.deleteNote(id).subscribe({
      next: () => {
        this.notes = this.notes.filter((note) => note.id !== id);
        this.filteredNotes = this.filteredNotes.filter(
          (note) => note.id !== id
        );
      },
      error: (error) => {
        this.error = 'Fehler beim Löschen der Notiz';
        console.error('Error deleting note:', error);
      },
    });
  }
}

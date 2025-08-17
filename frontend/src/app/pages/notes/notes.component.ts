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

@Component({
  selector: 'app-notes',
  imports: [CommonModule, FormsModule, RouterModule, MatIconButton, MatIcon, MatInput, MatFormField, MatLabel, MatSuffix],
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.css'],
})
export class NotesComponent implements OnInit {
  notes: Note[] = [];
  filteredNotes: Note[] = [];
  searchTitle = '';
  loading = false;
  error = '';
  notebookId: string | null = null;
  notebookTitle: string = 'Notizen';

  constructor(
    private noteService: NoteService,
    private notebookService: NotebookService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.notebookId = params['notebookId'] || null;
      this.loadNotebookTitle();
      this.loadNotes();
    });
  }

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

  searchNotes() {
    if (this.searchTitle.trim()) {
      this.filteredNotes = this.notes.filter((note) =>
        note.title.toLowerCase().includes(this.searchTitle.toLowerCase())
      );
    } else {
      this.filteredNotes = this.notes;
    }
  }

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

  private handleDelete(id: string) {
    this.notes = this.notes.filter((n) => n.id !== id);
    this.filteredNotes = this.notes;
  }

  private handleUpdate(id: string, updatedData: { title: string; content: string }) {
    this.noteService
      .updateNote(id, { ...updatedData, notebookId: this.notebookId! })
      .subscribe({
        next: (updatedNote) => {
          const index = this.notes.findIndex((n) => n.id === updatedNote.id);
          if (index > -1) this.notes[index] = updatedNote;
          this.filteredNotes = this.notes;
        },
        error: (err) => {
          this.error = 'Fehler beim Bearbeiten der Notiz';
          console.error(err);
        },
      });
  }

  private handleCreate(newData: { title: string; content: string }) {
    this.noteService
      .createNote({ ...newData, notebookId: this.notebookId! })
      .subscribe({
        next: (createdNote) => {
          this.notes.push(createdNote);
          this.filteredNotes = this.notes;
        },
        error: (err) => {
          this.error = 'Fehler beim Erstellen der Notiz';
          console.error(err);
        },
      });
  }
}

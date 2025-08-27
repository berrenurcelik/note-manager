import { Component, Inject } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormField, MatLabel, MatHint } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { NoteService } from '../../../services/note.service';
import { Note } from '../../../models/note.model';
import { ConfirmDialogComponent } from '../dialog/dialog';

@Component({
  selector: 'app-create-note-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormField,
    MatLabel,
    MatDialogContent,
    MatDialogActions,
    MatInput,
    MatButton,
    MatDialogTitle,
    MatIconButton,
    MatIcon,
    MatHint
  ],
  templateUrl: './create-note-dialog.html',
  styleUrls: ['./create-note-dialog.css']
})
export class CreateNoteDialog {
  title: string = '';
  content: string = '';
  error: string = '';
  notebookId: string | undefined;

  constructor(
    private dialogRef: MatDialogRef<CreateNoteDialog>,
    private noteService: NoteService,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data?: Note
  ) {
    if (data) {
      this.title = data.title;
      this.content = data.content;
      this.notebookId = data.notebookId;
    }
  }

  ngOnInit() {
    if (this.data) {
      this.title = this.data.title;
      this.content = this.data.content;
    }
  }

  save() {
    if (!this.title.trim()) return;

    const noteData: { title: string; content: string; notebookId: string | null } = {
      title: this.title,
      content: this.content,
      notebookId: this.notebookId || null
    };

    if (this.data && this.data.id) {
      this.noteService.updateNote(this.data.id, noteData).subscribe({
        next: updatedNote => this.dialogRef.close(updatedNote),
        error: error => {
          this.error = 'Fehler beim Bearbeiten der Notiz';
          console.error(error);
        }
      });
    } else {
      this.noteService.createNote(noteData).subscribe({
        next: createdNote => this.dialogRef.close(createdNote),
        error: error => {
          this.error = 'Fehler beim Erstellen der Notiz';
          console.error(error);
        }
      });
    }
  }

  deleteNote() {
    if (!this.data?.id) return;

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Notiz löschen',
        message: 'Möchten Sie diese Notiz wirklich löschen?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.noteService.deleteNote(this.data!.id).subscribe({
          next: () => this.dialogRef.close({ deleted: true, id: this.data!.id }),
          error: err => {
            this.error = 'Fehler beim Löschen der Notiz';
            console.error(err);
          }
        });
      }
    });
  }

  close() {
    this.dialogRef.close();
  }
}

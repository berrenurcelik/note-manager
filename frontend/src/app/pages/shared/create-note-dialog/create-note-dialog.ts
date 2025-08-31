import { Component, Inject, OnInit } from '@angular/core';
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

/**
 * Dialog-Komponente zum Erstellen, Bearbeiten und Löschen einer Notiz.
 * Diese Komponente dient als modaler Dialog, um Notizdaten einzugeben und über den `NoteService` mit der API zu interagieren.
 * Sie unterstützt die Bearbeitung einer bestehenden Notiz (über `MAT_DIALOG_DATA`) oder die Erstellung einer neuen Notiz.
 * Zusätzlich bietet sie eine Löschfunktion, die einen Bestätigungsdialog öffnet.
 */
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
export class CreateNoteDialog implements OnInit {
  /**
   * Der Titel der Notiz, der vom Benutzer eingegeben wird.
   * @type {string}
   */
  title: string = '';

  /**
   * Der Inhalt der Notiz, der vom Benutzer eingegeben wird.
   * @type {string}
   */
  content: string = '';

  /**
   * Eine Fehlermeldung, die bei einem fehlgeschlagenen API-Aufruf angezeigt wird.
   * @type {string}
   */
  error: string = '';

  /**
   * Die ID des Notizbuchs, zu dem die Notiz gehört.
   * @type {string | undefined}
   */
  notebookId: string | undefined;

  /**
   * Der Konstruktor, der die notwendigen Dienste injiziert und die Dialogdaten initialisiert.
   * @param {MatDialogRef<CreateNoteDialog>} dialogRef Eine Referenz auf den geöffneten Dialog.
   * @param {NoteService} noteService Der Service zur Interaktion mit Notizdaten.
   * @param {MatDialog} dialog Der Service zum Öffnen von Dialogen (für den Bestätigungsdialog).
   * @param {Note} [data] Optionales Notizobjekt, das übergeben wird, wenn eine Notiz bearbeitet wird.
   */
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

  /**
   * Ein Angular-Lebenszyklus-Hook, der beim Initialisieren des Components aufgerufen wird.
   * Er initialisiert die Eingabefelder, falls Daten für eine bestehende Notiz vorhanden sind.
   */
  ngOnInit() {
    if (this.data) {
      this.title = this.data.title;
      this.content = this.data.content;
    }
  }

  /**
   * Speichert eine Notiz (entweder neu erstellen oder aktualisieren).
   * Schließt den Dialog mit den gespeicherten Daten.
   * Zeigt einen Fehler an, falls der Speichervorgang fehlschlägt.
   * @returns {void}
   */
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

  /**
   * Löscht eine Notiz, nachdem der Benutzer den Löschvorgang in einem Bestätigungsdialog bestätigt hat.
   * Schließt den Dialog nach erfolgreichem Löschen mit den entsprechenden Daten.
   * Zeigt einen Fehler an, falls der Löschvorgang fehlschlägt.
   * @returns {void}
   */
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

  /**
   * Schließt den Dialog, ohne Änderungen zu speichern oder eine Aktion auszuführen.
   * @returns {void}
   */
  close() {
    this.dialogRef.close();
  }
}

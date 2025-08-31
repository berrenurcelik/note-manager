import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { NotebookService } from '../../../services/notebook.service';
import { CommonModule } from '@angular/common';

import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import {MatFormField, MatHint, MatLabel} from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import {MatButton, MatIconButton} from '@angular/material/button';
import { Notebook } from '../../../models/notebook.model';
import { ConfirmDialogComponent } from '../dialog/dialog';
import {MatIcon} from '@angular/material/icon';

/**
 * @fileoverview Dialog-Komponente zum Erstellen, Bearbeiten und Löschen eines Notizbuchs.
 * @description Diese Komponente dient als modaler Dialog, um Notizbuchdaten einzugeben und über den `NotebookService` mit der API zu interagieren.
 * Sie unterstützt die Bearbeitung eines bestehenden Notizbuchs (über `MAT_DIALOG_DATA`) oder die Erstellung eines neuen.
 * Zusätzlich bietet sie eine Löschfunktion, die einen Bestätigungsdialog öffnet.
 */
@Component({
  selector: 'app-create-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
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
    MatHint,
  ],
  templateUrl: './create-notebook-dialog.html',
  styleUrls: ['./create-notebook-dialog.css']
})
export class CreateNotebookDialog implements OnInit {
  /**
   * Der Titel des Notizbuchs, der vom Benutzer eingegeben wird.
   * @type {string}
   */
  newNotebookTitle: string = "";

  /**
   * Eine Fehlermeldung, die bei einem fehlgeschlagenen API-Aufruf angezeigt wird.
   * @type {string}
   */
  error = '';

  /**
   * Ein Array von Bildpfaden für die Notizbuch-Cover.
   * @type {string[]}
   */
  image = [
    'assets/cover1.png','assets/cover2.png','assets/cover3.png','assets/cover4.png',
    'assets/cover5.png','assets/cover6.png','assets/cover7.png','assets/cover8.png',
    'assets/cover9.png','assets/cover10.png'
  ];

  /**
   * Der Pfad zum aktuell ausgewählten Notizbuch-Cover.
   * @type {string}
   */
  selectedImage: string = this.image[0]; // default Cover

  /**
   * Ein Flag, das die Sichtbarkeit des Bildauswahl-Tools steuert.
   * @type {boolean}
   */
  showImagePicker: boolean = false;

  /**
   * Der Konstruktor, der die notwendigen Dienste injiziert und die Dialogdaten initialisiert.
   * @param {NotebookService} notebookService Der Dienst zur Interaktion mit Notizbuchdaten.
   * @param {MatDialogRef<CreateNotebookDialog>} dialogRef Eine Referenz auf den geöffneten Dialog.
   * @param {MatDialog} dialog Der Dienst zum Öffnen von Dialogen (für den Bestätigungsdialog).
   * @param {Notebook} [data] Optionales Notizbuchobjekt, das übergeben wird, wenn ein Notizbuch bearbeitet wird.
   */
  constructor(
    protected notebookService: NotebookService,
    private dialogRef: MatDialogRef<CreateNotebookDialog>,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data?: Notebook
  ) {}

  /**
   * Ein Angular-Lebenszyklus-Hook, der beim Initialisieren des Components aufgerufen wird.
   * Er initialisiert die Eingabefelder, falls Daten für ein bestehendes Notizbuch vorhanden sind.
   */
  ngOnInit() {
    if (this.data) {
      this.newNotebookTitle = this.data.title;
      this.selectedImage = this.data.image;
    }
  }

  /**
   * Schaltet die Sichtbarkeit des Bildauswahl-Tools um.
   * @returns {void}
   */
  toggleImagePicker() {
    this.showImagePicker = !this.showImagePicker;
  }

  /**
   * Wählt ein Bild als Cover aus und schließt das Bildauswahl-Tool.
   * @param {string} img Der Pfad zum ausgewählten Bild.
   * @returns {void}
   */
  selectImage(img: string) {
    this.selectedImage = img;
    this.showImagePicker = false;
  }

  /**
   * Speichert ein Notizbuch (entweder neu erstellen oder aktualisieren).
   * Schließt den Dialog mit den gespeicherten Daten.
   * Zeigt einen Fehler an, falls der Speichervorgang fehlschlägt.
   * @returns {void}
   */
  saveNotebook() {
    if (!this.newNotebookTitle.trim() || !this.selectedImage) return;

    const notebookData: { title: string; image: string } = {
      title: this.newNotebookTitle,
      image: this.selectedImage
    };

    if (this.data && this.data.id) {
      this.notebookService.updateNotebook(this.data.id, notebookData).subscribe({
        next: (updatedNotebook) => this.dialogRef.close(updatedNotebook),
        error: (error) => {
          this.error = 'Fehler beim Bearbeiten des Notizbuchs';
          console.error(error);
        }
      });
    } else {
      this.notebookService.createNotebook(notebookData).subscribe({
        next: (createdNotebook) => this.dialogRef.close(createdNotebook),
        error: (error) => {
          this.error = 'Fehler beim Erstellen des Notizbuchs';
          console.error(error);
        }
      });
    }
  }

  /**
   * Löscht ein Notizbuch, nachdem der Benutzer den Löschvorgang in einem Bestätigungsdialog bestätigt hat.
   * Schließt den Dialog nach erfolgreichem Löschen mit den entsprechenden Daten.
   * Zeigt einen Fehler an, falls der Löschvorgang fehlschlägt.
   * @returns {void}
   */
  deleteNotebook() {
    if (!this.data || !this.data.id) return;

    const notebookId = this.data.id;

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Notizbuch löschen',
        message: 'Möchten Sie dieses Notizbuch wirklich löschen?'
      }
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.notebookService.deleteNotebook(notebookId).subscribe({
          next: () => {
            this.dialogRef.close({ deleted: true, id: notebookId });
          },
          error: (err) => {
            this.error = 'Fehler beim Löschen des Notizbuchs';
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
  close(): void {
    this.dialogRef.close();
  }
}

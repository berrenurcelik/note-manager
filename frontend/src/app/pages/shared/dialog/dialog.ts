import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

/**
 * @fileoverview Dialog-Komponente für die Bestätigung von Aktionen.
 * @description Diese Komponente dient als generischer Bestätigungsdialog mit einem Titel und einer Nachricht.
 * Sie gibt `true` zurück, wenn der Benutzer bestätigt, und `false`, wenn er abbricht.
 */
@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton
  ],
  templateUrl: '/dialog.html',
  styleUrls: ['./dialog.css']
})
export class ConfirmDialogComponent {
  /**
   * Der Konstruktor, der die Referenz auf den Dialog und die Daten injiziert.
   * @param {MatDialogRef<ConfirmDialogComponent>} dialogRef Eine Referenz auf den geöffneten Dialog.
   * @param {{ title: string; message: string }} data Die Daten, die Titel und Nachricht enthalten.
   */
  constructor(
    private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string; message: string }
  ) {}

  /**
   * Schließt den Dialog und gibt `true` zurück, um die Aktion zu bestätigen.
   * @returns {void}
   */
  onConfirm() {
    this.dialogRef.close(true);
  }

  /**
   * Schließt den Dialog und gibt `false` zurück, um die Aktion abzubrechen.
   * @returns {void}
   */
  onCancel() {
    this.dialogRef.close(false);
  }
}

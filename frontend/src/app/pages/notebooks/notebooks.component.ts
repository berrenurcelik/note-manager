import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NotebookService } from '../../services/notebook.service';
import { Notebook } from '../../models/notebook.model';
import { MatDialog } from '@angular/material/dialog';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { CreateNotebookDialog } from '../shared/create-notebook-dialog/create-notebook-dialog';
import { MatCard } from '@angular/material/card';

/**
 * Der Haupt-Component für die Notizbuch-Verwaltung.
 * Dieser Component ist verantwortlich für das Laden, Anzeigen und Verwalten der Notizbücher des Benutzers.
 * Er ermöglicht das Hinzufügen, Bearbeiten und Löschen von Notizbüchern über einen Dialog.
 */
@Component({
  selector: 'app-notebooks',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatIcon, MatIconButton, MatCard],
  templateUrl: './notebooks.component.html',
  styleUrls: ['./notebooks.component.css'],
})
export class NotebooksComponent implements OnInit {
  /**
   * Ein Array, das alle Notizbuch-Objekte enthält.
   * @type {Notebook[]}
   */
  notebooks: Notebook[] = [];

  /**
   * Ein Flag, das den Ladestatus anzeigt. True, wenn Notizbücher geladen werden.
   * @type {boolean}
   */
  loading = false;

  /**
   * Eine Fehlermeldung, die bei einem fehlgeschlagenen Ladevorgang angezeigt wird.
   * @type {string}
   */
  error = '';

  /**
   * Eine Eingabeeigenschaft, die den Bildpfad für ein Notizbuch festlegt.
   * @type {string}
   * @deprecated Diese Eigenschaft scheint ungenutzt zu sein.
   */
  @Input() image = '';

  /**
   * Der Konstruktor, der die notwendigen Dienste injiziert.
   * @param {NotebookService} notebookService Der Dienst für die Notizbuch-Datenverwaltung.
   * @param {MatDialog} dialog Der Dienst zum Öffnen von Dialogen.
   */
  constructor(private notebookService: NotebookService, private dialog: MatDialog) {}

  /**
   * Ein Angular-Lebenszyklus-Hook, der beim Initialisieren des Components aufgerufen wird.
   * Er startet das Laden der Notizbücher.
   */
  ngOnInit() {
    this.loadNotebooks();
  }

  /**
   * Lädt alle Notizbücher vom Server.
   * Aktualisiert die `notebooks`-Eigenschaft bei Erfolg oder setzt die `error`-Eigenschaft bei einem Fehler.
   * @private
   */
  private loadNotebooks() {
    this.loading = true;
    this.notebookService.getAllNotebooks().subscribe({
      next: (notebooks) => {
        this.notebooks = notebooks;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Fehler beim Laden der Notizbücher';
        this.loading = false;
        console.error(err);
      },
    });
  }

  /**
   * Öffnet den Dialog zum Erstellen oder Bearbeiten eines Notizbuchs.
   * Die Methode abonniert das `afterClosed()`-Ereignis des Dialogs, um die lokale Liste der Notizbücher zu aktualisieren.
   * @param {Notebook} [notebook] Das optionale Notizbuch-Objekt, das bearbeitet werden soll.
   */
  openNotebookDialog(notebook?: Notebook) {
    const dialogRef = this.dialog.open(CreateNotebookDialog, {
      width: '400px',
      data: notebook || null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!result) return;

      if (result.deleted) {
        this.handleDelete(result.id);
      } else if (notebook) {
        this.handleUpdate(result);
      } else {
        this.handleCreate(result);
      }
    });
  }

  /**
   * Entfernt ein Notizbuch aus der lokalen `notebooks`-Liste.
   * @private
   * @param {string} id Die ID des zu löschenden Notizbuchs.
   */
  private handleDelete(id: string) {
    this.notebooks = this.notebooks.filter((nb) => nb.id !== id);
  }

  /**
   * Aktualisiert ein Notizbuch in der lokalen `notebooks`-Liste.
   * @private
   * @param {Notebook} updatedNotebook Das aktualisierte Notizbuch-Objekt.
   */
  private handleUpdate(updatedNotebook: Notebook) {
    const index = this.notebooks.findIndex((nb) => nb.id === updatedNotebook.id);
    if (index > -1) this.notebooks[index] = updatedNotebook;
  }

  /**
   * Fügt ein neues Notizbuch zur lokalen `notebooks`-Liste hinzu.
   * @private
   * @param {Notebook} newNotebook Das neu erstellte Notizbuch-Objekt.
   */
  private handleCreate(newNotebook: Notebook) {
    this.notebooks.push(newNotebook);
  }
}

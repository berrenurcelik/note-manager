import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NotebookService } from '../../services/notebook.service';
import { Notebook } from '../../models/notebook.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/dialog';
import { MatIconButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { CreateDialog } from "../shared/create-dialog/create-dialog";
import { MatCard } from '@angular/material/card';

@Component({
  selector: 'app-notebooks',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatIcon,
    MatIconButton,
    MatCard
  ],
  templateUrl: './notebooks.component.html',
  styleUrl: './notebooks.component.css'
})
export class NotebooksComponent implements OnInit {
  notebooks: Notebook[] = [];
  loading = false;
  error = '';
  @Input() image = "";

  constructor(
    private notebookService: NotebookService,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.loadNotebooks();
  }

  loadNotebooks() {
    this.loading = true;
    this.notebookService.getAllNotebooks().subscribe({
      next: (notebooks) => {
        this.notebooks = notebooks;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Fehler beim Laden der Notizbücher';
        this.loading = false;
        console.error('Error loading notebooks:', error);
      }
    });
  }

  openCreateDialog(notebook?: Notebook) {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: notebook || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) return;

      if (result.deleted) {
        this.notebooks = this.notebooks.filter(nb => nb.id !== result.id);
      } else if (notebook) {
        // Bearbeitetes Notebook ersetzen
        const index = this.notebooks.findIndex(nb => nb.id === result.id);
        if (index > -1) this.notebooks[index] = result;
      } else {
        // Neues Notebook hinzufügen
        this.notebooks.push(result);
      }
    });
  }

  editNotebook(notebook: Notebook, event: Event): void {
    event.preventDefault();
    event.stopPropagation();

    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px',
      data: notebook
    });

    dialogRef.afterClosed().subscribe(updated => {
      if (updated) {
        const idx = this.notebooks.findIndex(n => n.id === updated.id);
        if (idx > -1) {
          this.notebooks[idx] = updated;
        }
        console.log('Notizbuch aktualisiert:', updated);
      }
    });
  }
}

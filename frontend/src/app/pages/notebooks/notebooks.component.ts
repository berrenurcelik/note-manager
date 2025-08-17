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

@Component({
  selector: 'app-notebooks',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, MatIcon, MatIconButton, MatCard],
  templateUrl: './notebooks.component.html',
  styleUrls: ['./notebooks.component.css'],
})
export class NotebooksComponent implements OnInit {
  notebooks: Notebook[] = [];
  loading = false;
  error = '';
  @Input() image = '';

  constructor(private notebookService: NotebookService, private dialog: MatDialog) {}

  ngOnInit() {
    this.loadNotebooks();
  }

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

  private handleDelete(id: string) {
    this.notebooks = this.notebooks.filter((nb) => nb.id !== id);
  }

  private handleUpdate(updatedNotebook: Notebook) {
    const index = this.notebooks.findIndex((nb) => nb.id === updatedNotebook.id);
    if (index > -1) this.notebooks[index] = updatedNotebook;
  }

  private handleCreate(newNotebook: Notebook) {
    this.notebooks.push(newNotebook);
  }
}

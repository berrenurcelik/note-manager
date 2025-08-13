import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NotebookService } from '../../services/notebook.service';
import { Notebook } from '../../models/notebook.model';

@Component({
  selector: 'app-notebooks.component',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './notebooks.component.html',
  styleUrl: './notebooks.component.css'
})
export class NotebooksComponent implements OnInit {
  notebooks: Notebook[] = [];
  newNotebookTitle = '';
  loading = false;
  error = '';

  constructor(private notebookService: NotebookService) {}

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

  createNotebook() {
    if (!this.newNotebookTitle.trim()) return;

    this.notebookService.createNotebook({ title: this.newNotebookTitle }).subscribe({
      next: (notebook) => {
        this.notebooks.push(notebook);
        this.newNotebookTitle = '';
      },
      error: (error) => {
        this.error = 'Fehler beim Erstellen des Notizbuchs';
        console.error('Error creating notebook:', error);
      }
    });
  }

  deleteNotebook(id: string) {
    if (!confirm('Sind Sie sicher, dass Sie dieses Notizbuch löschen möchten?')) return;

    this.notebookService.deleteNotebook(id).subscribe({
      next: () => {
        this.notebooks = this.notebooks.filter(nb => nb.id !== id);
      },
      error: (error) => {
        this.error = 'Fehler beim Löschen des Notizbuchs';
        console.error('Error deleting notebook:', error);
      }
    });
  }
}

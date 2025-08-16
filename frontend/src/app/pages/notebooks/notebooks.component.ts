import {Component, Input, OnInit} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NotebookService } from '../../services/notebook.service';
import { Notebook } from '../../models/notebook.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../shared/dialog/dialog';
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {CreateDialog} from "../shared/create-dialog/create-dialog"
import {
  MatCard
} from '@angular/material/card';

@Component({
  selector: 'app-notebooks.component',
  standalone: true, // hinzugefügt
  imports: [CommonModule, FormsModule, RouterModule, MatIcon, MatIconButton, MatCard],
  templateUrl: './notebooks.component.html',
  styleUrl: './notebooks.component.css'
})
export class NotebooksComponent implements OnInit {
  notebooks: Notebook[] = [];
  newNotebookTitle = '';
  loading = false;
  error = '';
  @Input() image = "";

  constructor(
    private notebookService: NotebookService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadNotebooks();
    console.log('Image variable:', this.image);
    this.notebookService.getAllNotebooks().subscribe({
      next: (data) => {
        this.notebooks = data;
        console.log(this.notebooks); // Prüfen, ob die Daten ankommen
      },
      error: (err) => console.error('Fehler beim Laden der Notebooks:', err)
    });
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

  deleteNotebook(id: string, event: Event) {
    event.preventDefault();
    event.stopPropagation();
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Notizbuch löschen',
        message: 'Sind Sie sicher, dass Sie dieses Notizbuch löschen möchten?'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.notebookService.deleteNotebook(id).subscribe({
          next: () => {
            this.notebooks = this.notebooks.filter(nb => nb.id !== id);
          },
          error: error => {
            this.error = 'Fehler beim Löschen des Notizbuchs';
            console.error('Error deleting notebook:', error);
          }
        });
      }
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.notebooks.push(result);
        console.log('Dialog result:', result);
      }
    });
  }

}

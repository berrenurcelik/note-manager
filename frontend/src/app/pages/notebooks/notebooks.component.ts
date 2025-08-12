import { Component, OnInit } from '@angular/core';
import { NgForOf } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { Notebook } from '../../models/notebook.model';
import { Note } from '../../models/note.model';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { CreateNotebookModal } from '../shared/create-notebook.modal/create-notebook.modal';

@Component({
  selector: 'app-notebooks.component',
  imports: [
    MatIcon,
    NgForOf,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent
  ],
  templateUrl: './notebooks.component.html',
  styleUrls: ['./notebooks.component.css']
})
export class NotebooksComponent implements OnInit {

  notebooks: Notebook[] = [];
  constructor(private router: Router, private dialog: MatDialog) { }

  ngOnInit() {

  }

  navigateToNotes(notebookId: string) {
    this.router.navigate(['/notes', notebookId]);
  }

  openNewNotebookDialog() {
    const dialogRef = this.dialog.open(CreateNotebookModal);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.createNotebook(result.title, result.coverImage);
      }
    });
  }

  createNotebook(title: string, coverImage: string) {
    const newId = 'nb' + (this.notebooks.length + 1);
    const newNotebook = new Notebook(
      newId,
      title,
      new Date(),
      [],
      'user123' // oder aktueller User
    );
    // Hier könntest du noch coverImage im Modell speichern, falls dein Notebook-Modell das unterstützt.
    this.notebooks.push(newNotebook);
    // Optional: direkt navigieren oder andere Aktionen ausführen
    this.navigateToNotes(newId);
  }

}

import { Component, Inject } from '@angular/core';
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
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import {MatButton, MatIconButton} from '@angular/material/button';
import { Notebook } from '../../../models/notebook.model';
import { ConfirmDialogComponent } from '../dialog/dialog';
import {MatIcon} from '@angular/material/icon';

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
  ],
  templateUrl: './create-dialog.html',
  styleUrls: ['./create-dialog.css']
})
export class CreateDialog {
  newNotebookTitle: string = "";
  error = '';

  image = [
    'assets/cover1.png','assets/cover2.png','assets/cover3.png','assets/cover4.png',
    'assets/cover5.png','assets/cover6.png','assets/cover7.png','assets/cover8.png',
    'assets/cover9.png','assets/cover10.png'
  ];

  selectedImage: string = this.image[0]; // default Cover
  showImagePicker: boolean = false;

  constructor(
    protected notebookService: NotebookService,
    private dialogRef: MatDialogRef<CreateDialog>,
    private dialog: MatDialog,               // <-- hier MatDialog injizieren
    @Inject(MAT_DIALOG_DATA) public data?: Notebook
  ) {}

  ngOnInit() {
    if (this.data) {
      this.newNotebookTitle = this.data.title;
      this.selectedImage = this.data.image;
    }
  }

  toggleImagePicker() {
    this.showImagePicker = !this.showImagePicker;
  }

  selectImage(img: string) {
    this.selectedImage = img;
    this.showImagePicker = false;
  }

  saveNotebook() {
    if (!this.newNotebookTitle.trim() || !this.selectedImage) return;

    const notebookData: { title: string; image: string } = {
      title: this.newNotebookTitle,
      image: this.selectedImage
    };

    if (this.data && this.data.id) {  // <-- check, dass id existiert
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

  deleteNotebook() {
    if (!this.data || !this.data.id) return;

    const notebookId = this.data.id;  // <-- sichere lokale Kopie

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

  close(): void {
    this.dialogRef.close();
  }

}

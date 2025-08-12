import { Component } from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-create-notebook-modal',
  templateUrl: './create-notebook.modal.html',
  imports: [
    MatDialogContent,
    MatFormField,
    MatLabel,
    FormsModule,
    MatDialogActions,
    MatButton,
    MatInput
  ]
})
export class CreateNotebookModal {
  title = '';
  coverImage = '';

  constructor(private dialogRef: MatDialogRef<CreateNotebookModal>) {}

  onCancel() {
    this.dialogRef.close();
  }

  onCreate() {
    this.dialogRef.close({ title: this.title, coverImage: this.coverImage });
  }
}

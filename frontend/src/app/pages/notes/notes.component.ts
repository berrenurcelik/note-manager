import { Component, OnInit } from '@angular/core';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { Note } from '../../models/note.model';
import { CommonModule } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-notes.component',
  imports: [
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    CommonModule,
    MatIcon
  ],
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.css']
})
export class NotesComponent implements OnInit {
  notes: Note[] = [];

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {

  }
}

import {Component, signal, ViewChild} from '@angular/core';
import { RouterOutlet} from '@angular/router';
import { CommonModule } from '@angular/common';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { NotebooksComponent } from './pages/notebooks/notebooks.component';
import { NotesComponent } from './pages/notes/notes.component';
import {MatIconButton} from '@angular/material/button';
import {MatToolbar} from '@angular/material/toolbar';
import {MatDivider, MatNavList} from '@angular/material/list';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, MatSidenavContainer, MatSidenav, MatSidenavContent, MatIconModule, RouterOutlet, MatIconButton, MatToolbar, MatNavList, MatDivider],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  @ViewChild('sidenav') sidenav!: MatSidenav; // Hier selektieren Sie das Sidenav

  constructor(private router: Router) {}

  protected readonly title = signal('frontend');

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

import {Component, signal, ViewChild} from '@angular/core';
import { Router, RouterOutlet, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {MatToolbar} from '@angular/material/toolbar';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {MatIcon} from '@angular/material/icon';
import {MatActionList, MatDivider, MatListItem} from '@angular/material/list';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import { FlexLayoutModule } from '@angular/flex-layout';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterModule, MatToolbar, MatIcon, MatSidenavContainer, MatSidenav, MatSidenavContent, MatListItem, MatActionList, MatIconButton, MatButton, MatDivider, MatFormField, MatLabel, MatInput, FlexLayoutModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  @ViewChild('sidenav') sidenav!: MatSidenav;

  protected readonly title = signal('Note Manager');
  username: string | null = null;

  constructor(
    private router: Router) {
    this.username = this.capitalizeFirstLetter(localStorage.getItem('username') || '');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.router.navigate(['/login']);
  }

  getCurrentRoute(): string {
    return this.router.url;
  }

  capitalizeFirstLetter(str: string): string {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  isOnNotesPage() {
    return this.router.url.includes('/notes');
  }
}

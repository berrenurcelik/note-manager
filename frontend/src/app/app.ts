import { Component, signal, ViewChild, OnInit } from '@angular/core';
import { Router, RouterOutlet, RouterModule, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { MatToolbar } from '@angular/material/toolbar';
import { MatSidenav, MatSidenavContainer, MatSidenavContent } from '@angular/material/sidenav';
import { MatIcon } from '@angular/material/icon';
import { MatActionList, MatDivider, MatListItem } from '@angular/material/list';
import { MatButton, MatIconButton } from '@angular/material/button';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    RouterModule,
    MatToolbar,
    MatIcon,
    MatSidenavContainer,
    MatSidenav,
    MatSidenavContent,
    MatListItem,
    MatActionList,
    MatIconButton,
    MatButton,
    MatDivider,
    FlexLayoutModule,
    FormsModule
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;

  protected readonly title = signal('Note Manager');
  username = signal<string>('');

  constructor(private router: Router) {}

  ngOnInit() {
    // Username einmal initial laden
    this.loadUsername();

    // Bei jedem Router-Wechsel Username neu laden
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.loadUsername();
      });
  }

  private loadUsername() {
    const storedUser = localStorage.getItem('username') || '';
    this.username.set(this.capitalizeFirstLetter(storedUser));
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.username.set('');
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

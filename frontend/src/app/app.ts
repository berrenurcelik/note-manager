/**
 * @fileoverview Die Haupt-Anwendungskomponente für die Angular-Anwendung.
 * @description Diese Komponente verwaltet die allgemeine Layout-Struktur mit einem Seiten-Navigationsmenü (`sidenav`) und einer Toolbar.
 * Sie kümmert sich um die Benutzeroberfläche, die Anzeige des Benutzernamens, die Authentifizierung und die Navigation.
 * @since 1.0.0
 */
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
  /**
   * Referenz auf die `MatSidenav`-Komponente im Template.
   */
  @ViewChild('sidenav') sidenav!: MatSidenav;

  /**
   * Signal für den Titel der Anwendung.
   * @protected
   */
  protected readonly title = signal('Note Manager');

  /**
   * Signal für den Benutzernamen, der im UI angezeigt wird.
   */
  username = signal<string>('');

  /**
   * Konstruktor, der den Router injiziert.
   * @param {Router} router Der Angular-Router-Service.
   */
  constructor(private router: Router) {}

  /**
   * Angular-Lifecycle-Hook, der bei der Initialisierung der Komponente aufgerufen wird.
   * Lädt den Benutzernamen und abonniert Router-Events, um ihn bei Navigation zu aktualisieren.
   */
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

  /**
   * Lädt den Benutzernamen aus dem lokalen Speicher und aktualisiert das Signal.
   * @private
   */
  private loadUsername() {
    const storedUser = localStorage.getItem('username') || '';
    this.username.set(this.capitalizeFirstLetter(storedUser));
  }

  /**
   * Überprüft, ob der Benutzer angemeldet ist, indem es nach einem Authentifizierungs-Token im lokalen Speicher sucht.
   * @returns {boolean} `true`, wenn ein Token vorhanden ist; andernfalls `false`.
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  /**
   * Meldet den Benutzer ab, indem es Token und Benutzernamen aus dem lokalen Speicher entfernt und zur Login-Seite navigiert.
   */
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.username.set('');
    this.router.navigate(['/login']);
  }

  /**
   * Ruft die aktuelle URL-Route ab.
   * @returns {string} Die aktuelle Router-URL.
   */
  getCurrentRoute(): string {
    return this.router.url;
  }

  /**
   * Wandelt den ersten Buchstaben einer Zeichenkette in einen Großbuchstaben um.
   * @param {string} str Die einzugebende Zeichenkette.
   * @returns {string} Die Zeichenkette mit einem großgeschriebenen ersten Buchstaben.
   */
  capitalizeFirstLetter(str: string): string {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  /**
   * Überprüft, ob die aktuelle Route die Notizen-Seite ist.
   * @returns {boolean} `true`, wenn die URL `/notes` enthält; andernfalls `false`.
   */
  isOnNotesPage() {
    return this.router.url.includes('/notes');
  }
}

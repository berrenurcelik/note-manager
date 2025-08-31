import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

/**
 * Der Login-Komponente-Dienst, der die Authentifizierung des Benutzers handhabt.
 * Dieser Komponenten-Dienst verwaltet die Benutzeroberfläche und die Logik für das Login,
 * einschließlich der Dateneingabe, der Validierung und der Interaktion mit dem Authentifizierungsdienst.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  /**
   * Speichert den vom Benutzer eingegebenen Benutzernamen.
   * @type {string}
   */
  username = '';

  /**
   * Speichert das vom Benutzer eingegebene Passwort.
   * @type {string}
   */
  password = '';

  /**
   * Ein Flag, das auf `true` gesetzt wird, wenn ein Login-Fehler auftritt, z. B. bei falschen Anmeldedaten.
   * @type {boolean}
   */
  loginError = false;

  /**
   * Erstellt eine Instanz von LoginComponent.
   * @param {AuthService} auth Der Dienst zur Handhabung der Authentifizierungslogik.
   * @param {Router} router Der Dienst zur Navigation zu anderen Seiten der Anwendung.
   */
  constructor(private auth: AuthService, private router: Router) {}

  /**
   * Verarbeitet den Anmeldevorgang.
   * Ruft die Login-Methode des Authentifizierungsdienstes auf und behandelt die Antwort.
   * Bei erfolgreichem Login speichert es das Token und den Benutzernamen im `localStorage` und navigiert zum Notizbuch-Dashboard.
   * Bei einem Fehler wird das `loginError`-Flag gesetzt, um eine Fehlermeldung anzuzeigen.
   */
  onLogin() {
    this.auth.login(this.username, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', this.username);
        this.router.navigate(['/notebooks']);
      },
      error: () => {
        this.loginError = true;
      },
    });
  }
}

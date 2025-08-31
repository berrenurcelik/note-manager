import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

/**
 * @fileoverview Der Register-Komponente-Dienst, der die Benutzerregistrierung handhabt.
 * @description Dieser Komponenten-Dienst verwaltet die Benutzeroberfläche und die Logik für die Benutzerregistrierung,
 * einschließlich der Dateneingabe, der Validierung und der Interaktion mit dem Authentifizierungsdienst.
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  /**
   * Speichert den vom Benutzer eingegebenen Benutzernamen.
   * @type {string}
   */
  username = '';

  /**
   * Speichert den vom Benutzer eingegebenen Vornamen.
   * @type {string}
   */
  firstName = '';

  /**
   * Speichert den vom Benutzer eingegebenen Nachnamen.
   * @type {string}
   */
  lastName = '';

  /**
   * Speichert die vom Benutzer eingegebene E-Mail-Adresse.
   * @type {string}
   */
  email = '';

  /**
   * Speichert das vom Benutzer eingegebene Passwort.
   * @type {string}
   */
  password = '';

  /**
   * Ein Flag, das auf `true` gesetzt wird, wenn ein Registrierungsfehler auftritt.
   * @type {boolean}
   */
  registerError = false;

  /**
   * Ein Flag, das auf `true` gesetzt wird, wenn die Registrierung erfolgreich war.
   * @type {boolean}
   */
  registerSuccess = false;

  /**
   * Erstellt eine Instanz von RegisterComponent.
   * @param {AuthService} auth Der Dienst zur Handhabung der Authentifizierungslogik.
   * @param {Router} router Der Dienst zur Navigation zu anderen Seiten der Anwendung.
   */
  constructor(private auth: AuthService, private router: Router) {}

  /**
   * Verarbeitet den Registrierungsvorgang.
   * Erfasst die Benutzerdaten aus dem Formular und ruft die Registrierungsmethode des Authentifizierungsdienstes auf.
   * Bei erfolgreicher Registrierung wird ein Erfolgsflag gesetzt, eine Meldung in der Konsole ausgegeben und nach einer kurzen Verzögerung zur Login-Seite navigiert.
   * Bei einem Fehler wird das `registerError`-Flag gesetzt, um eine Fehlermeldung anzuzeigen.
   */
  onRegister() {
    const userData = {
      username: this.username,
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password
    };

    this.auth.register(userData).subscribe({
      next: (res) => {
        this.registerSuccess = true;
        console.log('User registered successfully:', userData);
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: () => {
        this.registerError = true;
      },
    });
  }
}

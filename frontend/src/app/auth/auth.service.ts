import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * @fileoverview Der Authentifizierungsdienst, der die Interaktion mit den Authentifizierungs- und Benutzer-APIs handhabt.
 * @description Dieser Dienst bietet Methoden für die Benutzeranmeldung, Registrierung, den Abruf von Benutzerlisten,
 * die Überprüfung des Anmeldestatus und das Abmelden. Er ist in der gesamten Anwendung als Singleton verfügbar.
 */

/**
 * Schnittstelle für die Anmeldeanforderung.
 * @property {string} username - Der Benutzername des Benutzers.
 * @property {string} password - Das Passwort des Benutzers.
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * Schnittstelle für die Anmeldeantwort.
 * @property {string} token - Das Authentifizierungstoken.
 */
export interface LoginResponse {
  token: string;
}

/**
 * Schnittstelle für die Registrierungsanforderung.
 * @property {string} username - Der gewünschte Benutzername.
 * @property {string} firstName - Der Vorname des Benutzers.
 * @property {string} lastName - Der Nachname des Benutzers.
 * @property {string} email - Die E-Mail-Adresse des Benutzers.
 * @property {string} password - Das gewünschte Passwort.
 */
export interface RegisterRequest {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

/**
 * Ein Dienst zur Handhabung der Benutzerauthentifizierung.
 * Dieser Dienst ist auf Root-Ebene bereitgestellt, sodass er in der gesamten Anwendung als Singleton-Instanz verwendet werden kann.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {

  /**
   * Erstellt eine Instanz von AuthService.
   * @param {HttpClient} http Der Angular HttpClient-Dienst, der für HTTP-Anfragen an die API verwendet wird.
   */
  constructor(private http: HttpClient) {}

  /**
   * Sendet eine Anmeldeanfrage an die API.
   * @param {string} username - Der Benutzername des Benutzers.
   * @param {string} password - Das Passwort des Benutzers.
   * @returns {Observable<LoginResponse>} Ein Observable, das die Anmeldeantwort enthält.
   */
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      '/api/auth/login',
      { username, password }
    );
  }

  /**
   * Sendet eine Registrierungsanforderung für einen neuen Benutzer an die API.
   * @param {RegisterRequest} userData - Die Daten des Benutzers, die für die Registrierung erforderlich sind.
   * @returns {Observable<any>} Ein Observable, das die API-Antwort enthält.
   */
  register(userData: RegisterRequest): Observable<any> {
    return this.http.post<any>('/api/users', userData);
  }

  /**
   * Ruft eine Liste aller registrierten Benutzer von der API ab.
   * Diese Anfrage erfordert ein gültiges Authentifizierungstoken.
   * @returns {Observable<any[]>} Ein Observable, das ein Array von Benutzerdaten enthält.
   */
  getUsers(): Observable<any[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any[]>('/api/users', { headers });
  }

  /**
   * Überprüft, ob der Benutzer angemeldet ist, indem es nach einem Authentifizierungstoken im `localStorage` sucht.
   * @returns {boolean} `true`, wenn ein Token vorhanden ist, andernfalls `false`.
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  /**
   * Meldet den Benutzer ab, indem es das Authentifizierungstoken aus dem `localStorage` entfernt.
   * @returns {void}
   */
  logout(): void {
    localStorage.removeItem('token');
  }
}

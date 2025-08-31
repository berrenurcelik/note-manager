/**
 * Angular-Service für die Verwaltung von Notizbüchern.
 * Dieser Dienst interagiert mit der Backend-API, um CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen) für Notizbücher durchzuführen.
 * Er verwendet `HttpClient` für die HTTP-Anfragen und `localStorage` zur Authentifizierung.
 * @since 1.0.0
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notebook } from '../models/notebook.model';

@Injectable({
  providedIn: 'root'
})
export class NotebookService {
  /**
   * Die Basis-URL für die Notizbücher-API.
   * @private
   */
  private apiUrl = 'http://localhost:8080/api/notebooks';

  /**
   * Konstruktor, der den `HttpClient` injiziert.
   * @param {HttpClient} http Der Angular-HTTP-Client für API-Anfragen.
   */
  constructor(private http: HttpClient) {}

  /**
   * Erstellt die HTTP-Header für die Authentifizierung mit einem Bearer-Token aus dem lokalen Speicher.
   * @private
   * @returns {HttpHeaders} Die konfigurierten HTTP-Header.
   */
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  /**
   * Ruft alle Notizbücher ab.
   * @returns {Observable<Notebook[]>} Ein Observable, das ein Array von Notizbuch-Objekten liefert.
   */
  getAllNotebooks(): Observable<Notebook[]> {
    return this.http.get<Notebook[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  /**
   * Ruft ein einzelnes Notizbuch anhand seiner ID ab.
   * @param {string} id Die ID des zu suchenden Notizbuchs.
   * @returns {Observable<Notebook>} Ein Observable, das ein einzelnes Notizbuch-Objekt liefert.
   */
  getNotebookById(id: string): Observable<Notebook> {
    return this.http.get<Notebook>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  /**
   * Erstellt ein neues Notizbuch.
   * @param {{ title: string, image: string }} notebook Das Notizbuch-Objekt, das erstellt werden soll.
   * @returns {Observable<Notebook>} Ein Observable, das das neu erstellte Notizbuch zurückgibt.
   */
  createNotebook(notebook: { title: string, image: string }): Observable<Notebook> {
    return this.http.post<Notebook>(this.apiUrl, notebook, { headers: this.getHeaders() });
  }

  /**
   * Aktualisiert ein bestehendes Notizbuch.
   * @param {string} id Die ID des zu aktualisierenden Notizbuchs.
   * @param {{ title: string, image: string }} notebook Das Notizbuch-Objekt mit den aktualisierten Daten.
   * @returns {Observable<Notebook>} Ein Observable, das das aktualisierte Notizbuch zurückgibt.
   */
  updateNotebook(id: string, notebook: { title: string, image: string }): Observable<Notebook> {
    return this.http.put<Notebook>(`${this.apiUrl}/${id}`, notebook, { headers: this.getHeaders() });
  }

  /**
   * Löscht ein Notizbuch anhand seiner ID.
   * @param {string} id Die ID des zu löschenden Notizbuchs.
   * @returns {Observable<void>} Ein Observable, das nach dem Löschen abgeschlossen wird.
   */
  deleteNotebook(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}

/**
 * @fileoverview Angular-Service für die Verwaltung von Notizen.
 * @description Dieser Dienst interagiert mit der Backend-API, um CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen) für Notizen durchzuführen.
 * Er verwendet `HttpClient` für die HTTP-Anfragen und `localStorage` zur Authentifizierung.
 * @since 1.0.0
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Note } from '../models/note.model';

@Injectable({
  providedIn: 'root'
})
export class NoteService {
  /**
   * Die Basis-URL für die Notizen-API.
   * @private
   */
  private apiUrl = 'http://localhost:8080/api/notes';

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
   * Ruft alle Notizen ab, optional gefiltert nach einer Notizbuch-ID.
   * @param {string} [notebookId] Optional: Die ID des Notizbuchs, um Notizen zu filtern.
   * @returns {Observable<Note[]>} Ein Observable, das ein Array von Notiz-Objekten liefert.
   */
  getAllNotes(notebookId?: string): Observable<Note[]> {
    const url = notebookId ? `${this.apiUrl}?notebookId=${notebookId}` : this.apiUrl;
    return this.http.get<Note[]>(url, { headers: this.getHeaders() });
  }

  /**
   * Ruft eine einzelne Notiz anhand ihrer ID ab.
   * @param {string} id Die ID der zu suchenden Notiz.
   * @returns {Observable<Note>} Ein Observable, das ein einzelnes Notiz-Objekt liefert.
   */
  getNoteById(id: string): Observable<Note> {
    return this.http.get<Note>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  /**
   * Sucht nach Notizen basierend auf einem Titel.
   * @param {string} title Der Titel oder ein Teil des Titels, nach dem gesucht werden soll.
   * @returns {Observable<Note[]>} Ein Observable, das ein Array von passenden Notizen liefert.
   */
  searchNotes(title: string): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/search?title=${encodeURIComponent(title)}`, { headers: this.getHeaders() });
  }

  /**
   * Erstellt eine neue Notiz.
   * @param {{ title: string; content: string; notebookId: string | null }} note Das Notiz-Objekt, das erstellt werden soll.
   * @returns {Observable<Note>} Ein Observable, das die neu erstellte Notiz zurückgibt.
   */
  createNote(note: { title: string; content: string; notebookId: string | null }): Observable<Note> {
    return this.http.post<Note>(this.apiUrl, note, { headers: this.getHeaders() });
  }

  /**
   * Aktualisiert eine bestehende Notiz.
   * @param {string} id Die ID der zu aktualisierenden Notiz.
   * @param {{ title: string; content: string; notebookId?: string | null }} note Das Notiz-Objekt mit den aktualisierten Daten.
   * @returns {Observable<Note>} Ein Observable, das die aktualisierte Notiz zurückgibt.
   */
  updateNote(id: string, note: { title: string; content: string; notebookId?: string | null }): Observable<Note> {
    return this.http.put<Note>(`${this.apiUrl}/${id}`, note, { headers: this.getHeaders() });
  }

  /**
   * Löscht eine Notiz anhand ihrer ID.
   * @param {string} id Die ID der zu löschenden Notiz.
   * @returns {Observable<void>} Ein Observable, das nach dem Löschen abgeschlossen wird.
   */
  deleteNote(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}

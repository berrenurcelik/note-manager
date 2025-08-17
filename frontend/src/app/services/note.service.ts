import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Note } from '../models/note.model';

@Injectable({
  providedIn: 'root'
})
export class NoteService {
  private apiUrl = 'http://localhost:8080/api/notes';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllNotes(notebookId?: string): Observable<Note[]> {
    const url = notebookId ? `${this.apiUrl}?notebookId=${notebookId}` : this.apiUrl;
    return this.http.get<Note[]>(url, { headers: this.getHeaders() });
  }

  getNoteById(id: string): Observable<Note> {
    return this.http.get<Note>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  searchNotes(title: string): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/search?title=${encodeURIComponent(title)}`, { headers: this.getHeaders() });
  }

  createNote(note: { title: string; content: string; notebookId: string | null }): Observable<Note> {
    return this.http.post<Note>(this.apiUrl, note, { headers: this.getHeaders() });
  }

  updateNote(id: string, note: { title: string; content: string; notebookId?: string | null }): Observable<Note> {
    return this.http.put<Note>(`${this.apiUrl}/${id}`, note, { headers: this.getHeaders() });
  }

  deleteNote(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}

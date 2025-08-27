import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notebook } from '../models/notebook.model';

@Injectable({
  providedIn: 'root'
})
export class NotebookService {
  private apiUrl = 'http://localhost:8080/api/notebooks';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllNotebooks(): Observable<Notebook[]> {
    return this.http.get<Notebook[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getNotebookById(id: string): Observable<Notebook> {
    return this.http.get<Notebook>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  createNotebook(notebook: { title: string, image: string }): Observable<Notebook> {
    return this.http.post<Notebook>(this.apiUrl, notebook, { headers: this.getHeaders() });
  }

  updateNotebook(id: string, notebook: { title: string, image: string }): Observable<Notebook> {
    return this.http.put<Notebook>(`${this.apiUrl}/${id}`, notebook, { headers: this.getHeaders() });
  }

  deleteNotebook(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}

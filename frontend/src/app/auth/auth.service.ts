import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      '/api/auth/login',
      { username, password }
    );
  }

  register(username: string, password: string): Observable<any> {
    return this.http.post<any>(
      '/api/users', 
      { username, password }
    );
  }
}

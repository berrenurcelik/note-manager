import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <h1 style="text-align:center;margin-top:3rem;">Welcome!</h1>
    <div style="text-align:center;margin-top:2rem;">
      <button (click)="logout()">Logout</button>
    </div>
  `
})
export class HomeComponent {
  constructor(private router: Router) {}

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

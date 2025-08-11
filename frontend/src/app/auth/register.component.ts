import { Component } from '@angular/core';
import { AuthService } from './auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  username = '';
  password = '';
  registerError = false;
  registerSuccess = false;

  constructor(private auth: AuthService, private router: Router) {}

  onRegister() {
    this.auth.register(this.username, this.password).subscribe({
      next: (res) => {
        this.registerSuccess = true;
        console.log('User registered successfully:', { username: this.username });
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: () => {
        this.registerError = true;
      },
    });
  }
}

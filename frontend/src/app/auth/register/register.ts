import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  username = '';
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  registerError = false;
  registerSuccess = false;

  constructor(private auth: AuthService, private router: Router) {}

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

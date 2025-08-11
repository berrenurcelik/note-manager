import { Component } from '@angular/core';
import { AuthService } from './auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError = false;
  loginSuccess = false;

  constructor(
    private auth: AuthService, 
    private router: Router,
    private fb: FormBuilder
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get username() { return this.loginForm.get('username')?.value || ''; }
  get password() { return this.loginForm.get('password')?.value || ''; }

  onLogin() {
    this.auth.login(this.username, this.password).subscribe({
      next: (res) => {
        if (res && res.token) {
          localStorage.setItem('token', res.token);
          this.loginSuccess = true;
          setTimeout(() => this.router.navigate(['/']), 1000);
        } else {
          this.loginError = true;
        }
      },
      error: () => {
        this.loginError = true;
      },
    });
  }
}

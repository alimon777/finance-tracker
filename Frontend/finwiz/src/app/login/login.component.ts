import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.credentials).subscribe(
      response => {
        if (response.token) {
          this.authService.storeToken(response.token); // Store token
          this.router.navigate(['/home']);
        }
      },
      error => {
        this.errorMessage = 'Invalid credentials';
        console.error('Login failed', error);
      }
    );
  }
}

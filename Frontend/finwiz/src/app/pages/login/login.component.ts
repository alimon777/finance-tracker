import { Component } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';
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
        // 'response' will be the plain text JWT token
        console.log('Login response:', response);
        this.authService.storeToken(response); // Store token
        this.router.navigate(['/home']);
      },
      error => {
        this.errorMessage = 'Invalid credentials';
        console.error('Login failed', error);
      }
    );
  }
}
  


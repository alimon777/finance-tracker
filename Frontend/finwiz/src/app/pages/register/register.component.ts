import { Component } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  user = { username: '', password: '' };
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.register(this.user).subscribe(
      response => {
        this.successMessage = 'Registration successful! Please login.';
        this.errorMessage = null;
        this.router.navigate(['/login']);
      },
      error => {
        this.errorMessage = 'Registration failed. Please try again.';
        console.error('Registration error', error);
      }
    );
  }
}

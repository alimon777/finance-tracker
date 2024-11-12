import { Component } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  user = { username: '', password: '', confirmPassword: '', firstName: '', lastName: '', email: '', phone: '' };
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.user.password !== this.user.confirmPassword) {
      this.errorMessage = 'Passwords do not match. Please try again.';
      return;
    }
  
    this.authService.register(this.user).subscribe(
      response => {
        this.successMessage = 'Registration successful! Please login.';
        window.alert('Registration successful! Please login.');
        this.errorMessage = null;
        this.router.navigate(['/login']);
      },
      error => {
        // Error already handled and parsed in the service, so directly use it
        this.errorMessage = error; // `error` here will be a string due to `handleError` in `AuthService`
        console.error('Registration error', error);
      }
    );
  }
  
}

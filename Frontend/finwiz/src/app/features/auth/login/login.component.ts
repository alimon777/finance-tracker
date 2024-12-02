import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';
import { StorageService } from 'src/app/core/services/storage/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router
  ) { }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  onSubmit() {
    this.authService.login(this.credentials).subscribe(
      response => {
        console.log('Login response:', response);
        this.storageService.addUserDetailsToStorage(response);
        this.router.navigate(['/home']);
      },
      error => {
        this.errorMessage = 'Invalid credentials';
        console.error('Login failed', error);
      }
    );
  }
}

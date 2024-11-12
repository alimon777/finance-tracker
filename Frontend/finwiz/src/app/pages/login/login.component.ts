import { Component } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/service/storage/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private storageService : StorageService,
    private router: Router
  ) {}

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

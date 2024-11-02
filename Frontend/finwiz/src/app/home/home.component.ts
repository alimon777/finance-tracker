import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  token: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.token = this.authService.getToken(); // Get the token
    if (!this.token) {
      this.router.navigate(['/login']); // Redirect to login if no token
    }
  }

  logout() {
    this.authService.clearToken(); // Clear token from storage
    this.router.navigate(['/login']);
  }
}

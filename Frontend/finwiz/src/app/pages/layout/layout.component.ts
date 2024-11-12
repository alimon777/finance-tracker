import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthService } from 'src/app/service/auth/auth.service';
import { StorageService } from 'src/app/service/storage/storage.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet,RouterModule],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
})
export class LayoutComponent {
  username: string | null = null;
  token: string | null = null;

  constructor(
    private storageService: StorageService,
    private router: Router
  ) {}

  ngOnInit() {
    this.token = this.storageService.getToken(); // Get the token
    this.username = localStorage.getItem('username');
    if (!this.token) {
      this.router.navigate(['/login']); // Redirect to login if no token
    }
  }

  logout() {
    this.storageService.clearStorage(); // Clear token from storage
    this.router.navigate(['/login']);
  }
}

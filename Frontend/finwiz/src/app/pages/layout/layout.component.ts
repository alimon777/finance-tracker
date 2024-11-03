import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from 'src/app/service/auth/auth.service';
// import { HTTP_INTERCEPTORS  } from '@angular/common/http';
// import { CustomInterceptor } from 'src/app/service/custom/custom.interceptor';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
  // providers: [
  //   { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true }
  // ]
})
export class LayoutComponent {

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

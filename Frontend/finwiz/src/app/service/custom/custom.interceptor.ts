import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { StorageService } from '../storage/storage.service';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    // private router:Router
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Check if the request URL matches a specific pattern
    if (req.url.includes('/api/')) { // Adjust the condition as needed
      if (token) {
        // if (this.authService.isTokenExpired()) {
        //   // If expired, clear the token and redirect to the login page
        //   this.authService.clearToken();
        //   this.router.navigate(['/login']);
        //   return throwError('Token expired. Redirecting to login...'); // Optionally, you can cancel the request here or prevent further operations
        // }
        const clonedRequest = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          }
        });
        return next.handle(clonedRequest);
      }
    }
    
    // Proceed without modifying the request
    return next.handle(req);
  }
}

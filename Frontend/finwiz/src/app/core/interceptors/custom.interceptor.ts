import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth/auth.service'
import { StorageService } from '../services/storage/storage.service';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private storageService: StorageService,
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    const userId = this.storageService.fetchUserId();

    // Handle Authorization header for requests to specific URLs
    if (req.url.includes('/api/')) {
      let clonedRequest = req;

      if (token) {
        clonedRequest = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
      }

      // Add userId as a query parameter for GET requests
      if (req.method === 'GET' && userId) {
        const modifiedParams = clonedRequest.params.set('userId', userId);
        clonedRequest = clonedRequest.clone({ params: modifiedParams });
      }

      return next.handle(clonedRequest);
    }

    // Proceed without modifying the request
    return next.handle(req);
  }

}

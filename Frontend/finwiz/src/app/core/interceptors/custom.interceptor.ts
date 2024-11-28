import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable} from 'rxjs';
import { AuthService } from 'src/app/core/services/auth/auth.service'

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Add Authorization headers if the request URL matches a specific pattern
    if (req.url.includes('/api/')) { 
      if (token) {
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

import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // const token = localStorage.getItem('token');

    // // Check if the request URL matches a specific pattern
    // if (req.url.includes('/api/')) { // Adjust the condition as needed
    //   if (token) {
    //     const clonedRequest = req.clone({
    //       setHeaders: {
    //         Authorization: `Bearer ${token}`,
    //         'Content-Type': 'application/json',
    //       }
    //     });
    //     return next.handle(clonedRequest);
    //   }
    // }
    
    // Proceed without modifying the request
    return next.handle(req);
  }
}

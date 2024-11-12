import { Injectable } from '@angular/core';
import { UserDetails } from 'src/app/models/user-details';

@Injectable({
    providedIn: 'root'
})
export class StorageService {

    constructor() {}

    fetchUserDetails(): UserDetails {
        const storedUsername = localStorage.getItem('username');
        const storedEmail = localStorage.getItem('email');

        return {
            username: storedUsername ? storedUsername : "John Doe",
            email: storedEmail ? storedEmail : "alimonna777@gmail.com"
      };
    }

    fetchUserId() : number {
      const storedUserId = localStorage.getItem('userId');
      return storedUserId ? +storedUserId : 0;
    }

    addUserDetailsToStorage(response: { token: string; userId: string; username: string , email: string}) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('userId', response.userId);
      localStorage.setItem('username', response.username);
      localStorage.setItem('email',response.email);
      localStorage.removeItem('budgetSuggestion'); 
    }
  
    // Method to retrieve token from local storage
    getToken(): string | null {
      return localStorage.getItem('token');
    }
  
    // Method to clear token from local storage
    clearStorage() {
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      localStorage.removeItem('budgetSuggestion');
    }
}

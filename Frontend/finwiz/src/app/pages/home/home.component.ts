import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  members: any[] = []; // Array to store the member data

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getMembers();
  }

  getMembers(): void {
    this.http.get<any[]>('http://localhost:8099/member/getAll')
      .subscribe(
        data => {
          this.members = data;
          console.log('Members fetched successfully:', this.members);
        },
        error => {
          console.error('Error fetching members:', error);
        }
      );
  }
}
  // token: string | null = null;

  // constructor(private authService: AuthService, private router: Router) {}

  // ngOnInit() {
  //   this.token = this.authService.getToken(); // Get the token
  //   if (!this.token) {
  //     this.router.navigate(['/login']); // Redirect to login if no token
  //   }
  // }

  // logout() {
  //   this.authService.clearToken(); // Clear token from storage
  //   this.router.navigate(['/login']);
  // }
// }

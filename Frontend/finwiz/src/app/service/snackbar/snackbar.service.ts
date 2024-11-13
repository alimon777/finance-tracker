import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {
  constructor(private snackbar: MatSnackBar) {}

  show(message: string, action: string = 'Close', duration: number = 3000) {
    this.snackbar.open(message, action, {
      duration,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}

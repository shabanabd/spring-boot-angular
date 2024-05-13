import { Injectable } from '@angular/core';
import { User } from '../shared/user';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';
  headers = new HttpHeaders().set('Content-Type', 'application/json');
  currentUser = {};

  constructor(private http: HttpClient, private router: Router) {
  }

  signIn(user: User) {
    return this.http
      .post<any>(`${this.baseUrl}/api/auth/login`, user)
      .pipe(catchError(this.handleError))
      .subscribe((res: any) => {
        localStorage!.setItem('access_token', res.accessToken);
        this.router.navigate(['/dashboard' ]);
      });
  }
  getToken() {
    return localStorage!.getItem('access_token');
  }
  get isLoggedIn(): boolean {
    if (typeof localStorage !== 'undefined') {
      let authToken = localStorage.getItem('access_token');
      return authToken !== null ? true : false;
    }
    return false;
  }
  doLogout() {
    let removeToken = localStorage!.removeItem('access_token');
    if (removeToken == null) {
      this.router.navigate(['log-in']);
    }
  }

  handleError(error: HttpErrorResponse) {
    let msg = '';
    window.alert('Invalid Username or Password!');
    if (error.error instanceof ErrorEvent) {
      msg = error.error.message;
    } else {
      msg = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(msg);
  }
}

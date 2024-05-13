import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {Application, ApplicationResponse} from '../shared/application'
import {Statistics} from "../shared/statistics";

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getApplications(param: any): Observable<ApplicationResponse> {
    return this.http.get<ApplicationResponse>(this.baseUrl + '/api/applications', {params:param})
      .pipe(
        catchError(this.handleError)
      );
  }

  addApplication(app: Application): Observable<number> {
    return this.http.post<number>(this.baseUrl + '/api/applications', app)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateApplication(app: Application): Observable<number> {
    return this.http.put<number>(this.baseUrl + '/api/applications', app)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteApplication(appId: number): Observable<any> {
    return this.http.delete(this.baseUrl + '/api/applications/' + appId)
      .pipe(
        catchError(this.handleError)
      );
  }

  exportApplications(param: any): Observable<HttpResponse<any>> {
    let headers = new HttpHeaders();
    headers = headers.append('Accept', 'text/csv; charset=utf-8');
    return this.http.get<HttpResponse<any>>(this.baseUrl + '/api/applications/export', {headers:headers, params:param, observe:"response", 'responseType': 'text' as 'json'})
      .pipe(
        catchError(this.handleError)
      );
  }

  getApplicationStatistics(): Observable<Statistics> {
    return this.http.get<Statistics>(this.baseUrl + '/api/applications/statistics')
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}

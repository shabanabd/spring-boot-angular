import { Injectable } from "@angular/core";
import {HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse} from "@angular/common/http";
import { AuthService } from "../services/auth.service";
import {Observable, of, throwError} from "rxjs";
import {catchError} from "rxjs/operators";
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) { }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    //handle auth error or rethrow
    if (err.status === 401 || err.status === 403) {
      this.authService.doLogout();
    }
    return throwError(err);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const authToken = this.authService.getToken();
    if(authToken != null) {
      req = req.clone({
        setHeaders: {
          Authorization: "Bearer " + authToken
        }
      });
    }
    return next.handle(req).pipe(catchError(err=> this.handleAuthError(err)));
  }
}

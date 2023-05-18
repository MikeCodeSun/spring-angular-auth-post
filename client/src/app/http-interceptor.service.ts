import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppService } from './app.service';
@Injectable({
  providedIn: 'root',
})

// inter cept header is log with jsession
export class HttpInterceptorService implements HttpInterceptor {
  constructor(private appService: AppService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.appService.isLogin) {
      const authReq = req.clone({ withCredentials: true });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}

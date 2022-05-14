import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UtilService } from './util.service';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor{

  constructor(private utilService:UtilService) { }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if(this.utilService.isLoggedIn()){
     const headers =   req.headers.set('Authorization',`Bearer ${this.utilService.getUserToken()['token']}`)
      req  = req.clone({headers:  headers})
    }

    const request  = req.clone({url:environment.apiUrl+req.url});
  return next.handle(request)
  }
}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { User } from './model/user';

@Injectable({
  providedIn: 'root'
})
export class ApiServiceService {

  baseUrl: string = "http://localhost:8081/api/v1";
  user:User = new User();
  

  constructor(
    private httpClient: HttpClient
  ) { }

  getHeaders() {
    // create new instance for each api call
    const httpHeaders = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem('access_token'))
        .set('Content-Type', 'application/json')
    };
    return httpHeaders;
  }

  getFileHeaders() {
    // create new instance for each api call
    const httpHeaders = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem('access_token'))
    };
    return httpHeaders;
  }
  signUp(user: Object): Observable<Object> {
  	return this.httpClient.post(`${this.baseUrl}/sign-up`, user);
  }

  login(user: Object): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}/acc/login`, user);
  }
}

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { User } from './model/user';
import { Expense } from './model/expense';

@Injectable({
  providedIn: 'root'
})
export class ApiServiceService {

  baseUrl: string = "http://localhost:8081/api/v1";
  user:User = new User();
  expense:Expense = new Expense();
  

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
    console.log('HTTP Headers:', httpHeaders);
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

  //Account
  signUp(user: Object): Observable<Object> {
  	return this.httpClient.post(`${this.baseUrl}/acc/signup`, user);
  }

  login(user: Object): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}/acc/login`, user);
  }

  refreshLogin() {
    return this.httpClient.post(`${this.baseUrl}/sign-in/refresh`, {}, this.getHeaders());
  }
  //Expense
  addExp(expsense: Object): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}/exp/addExp`, expsense, this.getHeaders());
  }

  getExp(username:any) : Observable<Object>{
    return this.httpClient.get(`${this.baseUrl}/exp/getExp?username=`+username, this.getHeaders());
  }

  delExp(trans_id:any) : Observable<Object>{
    const headers = this.getHeaders();
    const token = localStorage.getItem('access_token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      console.log('Token Payload:', payload);
      const now = Math.floor(new Date().getTime() / 1000);
      console.log('Token Issued At:', payload.iat);
      console.log('Token Expiry:', payload.exp);
      console.log('Current Time:', now);
      if (payload.exp < now) {
        console.error("Token is expired");
        // Handle token expiration (e.g., redirect to login)
      }
    } else {
      console.error("No access token found");
    }
    return this.httpClient.post(`${this.baseUrl}/exp/deleteExp?id=`+trans_id+'&username='+localStorage.getItem("username"), this.getHeaders());
  }

  //Graph
  getTotal(username:any) : Observable<Object>{
    return this.httpClient.get(`${this.baseUrl}/graph/getTotalData?username=`+username, this.getHeaders());
  }
}

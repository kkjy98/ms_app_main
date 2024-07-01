import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiServiceService } from '../api-service.service';
import { User } from '../model/user';
import { lastValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  isLoading = false;
  isError = false;
  loginForm!: FormGroup;
  result!:any;
  resultCode!: number;
  user:User = new User();
  errorMessage: string | undefined;
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private apiservice: ApiServiceService
  ) {
    this.loginForm = this.formBuilder.group({
      email: '',
      password: ''
    });
  }

  ngOnInit(): void {
    // You can remove this line as the form is already initialized in the constructor.
  }

  async submit() {
    this.isLoading = true;
    this.isError = false;
    this.user.username = this.loginForm.controls['email'].value;
    this.user.password = this.loginForm.controls['password'].value;
    await lastValueFrom(this.apiservice.login(this.user)).then((res:any) => {
      this.result=res;
      //get data & set on localStorage
      localStorage.setItem('access_token', this.result.access_token);

      //get result code
      console.log("Response");
      this.resultCode = this.result.result_code;
      console.log(this.resultCode);
      console.log(this.result);
      console.log(this.isTokenExpired(this.result.access_token));

    }).catch(err => {
      console.log("Error");
      console.log(err);
      this.resultCode = err.error.result_code;
       this.handleError(err);
    });

    //go to homepage if success
    if(this.resultCode == 1000){
        this.isLoading = false;
        this.router.navigate(['/home']);
        return;
      } else if (this.resultCode==401){
        this.isLoading = false;
        this.isError = true;

      }
    
  }

  isTokenExpired(token: any) {
    token = token.split(".");
    var decryptedToken = JSON.parse(window.atob(token[1]));

    var tokenExpiry = decryptedToken.exp;
    var currentTime = Math.round(new Date().getTime() / 1000);
    return currentTime > tokenExpiry;
  }

  private handleError(httpErrorResponse: HttpErrorResponse) {

    var errorResponse = httpErrorResponse.error;

    if (errorResponse && errorResponse.error) {
      let apiError: any = errorResponse.error;
      let message: string = apiError.message;

      this.resultCode = errorResponse.code;
      this.errorMessage = message;

      return;
    }

  }
}

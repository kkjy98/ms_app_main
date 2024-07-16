import { Injectable } from '@angular/core';
import { ApiServiceService } from './api-service.service';
import { Router } from '@angular/router';
import { fromEvent, timer } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

    //5 mins
    idleDuration:number = 300000;

    data: any = {};
    lastActivityTime:any = Date.now();

    constructor(
      private router: Router,
      //private offcanvasService: NgbOffcanvas
      private apiService: ApiServiceService
    ) {
      fromEvent(document, 'click').pipe().subscribe(() => {this.lastActivityTime = Date.now()});
      fromEvent(document, 'keypress').pipe().subscribe(() => {this.lastActivityTime = Date.now()});
    }

    isTokenExpired(token: any) {
      token = token.split(".");
      var decryptedToken = JSON.parse(window.atob(token[1]));
  
      var tokenExpiry = decryptedToken.exp;
      var currentTime = Math.round(new Date().getTime() / 1000);
      return currentTime > tokenExpiry;
    }

    checkExpiringToken(token: any) {

      /* 
        1. when authguard is triggered, this will run and prompt the user once the token is expiring
        2. if the user doesnt click refresh token on the prompt, it will remove the token from localStorage
        3. redirect user to login page because no accessToken in localStorage
      */
    
      if (!token || this.isTokenExpired(token)) {
        localStorage.removeItem('access_token');
        //this.showExpiredAlert(this.router.url);
        return;
      }
    
      var remainingTokenDuration = this.getRemainingTokenDuration(token);
    
      // Starts 60 seconds before expiry
      timer((remainingTokenDuration - 60) * 1000).subscribe(() => {
    
        var timerInterval: any;
    
        /*
          If user's last click/type is within 5 mins, it will automatically renew the token
            else
          If user is inactive, it will show the pop-up
        */
        if ((Date.now() - this.lastActivityTime) < this.idleDuration) {
    
          this.data.returnUrl = this.router.url;
          this.data.accessToken = localStorage.getItem('access_token');
    
          this.refreshToken(this.data).subscribe({
            next: (res: any) => {
              console.log("Enter refresh api");
              var accessToken = res.data.access_token;
              localStorage.setItem('access_token', accessToken);
    
              this.checkExpiringToken(accessToken);
            },
            error: () => {
              setTimeout(() => {
                localStorage.removeItem('access_token');
                this.router.navigate(['/login']);
              }, 3000);
            }
          });
        } else {
          //this.showExpiredAlert(this.router.url);
        }
      });
    }
    
  
   getRemainingTokenDuration(token: any) {
      token = token.split(".");
      var decryptedToken = JSON.parse(window.atob(token[1]));
  
      var tokenExpiry = decryptedToken.exp;
      var currentTime = Math.round(new Date().getTime() / 1000);
      return tokenExpiry - currentTime;
    }

  refreshToken(data: any) {
    return this.apiService.refreshLogin();
  }

  // showExpiredAlert(returnUrl?: string) {
  //   Swal.fire({
  //     title: 'Session Expired!',
  //     text: 'You have been automatically logged out.',
  //     confirmButtonColor: '#508A7B',
  //     allowOutsideClick: false
  //   }).then(() => {
  //     //this.offcanvasService.dismiss();
  //     if (returnUrl) {
  //       this.router.navigate(['/login'], { queryParams: { returnUrl: decodeURI(returnUrl) } });
  //     } else {
  //       this.router.navigate(['/login']);
  //     }
  //   });
  // }


}

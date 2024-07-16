import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  var accessToken = localStorage.getItem('access_token');
  const router = inject(Router);
  const authService = inject(AuthService);

  console.log("Auth guard");
  console.log(accessToken);
    // Checks if token is within prompt range
    //authService.checkExpiringToken(accessToken);

    if (!accessToken) {
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }

    if (authService.isTokenExpired(accessToken)) {
      router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      return false;
    }
  

  return true;
};

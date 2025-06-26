import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {KeycloakService} from '../keycloak/keycloak.service';
import {from, switchMap} from 'rxjs';

export const keycloakHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);
  const keycloak = keycloakService.keycloak;

  if (keycloak.token) {
    if (keycloak.isTokenExpired()) {
      return from(keycloak.updateToken()).pipe(
        switchMap(() => {
          if (keycloak.token) {
            const refreshedRequest = req.clone({
              headers: new HttpHeaders({
                Authorization: `Bearer ${keycloak.token}`
              })
            });
            return next(refreshedRequest);
          } else {
            console.warn('Token still not available after refresh');
            return next(req);
          }
        })
      );
    } else {
      const authRequest = req.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${keycloak.token}`
        })
      });
      return next(authRequest);
    }
  }

  console.warn('No token is available');
  return next(req);
};

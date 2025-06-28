import {inject, Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {environment} from '../../../environments/environment.development';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {interval, switchMap} from 'rxjs';
import {fromPromise} from 'rxjs/internal/observable/innerFrom';
import {AuthModal} from '../../components/auth-modal/auth-modal';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  modalService = inject(NgbModal);

  private _keycloak: Keycloak | undefined;
  private authModalRef: NgbModalRef | undefined;
  private MIN_TOKEN_VALIDITY_MILLISECONDS = 10000;

  constructor() { }

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId,
      })
    }
    return this._keycloak;
  }

  async init(): Promise<void> {
    const isAuthenticated = await this.keycloak.init({
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + "/assets/silentCheckSsoRedirectUri.html"
    });
    if (isAuthenticated) {
      // this.initUpdateRefreshToken() // if you want the access token to be generated every interval
      if(this.authModalRef) {
        this.authModalRef.close();
      }
    } else {
      this.authModalRef = this.modalService.open(AuthModal, {
        centered: true,
        backdrop: 'static',
      });
    }
  }

  login(): void {
    this.keycloak.login();
  }

  logout(): Promise<void> {
    return this.keycloak.logout({
      redirectUri: environment.HOME_PAGE_URL,
    });
  }

  accountManagement() : Promise<void> {
    return this.keycloak.accountManagement();
  }

  get userId(): string {
    return this.keycloak?.tokenParsed?.sub as string;
  }

  get isAuthenticated(): boolean {
    return this.keycloak?.authenticated === true;
  }

  get isTokenValid(): boolean {
    return this.keycloak?.isTokenExpired();
  }

  get fullname(): string {
    return this.keycloak?.tokenParsed?.['name'] as string;
  }

  get profilePictureUrl(): string | null {
     if (this.keycloak?.tokenParsed?.['image_url']) {
        return this.keycloak?.tokenParsed?.['image_url'] as string;
     } else {
        return null;
     }
  }

  private initUpdateRefreshToken(): void {
    interval(this.MIN_TOKEN_VALIDITY_MILLISECONDS).pipe(
      switchMap(() => fromPromise(this.keycloak.updateToken(
        this.MIN_TOKEN_VALIDITY_MILLISECONDS
      )))
    ).subscribe({
      next: _ => {},
      error: err => console.error("Failed to refresh token: " + err)
    });
  }
}

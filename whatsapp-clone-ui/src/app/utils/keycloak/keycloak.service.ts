import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {interval, switchMap} from 'rxjs';
import {fromPromise} from 'rxjs/internal/observable/innerFrom';
import {environment} from '../../../environments/environment';
import {UserResponse} from '../../services/user/models/user-response';
import {UsersControllerService} from '../../services/user/services';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private authModalRef: NgbModalRef | undefined;
  private user: UserResponse | undefined;
  private MIN_TOKEN_VALIDITY_MILLISECONDS = 10000;

  constructor(
    private userService: UsersControllerService
  ) {}

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
      // this.authModalRef = this.modalService.open(AuthModal, {
      //   centered: true,
      //   backdrop: 'static',
      // });
      this.keycloak.login();
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

  get token(): string {
    return this.keycloak?.token!;
  }

  getMe(): void {
    this.userService.getUser().subscribe({
      next: res => {
        this.user = res.body;
      },
      error: () => {
        this.userService.syncMyUser().subscribe({
          next: () => {
            this.user = this.keycloakMe;
          }
        })
      }
    })


  }

  get me(): UserResponse {
    return this.user!;
  }

  get keycloakMe(): UserResponse {
    let claims = this.keycloak.tokenParsed!;
    return {
      id: this.userId,
      email: claims['email'],
      firstName: claims['given_name'],
      lastName: claims['family_name'],
      online: true,
      profilePictureReference: claims['image_url'],
    } as UserResponse;
  }

  get profilePictureReference(): string | null {
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

import {Component, inject} from '@angular/core';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';

@Component({
  selector: 'app-auth-modal',
  imports: [],
  templateUrl: './auth-modal.html',
  styleUrl: './auth-modal.scss'
})
export class AuthModal {

  private keycloakService = inject(KeycloakService);

  login() {
    this.keycloakService.login();
  }
}

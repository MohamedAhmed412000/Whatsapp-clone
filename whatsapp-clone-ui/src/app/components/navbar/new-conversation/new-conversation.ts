import {Component, EventEmitter, inject, OnDestroy, OnInit, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {FormsModule} from '@angular/forms';
import {ConversationSelector} from './conversation-selector/conversation-selector';
import {UsersControllerService} from '../../../services/user/services';
import {UserResponse} from '../../../services/user/models/user-response';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {ActionCreator} from './action-creator/action-creator';
import {NewGroupChat} from './new-group-chat/new-group-chat';
import {NewContactModal} from './new-contact-modal/new-contact-modal';
import {NgbActiveOffcanvas, NgbModal, NgbOffcanvas} from '@ng-bootstrap/ng-bootstrap';
import {OffcanvasTrackerService} from '../../../utils/offCanvasStack/offcanvas-tracker.service';
import {Toast} from '../../../shared/toast/toast';

@Component({
  selector: 'app-new-conversation',
  imports: [
    FaIconComponent,
    FormsModule,
    ConversationSelector,
    ActionCreator
  ],
  templateUrl: './new-conversation.html',
  styleUrl: './new-conversation.scss'
})
export class NewConversation implements OnInit, OnDestroy {

  private toastService = inject(Toast);
  private userSearchService = inject(UsersControllerService);
  private activeOffCanvas = inject(NgbActiveOffcanvas);
  private offcanvasTracker = inject(OffcanvasTrackerService);
  private offCanvasService = inject(NgbOffcanvas);
  private keycloakService = inject(KeycloakService);
  private modalService = inject(NgbModal);

  protected loadingSearch = true;
  protected query = null;
  protected meUser: UserResponse = this.keycloakService.keycloakMe;

  public usersResponses: Array<UserResponse> = [];

  @Output() public userChatSelected = new EventEmitter<UserResponse>();

  ngOnDestroy(): void {
    this.usersResponses = [];
  }

  ngOnInit(): void {
    this.onQueryChange("");
  }

  onQueryChange(query: any): void {
    this.loadingSearch = true;
    // if (query == null || query == "") {
    //   this.usersResponse = [];
    //   return;
    // }
    this.userSearchService.getUsers({
      q: query
    }).subscribe({
      next: response => {
        this.usersResponses = response.body ?? [];
        this.loadingSearch = false;
      },
      error: error => {
        console.log("Failed to load users: ", error);
        this.toastService.show("Error occurred while loading users, please try again", "DANGER");
        this.loadingSearch = false;
      }
    });
  }

  onCloseNav(): void {
    this.activeOffCanvas.close();
  }

  handleConversation(user: UserResponse): void {
    if (user) {
      this.userChatSelected.emit(user);
      this.offcanvasTracker.closeAll();
    }
  }

  openNewGroupChat= () => {
    const ref = this.offCanvasService.open(NewGroupChat, {
      position: "start",
      container: "#main",
      panelClass: "offcanvas",
    });

    this.offcanvasTracker.register(ref);
  };

  openNewContactModal(): void {
    this.offcanvasTracker.closeAll();
    this.modalService.open(NewContactModal, {
      centered: true
    });
  }
}

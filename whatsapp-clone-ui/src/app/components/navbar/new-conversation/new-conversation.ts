import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {UserControllerService} from '../../../services/services/user-controller.service';
import {NgbActiveOffcanvas} from '@ng-bootstrap/ng-bootstrap';
import {Toast} from '../../../shared/toast/toast';
import {UserResponse} from '../../../services/models/user-response';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {FormsModule} from '@angular/forms';
import {query} from '@angular/animations';
import {ConversationSelector} from './conversation-selector/conversation-selector';

@Component({
  selector: 'app-new-conversation',
  imports: [
    FaIconComponent,
    FormsModule,
    ConversationSelector
  ],
  templateUrl: './new-conversation.html',
  styleUrl: './new-conversation.scss'
})
export class NewConversation implements OnInit, OnDestroy {

  private userSearchService = inject(UserControllerService);
  private toastService = inject(Toast);
  private activeOffCanvas = inject(NgbActiveOffcanvas);

  protected loadingSearch = true;
  protected query = null;
  private search = {
    page: 0,
    size: 20,
    sort: ["firstName", "ASC"],
  }

  public usersResponse: Array<UserResponse> = [];

  ngOnDestroy(): void {
    this.usersResponse = [];
  }

  ngOnInit(): void {
    // this.onQueryChange("");
  }

  onQueryChange(query: any): void {
    this.loadingSearch = true;
    this.userSearchService.getUsers().subscribe({
      next: response => {
        this.usersResponse = response;
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

  handleConversation(userId: String): void {
    this.activeOffCanvas.close();
  }
}

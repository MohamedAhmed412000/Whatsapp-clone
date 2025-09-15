import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveOffcanvas, NgbOffcanvas} from '@ng-bootstrap/ng-bootstrap';
import {Toast} from '../../../../shared/toast/toast';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {FormsModule} from '@angular/forms';
import {UsersControllerService} from '../../../../services/user/services';
import {UserResponse} from '../../../../services/user/models/user-response';
import {ContactSelector} from './contact-selector/contact-selector';
import {GroupChatCreator} from './group-chat-creator/group-chat-creator';
import {OffcanvasTrackerService} from '../../../../utils/offCanvasStack/offcanvas-tracker.service';

@Component({
  selector: 'app-new-group-chat',
  imports: [
    FaIconComponent,
    FormsModule,
    ContactSelector
  ],
  templateUrl: './new-group-chat.html',
  styleUrl: './new-group-chat.scss'
})
export class NewGroupChat implements OnInit, OnDestroy {

  private userSearchService = inject(UsersControllerService);
  private toastService = inject(Toast);
  private offcanvasTracker = inject(OffcanvasTrackerService);
  private activeOffCanvas = inject(NgbActiveOffcanvas);
  private offCanvasService = inject(NgbOffcanvas);
  private totalContacts: number = 0;

  protected loadingSearch = true;
  protected query = null;
  protected selectedUserIds: Array<string> = [];

  public usersResponse: Array<UserResponse> = [];

  ngOnDestroy(): void {
    this.usersResponse = [];
  }

  ngOnInit(): void {
    this.onQueryChange("");
  }

  onUserSelectionChange(userId: string, checked: boolean) {
    if (checked) {
      if (!this.selectedUserIds.includes(userId)) {
        this.selectedUserIds.push(userId);
      }
    } else {
      this.selectedUserIds = this.selectedUserIds.filter(id => id !== userId);
    }
  }

  onQueryChange(query: any): void {
    this.loadingSearch = true;
    this.userSearchService.getUsers({
      q: query
    }).subscribe({
      next: response => {
        this.usersResponse = response.body ?? [];
        this.totalContacts = response.body ? response.body.length : 0;
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

  openCreateGroupChat(): void {
    const ref = this.offCanvasService.open(GroupChatCreator, {
      position: "start",
      container: "#main",
      panelClass: "offcanvas",
    });

    ref.componentInstance.userIds = this.selectedUserIds;
    ref.componentInstance.totalContacts = this.totalContacts;
    this.offcanvasTracker.register(ref);
  }

}

import {Component, Input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, NgForm} from '@angular/forms';
import {UserContactsControllerService, UsersControllerService} from '../../../../services/user/services';
import {ChatUserResponse} from '../../../../services/core/models/chat-user-response';
import {ChatResponse} from '../../../../services/core/models/chat-response';
import {KeycloakService} from '../../../../utils/keycloak/keycloak.service';
import {MediaUrlPipe} from '../../../../utils/media/media-url.pipe';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-auth-modal',
  imports: [
    FaIconComponent,
    FormsModule,
    MediaUrlPipe,
    AsyncPipe
  ],
  templateUrl: './edit-contact-modal.html',
  styleUrl: './edit-contact-modal.scss'
})
export class EditContactModal {
  @Input() chatUser!: ChatUserResponse;
  @Input() chat!: ChatResponse;

  protected previewUrl: string | ArrayBuffer | null = null;
  protected selectedFile: File | null = null;

  constructor(
    private userContactService: UserContactsControllerService,
    private userService: UsersControllerService,
    private modalService: NgbModal,
    private keycloakService: KeycloakService,
    public activeModal: NgbActiveModal
  ) {}

  updateUserContact(form: NgForm) {
    if (form.valid) {
      const { firstName, lastName } = form.value;

      if (this.isSelfChat()) {
        this.userService.updateUser({
          body: {
            firstName: firstName,
            lastName: lastName,
            profilePicture: this.selectedFile!
          }
        }).subscribe({
          next: res => {
            console.log("Is updated: " + res.body?.done);
            this.keycloakService.getMe();
          },
          error: err => {
            console.log(err);
          }
        })
      } else {
        this.userContactService.updateContact({
          'user-id': this.chatUser.id as string,
          body: {
            firstname: firstName,
            lastname: lastName
          }
        }).subscribe({
          next: res => {
            console.log("Is updated: " + res.body?.done);
          },
          error: err => {
            console.log(err);
          }
        });
      }

      this.chat = {
        ...this.chat,
        name: `${firstName} ${lastName}`
      };
    }
    this.modalService.dismissAll();
  }

  isSelfChat(): boolean {
  return Array.isArray(this.chat.receiversId) && this.chat.receiversId.length > 0
    ? this.chat.receiversId.every(userId => userId === this.keycloakService.userId)
    : false;
}

  closeModal() {
    this.activeModal.dismiss();
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = () => this.previewUrl = reader.result;
      reader.readAsDataURL(file);
    }
  }
}

import {Component, inject, Input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, NgForm} from '@angular/forms';
import {UserContactsControllerService} from '../../../../services/user/services';
import {ChatUserResponse} from '../../../../services/core/models/chat-user-response';
import {MediaUrlPipe} from '../../../../utils/media/media-url.pipe';

@Component({
  selector: 'app-auth-modal',
  imports: [
    FaIconComponent,
    FormsModule,
    MediaUrlPipe
  ],
  templateUrl: './edit-contact-modal.html',
  styleUrl: './edit-contact-modal.scss'
})
export class EditContactModal {
  @Input() chatUser!: ChatUserResponse;

  constructor(
    private userContactService: UserContactsControllerService,
    private modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {}

  updateUserContact(form: NgForm) {
    if (form.valid) {
      const { firstName, lastName } = form.value;

      this.userContactService.updateContact({
        'user-id': this.chatUser.id as string,
        body: {
          firstname: firstName,
          lastname: lastName
        }
      }).subscribe({
        next: res => {
          console.log("Is updated: " + res.body);
        },
        error: err => {
          console.log(err);
        }
      });
    }
    this.modalService.dismissAll();
  }

  closeModal() {
    this.activeModal.dismiss();
  }
}

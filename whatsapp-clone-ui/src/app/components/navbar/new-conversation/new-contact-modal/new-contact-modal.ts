import {Component, inject} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule, NgForm} from '@angular/forms';
import {UserContactsControllerService} from '../../../../services/user/services';

@Component({
  selector: 'app-auth-modal',
  imports: [
    FaIconComponent,
    FormsModule
  ],
  templateUrl: './new-contact-modal.html',
  styleUrl: './new-contact-modal.scss'
})
export class NewContactModal {

  private modalService = inject(NgbModal);
  private userContactService = inject(UserContactsControllerService);

  createUserContact(form: NgForm) {
    if (form.valid) {
      const { email, firstName, lastName } = form.value;

      this.userContactService.addNewContact({
        body: {
          email: email,
          firstname: firstName,
          lastname: lastName
        }
      }).subscribe({
        next: res => {
          console.log(res.body?.content);
        },
        error: err => {
          console.log(err);
        }
      });
    }
    this.modalService.dismissAll();
  }

  closeModal() {
    this.modalService.dismissAll();
  }
}

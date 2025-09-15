import {Component, inject, Input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgbActiveOffcanvas} from '@ng-bootstrap/ng-bootstrap';
import {ChatsControllerService} from '../../../../../services/core/services';
import {OffcanvasTrackerService} from '../../../../../utils/offCanvasStack/offcanvas-tracker.service';

@Component({
  selector: 'app-group-chat-creator',
  imports: [
    FaIconComponent,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './group-chat-creator.html',
  styleUrl: './group-chat-creator.scss'
})
export class GroupChatCreator {
  @Input() userIds!: string[];
  @Input() totalContacts!: number;

  protected groupName: string = '';
  protected previewUrl: string | ArrayBuffer | null = null;
  protected selectedFile: File | null = null;

  private offCanvasTracker = inject(OffcanvasTrackerService);
  private activeOffCanvas = inject(NgbActiveOffcanvas);
  private chatService = inject(ChatsControllerService);

  onCloseNav(): void {
    this.activeOffCanvas.close();
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

  resetImage(): void {
    this.previewUrl = null;
    this.selectedFile = null;
    const input = document.getElementById('groupIconInput') as HTMLInputElement;
    if (input) input.value = '';
  }

  createGroupChat(): void {
    this.chatService.createGroupChat({
      body: {
        name: this.groupName,
        receiversIds: this.userIds,
        file: this.selectedFile!
      }
    }).subscribe({
      next: res => {
        console.log(res!.body!.content);
      },
      error: err => {
        console.log(err);
      }
    })
    this.offCanvasTracker.closeAll();
  }

}

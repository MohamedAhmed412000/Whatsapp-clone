import {Component, OnInit} from '@angular/core';
import {FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {fontAwesomeIcons} from './shared/font-awesome-icons';
import {Navbar} from './components/navbar/navbar';
import {Toast} from './shared/toast/toast';
import {ToastInfo} from './shared/toast/toast-info.model';
import {NgbToast} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-root',
  imports: [Navbar, NgbToast],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'whatsapp-clone-ui';

  constructor(
    private toastService: Toast,
    private faIconLibrary: FaIconLibrary
  ) {}

  ngOnInit() {
    this.initFontAwesome();
  }

  allToasts(): ToastInfo[] {
    return this.toastService.toasts;
  }

  removeToast(toast: ToastInfo) {
    this.toastService.remove(toast);
  }

  private initFontAwesome() {
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }
}

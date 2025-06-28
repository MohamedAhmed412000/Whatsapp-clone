import {Injectable} from '@angular/core';
import {ToastInfo} from './toast-info.model';

@Injectable({
  providedIn: 'root'
})
export class Toast {

  toasts: ToastInfo[] = [];

  constructor() {}

  show(body: string, type: "SUCCESS" | "DANGER") {
    setTimeout(() => {
      const className =
        type === 'SUCCESS' ? 'bg-success text-light' : 'bg-danger text-light';
      const id = Date.now();
      this.toasts.push({ id, body, className });
    });
  }

  remove(toast: ToastInfo) {
    this.toasts = this.toasts.filter(toastToCompare => toastToCompare.id !== toast.id);
  }
}

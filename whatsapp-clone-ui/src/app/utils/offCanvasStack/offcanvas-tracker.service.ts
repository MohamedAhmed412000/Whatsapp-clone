import { Injectable } from '@angular/core';
import { NgbOffcanvasRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable({ providedIn: 'root' })
export class OffcanvasTrackerService {
  private stack: NgbOffcanvasRef[] = [];

  register(ref: NgbOffcanvasRef) {
    this.stack.push(ref);
  }

  closeAll() {
    while (this.stack.length) {
      this.stack.pop()?.close();
    }
  }
}

import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface GalleryImage {
  src: string;
  caption?: string;
}

@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  private _images = new BehaviorSubject<GalleryImage[]>([]);
  private _visible = new BehaviorSubject<boolean>(false);
  private _startIndex = new BehaviorSubject<number>(0);

  images$ = this._images.asObservable();
  visible$ = this._visible.asObservable();
  startIndex$ = this._startIndex.asObservable();

  open(images: GalleryImage[], startIndex = 0): void {
    this._images.next(images);
    this._startIndex.next(startIndex);
    this._visible.next(true);
  }

  close(): void {
    this._visible.next(false);
  }
}

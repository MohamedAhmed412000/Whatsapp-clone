import { Component, HostListener, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { GalleryService, GalleryImage } from '../gallery.service';
import {MediaUrlPipe} from '../../utils/media/media-url.pipe';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.html',
  imports: [
    MediaUrlPipe,
    AsyncPipe
  ],
  styleUrls: ['./gallery.scss']
})
export class GalleryComponent implements OnInit, OnDestroy {
  images: GalleryImage[] = [];
  visible = false;
  currentIndex = 0;

  private subs: Subscription[] = [];

  constructor(private galleryService: GalleryService) {}

  ngOnInit() {
    this.subs.push(
      this.galleryService.images$.subscribe(imgs => (this.images = imgs)),
      this.galleryService.visible$.subscribe(v => (this.visible = v)),
      this.galleryService.startIndex$.subscribe(i => (this.currentIndex = i))
    );
  }

  ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
  }

  next() {
    if (this.currentIndex < this.images.length - 1) this.currentIndex++;
  }

  prev() {
    if (this.currentIndex > 0) this.currentIndex--;
  }

  close() {
    this.galleryService.close();
  }

  @HostListener('document:keydown.arrowright')
  onRight() {
    if (this.visible) this.next();
  }

  @HostListener('document:keydown.arrowleft')
  onLeft() {
    if (this.visible) this.prev();
  }

  @HostListener('document:keydown.escape')
  onEsc() {
    if (this.visible) this.close();
  }
}

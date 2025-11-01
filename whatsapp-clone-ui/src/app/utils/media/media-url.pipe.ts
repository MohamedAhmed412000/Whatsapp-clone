import {environment} from '../../../environments/environment';
import {Pipe, PipeTransform} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';
import {catchError, from, of, startWith, switchMap} from 'rxjs';

@Pipe({
  name: 'mediaUrl',
  standalone: true
})
export class MediaUrlPipe implements PipeTransform {
  constructor(private sanitizer: DomSanitizer) {}

  transform(reference: string | null | undefined, getDirect: boolean = false) {
    if (!reference) {
      return of('/assets/images/image-not-available.png');
    }

    const url = `${environment.MEDIA_URL}${reference}`;
    if (getDirect) return of(this.sanitizer.bypassSecurityTrustUrl(url));

    return from(fetch(url, { method: 'HEAD' })).pipe(
      switchMap((response) => {
        const contentType = response.headers.get('content-type');
        if (contentType?.includes('application/json')) {
          return of('/assets/images/image-not-available.png');
        }
        return of(this.sanitizer.bypassSecurityTrustUrl(url));
      }),
      catchError(() => of('/assets/images/image-not-available.png')),
      startWith('/assets/images/image-not-available.png')
    );
  }
}

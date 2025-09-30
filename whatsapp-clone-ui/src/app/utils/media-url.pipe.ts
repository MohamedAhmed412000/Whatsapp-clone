import {environment} from '../../environments/environment';
import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'mediaUrl',
  standalone: true
})
export class MediaUrlPipe implements PipeTransform {
  transform(reference: string): string {
    return `${environment.MEDIA_URL}${reference}`;
  }
}

import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MediaService {
  generateUrl(reference: string): string {
    return `${environment.MEDIA_URL}${reference}`;
  }
}

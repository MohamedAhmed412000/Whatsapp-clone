import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'colorize',
  standalone: true,
})
export class ColorizePipe implements PipeTransform {
  transform(value: string, isSingleChat: boolean = false, isSelfMessage: boolean = true): string {
    const colors = [
      '#03A9F4', '#2fb872', '#b95d97', '#673AB7',
      '#7f66ff', '#a75e8c', '#0bbe90', '#a5b337',
      '#ffbc38', '#FF9800'
    ];

    if (isSingleChat) {
      if (isSelfMessage) {
        return colors[0];
      } else {
        return colors[1];
      }
    }

    let hash = 0;
    for (let i = 0; i < value.length; i++) {
      hash = (hash * 31 + value.charCodeAt(i)) | 0;
    }

    hash = Math.abs(hash);
    const colorIndex = hash % colors.length;
    return colors[colorIndex];
  }
}

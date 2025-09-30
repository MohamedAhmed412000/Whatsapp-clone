import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'relativeMessagesDate',
  standalone: true,
})
export class RelativeChatDatePipe implements PipeTransform {
  transform(value: Date | string | null | undefined): string {
    if (!value) return '';

    const date = new Date(value);
    const now = new Date();

    const isToday =
      date.getDate() === now.getDate() &&
      date.getMonth() === now.getMonth() &&
      date.getFullYear() === now.getFullYear();

    if (isToday) {
      return 'Today';
    }

    const yesterday = new Date(now);
    yesterday.setDate(now.getDate() - 1);

    const isYesterday =
      date.getDate() === yesterday.getDate() &&
      date.getMonth() === yesterday.getMonth() &&
      date.getFullYear() === yesterday.getFullYear();

    if (isYesterday) {
      return 'Yesterday';
    }

    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();

    return `${year}-${month}-${day}`;
  }
}

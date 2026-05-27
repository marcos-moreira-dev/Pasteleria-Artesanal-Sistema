import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class ShellFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly notifications = this.store.notifications;
  readonly notificationCounters = this.store.notificationCounters;
  readonly loading = this.store.loading;
  readonly error = this.store.error;
  readonly actionMessage = this.store.actionMessage;

  loadShellData() {
    this.store.loadAll();
  }

  reload() {
    this.store.loadAll();
  }

  markNotificationAsRead(notificationId: number) {
    this.store.markNotificationAsRead(notificationId);
  }

  archiveNotification(notificationId: number) {
    this.store.archiveNotification(notificationId);
  }

  archiveSelectedNotifications(notificationIds: number[]) {
    this.store.archiveSelectedNotifications(notificationIds);
  }

  archiveReadNotifications() {
    this.store.archiveReadNotifications();
  }
}

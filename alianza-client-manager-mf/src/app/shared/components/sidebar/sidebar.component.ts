import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MenuItem } from '../../interfaces/menu_item';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {


  @Input() collapsed: boolean = false;

  @Output() toggleSidebar = new EventEmitter<void>();

  menuItems: MenuItem[] = [
    { icon: 'pi pi-users', label: 'Clients', route: '/home/clients', active: true },
    { icon: 'pi pi-clock', label: 'Client look history', route: '/home/history', active: false },
    { icon: 'pi pi-lock', label: 'Emergency PIN configuration', route: '/home/pin-config', active: false },
    { icon: 'pi pi-history', label: 'Emergency PIN history', route: '/home/pin-history', active: false }
  ];

  onToggleSidebar() {
    this.toggleSidebar.emit();
  }

}

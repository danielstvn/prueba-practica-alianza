import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { PlaceholderComponent } from './shared/components/placeholder/placeholder.component';
import { ClientListComponent } from './pages/clients/client-list/client-list.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent,
    children: [
      {
        path: '',
        redirectTo: 'clients',
        pathMatch: 'full'
      },

      {
        path: 'clients',
        component: ClientListComponent
      },

      {
        path: 'history',
        component: PlaceholderComponent,
        data: { title: 'Client Look History', message: 'This feature is coming soon' }
      },
      {
        path: 'pin-config',
        component: PlaceholderComponent,
        data: { title: 'Emergency PIN Configuration', message: 'This feature is coming soon' }
      },
      {
        path: 'pin-history',
        component: PlaceholderComponent,
        data: { title: 'Emergency PIN History', message: 'This feature is coming soon' }
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];

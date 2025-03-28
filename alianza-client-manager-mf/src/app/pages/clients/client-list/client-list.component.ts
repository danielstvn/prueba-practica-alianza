import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ClientFormComponent } from '../components/client-form.component/client-form.component.component';
import { AdvancedSearchComponent } from '../components/advanced-search/advanced-search.component';
import { ToastModule } from 'primeng/toast';
import { Client } from '../interfaces/client';
import { ClientService } from '../services/client.service';
import { MessageService } from 'primeng/api';
import { ClientSearchCriteria } from '../interfaces/client_search_criteria';
import { FormMode } from '../enum/form_mode';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [    CommonModule,
    FormsModule,
    TableModule,
    TooltipModule,
    ButtonModule,
    InputTextModule,
    ToastModule,
    ClientFormComponent,
    AdvancedSearchComponent],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.scss'
})
export class ClientListComponent implements OnInit {

  clients: Client[] = [];

  loading: boolean = false;

  displayClientForm: boolean = false;

  displayAdvancedSearch: boolean = false;

  searchKey: string = '';

  selectedClient:Client | undefined = undefined;

  formMode = FormMode.CREATE;

  constructor(
    private clientService: ClientService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading = true;
    this.clientService.findAllByCriteria({criteria:{}}).subscribe({
      next: (data) => {
        this.clients = data;
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error loading clients' });
        this.loading = false;
        console.error('Error loading clients:', error);
      }
    });
  }

  searchClients(): void {
    if (!this.searchKey.trim()) {
      this.loadClients();
      return;
    }

    this.loading = true;
    this.clientService.findClientsBySharedKey(this.searchKey).subscribe({
      next: (data) => {
        this.clients = data;
        this.loading = false;
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error searching clients' });
        this.loading = false;
        console.error('Error searching clients:', error);
      }
    });
  }

  advancedSearch(criteria: ClientSearchCriteria): void {
    this.loading = true;
    this.clientService.findAllByCriteria({ criteria: criteria }).subscribe({
      next: (data) => {
        this.clients = data;
        this.loading = false;
        this.displayAdvancedSearch = false;
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Error in advanced search',
        });
        this.loading = false;
        console.error('Error in advanced search:', error);
      },
    });
  }

  openNewClientDialog(): void {
    this.displayClientForm = true;
    this.formMode = FormMode.CREATE;
  }

  openAdvancedSearchDialog(): void {
    this.displayAdvancedSearch = true;
  }

  onClientSaved(client: Client): void {
    this.displayClientForm = false;
    this.selectedClient = undefined;
    this.loadClients();
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Client successfully saved' });
  }

  exportToCSV(): void {
    this.clientService.exportToCsv().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'clients_export.csv';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);

        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'File exported successfully' });
      },
      error: (error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error exporting to CSV' });
        console.error('Error exporting to CSV:', error);
      }
    });
  }


  onEditClient(client:Client){
    this.selectedClient = client;
    this.formMode = FormMode.UPDATE;
    this.displayClientForm = true;
  }
}

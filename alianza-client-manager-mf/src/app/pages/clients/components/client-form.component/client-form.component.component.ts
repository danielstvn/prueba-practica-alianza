import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Client } from '../../interfaces/client';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { CalendarModule } from 'primeng/calendar';
import { ClientService } from '../../services/client.service';
import moment from 'moment-timezone';
import { FormMode } from '../../enum/form_mode';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    DialogModule,
    CalendarModule,
  ],
  templateUrl: './client-form.component.component.html',
  styleUrl: './client-form.component.component.scss',
})
export class ClientFormComponent implements OnInit {
  @Input() display: boolean = false;

  @Input() client?: Client;

  @Input() formMode?: FormMode;

  @Output() onClose = new EventEmitter<void>();

  @Output() onSave = new EventEmitter<Client>();

  clientForm!: FormGroup;

  submitting: boolean = false;

  dialogTitle: string = 'Create New Client';

  constructor(
    private fb: FormBuilder,
    private _clientService: ClientService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.clientForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      email: ['', [Validators.required, Validators.email]],
      startDate: [null, Validators.required],
      endDate: [null, Validators.required],
    });

    if (this.client && this.formMode == FormMode.UPDATE) {
      this.dialogTitle = 'Edit Client';
      this.clientForm.patchValue({
        name: this.client.name,
        phone: this.client.phone,
        email: this.client.email,
        startDate: new Date(this.client.startDate || ''),
        endDate: this.client.endDate ? new Date(this.client.endDate) : null,
      });
    }
  }

  closeDialog(): void {
    this.onClose.emit();
  }

  saveClient(): void {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const client = this.processData();

    if (this.formMode === FormMode.UPDATE) {
      this.updateClient(client);
    } else {
      this.createClient(client);
    }
  }

  private processData(): Client {
    const formData = this.clientForm.value;

    const clientData = {
      sharedKey: this.generateSharedKey(formData.name),
      name: formData.name,
      phone: formData.phone,
      email: formData.email,
      startDate: this.formatDateForZonedDateTime(formData.startDate),
      endDate: this.formatDateForZonedDateTime(formData.endDate),
    };

    if (this.formMode === FormMode.UPDATE) {
      return { ...this.client, ...clientData };
    }

    return clientData;
  }

  private updateClient(client: Client): void {
    this._clientService.updateClient(client).subscribe({
      next: (updatedClient) => {
        this.submitting = false;
        this.onSave.emit(updatedClient);
      },
      error: (error) => {
        console.error('Error updating client:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Name, email or phone number is already registered in the system.'
        });
        this.submitting = false;
      },
    });
  }

  private createClient(client: Client): void {
    this._clientService.createClient(client).subscribe({
      next: (savedClient) => {
        this.submitting = false;
        this.onSave.emit(savedClient);
      },
      error: (error) => {
        console.error('Error saving client:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Name, email or phone number is already registered in the system.'
        });
        this.submitting = false;
      },
    });
  }

  generateSharedKey(name: string): string {
    const cleanedName = name.trim().toLowerCase();

    const nameParts = cleanedName.split(' ');

    switch (nameParts.length) {
      case 1:
        return cleanedName;

      case 2:
        return nameParts[0].charAt(0) + nameParts[1];

      case 3:

        if (nameParts[1].length > 1) {
          return nameParts[0].charAt(0) + nameParts[1].charAt(0) + nameParts[2];
        }
        return nameParts[0].charAt(0) + nameParts[2];

      default:
        const firstInitial = nameParts[0].charAt(0);
        const middleParts = nameParts.slice(1, -1);
        const lastName = nameParts[nameParts.length - 1];

        return firstInitial +
              middleParts.map(part => part.charAt(0)).join('') +
              lastName;
}
  }

  private formatDateForZonedDateTime(date: Date | null): string | null {
    if (!date) return null;
    const momentDate = moment(date);
    return (
      momentDate.format('YYYY-MM-DDTHH:mm:ss.SSS') +
      moment().format('ZZ').replace(':', '')
    );
  }
}

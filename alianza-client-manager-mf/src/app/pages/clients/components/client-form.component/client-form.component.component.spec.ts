import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { DialogModule } from 'primeng/dialog';
import { CalendarModule } from 'primeng/calendar';

import { ClientFormComponent } from './client-form.component.component';
import { ClientService } from '../../services/client.service';
import { Client } from '../../interfaces/client';
import { FormMode } from '../../enum/form_mode';
import { of, throwError } from 'rxjs';
import { v4 as uuidv4 } from 'uuid';

describe('ClientFormComponent', () => {
  let component: ClientFormComponent;
  let fixture: ComponentFixture<ClientFormComponent>;
  let clientServiceSpy: jasmine.SpyObj<ClientService>;

  const clientId1 = uuidv4();
  const ISO_DATE_START = '2023-01-01T00:00:00.000-0500';
  const ISO_DATE_END = '2023-12-31T00:00:00.000-0500';

  beforeEach(async () => {
    // Crear un mock del ClientService
    clientServiceSpy = jasmine.createSpyObj('ClientService', ['createClient', 'updateClient']);

    await TestBed.configureTestingModule({
      imports: [
        ClientFormComponent,
        ReactiveFormsModule,
        DialogModule,
        CalendarModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: ClientService, useValue: clientServiceSpy },
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ClientFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    expect(component.clientForm).toBeDefined();
    expect(component.clientForm.get('name')?.value).toBe('');
    expect(component.clientForm.get('phone')?.value).toBe('');
    expect(component.clientForm.get('email')?.value).toBe('');
    expect(component.clientForm.get('startDate')?.value).toBeNull();
    expect(component.clientForm.get('endDate')?.value).toBeNull();
  });

  it('should have form validation', () => {
    const nameControl = component.clientForm.get('name');
    const phoneControl = component.clientForm.get('phone');
    const emailControl = component.clientForm.get('email');
    const startDateControl = component.clientForm.get('startDate');
    const endDateControl = component.clientForm.get('endDate');

    // Empty form should be invalid
    expect(component.clientForm.valid).toBeFalsy();

    // Set valid values
    nameControl?.setValue('John Doe');
    phoneControl?.setValue('1234567890');
    emailControl?.setValue('john@example.com');
    startDateControl?.setValue(new Date());
    endDateControl?.setValue(new Date());

    expect(component.clientForm.valid).toBeTruthy();

    // Test email validation
    emailControl?.setValue('invalid-email');
    expect(emailControl?.valid).toBeFalsy();

    // Test phone number validation
    phoneControl?.setValue('123'); // too short
    expect(phoneControl?.valid).toBeFalsy();
  });

  it('should populate form when in update mode with client data', () => {
    const mockClient: Client = {
      id: clientId1,
      sharedKey: 'jdoe',
      name: 'John Doe',
      email: 'john@example.com',
      phone: '1234567890',
      startDate: ISO_DATE_START,
      endDate: ISO_DATE_END
    };

    component.client = mockClient;
    component.formMode = FormMode.UPDATE;
    component.ngOnInit();

    expect(component.dialogTitle).toBe('Edit Client');
    expect(component.clientForm.get('name')?.value).toBe('John Doe');
    expect(component.clientForm.get('email')?.value).toBe('john@example.com');
    expect(component.clientForm.get('phone')?.value).toBe('1234567890');
    expect(component.clientForm.get('startDate')).toBeDefined();
    expect(component.clientForm.get('endDate')).toBeDefined();
  });

  it('should generate correct shared key for different name formats', () => {
    // Access private method using type assertion
    const component_any = component as any;

    expect(component_any.generateSharedKey('John Doe')).toBe('jdoe');
    expect(component_any.generateSharedKey('Jasmine Smith')).toBe('jsmith');
  });

  it('should emit close event when closeDialog is called', () => {
    spyOn(component.onClose, 'emit');
    component.closeDialog();
    expect(component.onClose.emit).toHaveBeenCalled();
  });

  it('should not save when form is invalid', () => {
    component.saveClient();
    expect(clientServiceSpy.createClient).not.toHaveBeenCalled();
    expect(clientServiceSpy.updateClient).not.toHaveBeenCalled();
  });

  it('should create client when form is valid in CREATE mode', () => {
    // Crear un objeto de respuesta completo con fechas ISO
    const responseObject: Client = {
      id: clientId1,
      sharedKey: 'jdoe',
      name: 'John Doe',
      email: 'john@example.com',
      phone: '1234567890',
      startDate: ISO_DATE_START,
      endDate: ISO_DATE_END
    };

    clientServiceSpy.createClient.and.returnValue(of(responseObject));

    spyOn(component.onSave, 'emit');

    // Fill the form
    component.clientForm.setValue({
      name: 'John Doe',
      phone: '1234567890',
      email: 'john@example.com',
      startDate: new Date(2023, 0, 1), // Enero 1, 2023
      endDate: new Date(2023, 11, 31)  // Diciembre 31, 2023
    });

    component.saveClient();

    expect(clientServiceSpy.createClient).toHaveBeenCalled();
    expect(component.onSave.emit).toHaveBeenCalled();
    expect(component.submitting).toBeFalse();
  });

  it('should update client when form is valid in UPDATE mode', () => {
    const existingClient: Client = {
      id: clientId1,
      sharedKey: 'jdoe',
      name: 'John Doe',
      email: 'john@example.com',
      phone: '1234567890',
      startDate: ISO_DATE_START,
      endDate: ISO_DATE_END
    };

    component.client = existingClient;
    component.formMode = FormMode.UPDATE;
    component.ngOnInit();

    const updatedClient: Client = {
      id: clientId1,
      sharedKey: 'jdoe',
      name: 'John Updated',
      email: 'updated@example.com',
      phone: '1234567890',
      startDate: ISO_DATE_START,
      endDate: ISO_DATE_END
    };

    clientServiceSpy.updateClient.and.returnValue(of(updatedClient));

    spyOn(component.onSave, 'emit');

    // Update the form
    component.clientForm.patchValue({
      name: 'John Updated',
      email: 'updated@example.com',
    });

    component.saveClient();

    expect(clientServiceSpy.updateClient).toHaveBeenCalled();
    expect(component.onSave.emit).toHaveBeenCalled();
    expect(component.submitting).toBeFalse();
  });

  it('should handle error when creating client fails', () => {
    clientServiceSpy.createClient.and.returnValue(throwError(() => new Error('Connection error')));

    spyOn(console, 'error');
    spyOn(component.onSave, 'emit');

    // Fill the form
    component.clientForm.setValue({
      name: 'John Doe',
      phone: '1234567890',
      email: 'john@example.com',
      startDate: new Date(2023, 0, 1),
      endDate: new Date(2023, 11, 31)
    });

    component.saveClient();

    expect(clientServiceSpy.createClient).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalled();
    expect(component.onSave.emit).not.toHaveBeenCalled();
    expect(component.submitting).toBeFalse();
  });

  it('should format dates correctly for backend', () => {
    // Access private method using type assertion
    const component_any = component as any;

    const testDate = new Date('2023-05-15T10:30:00');
    const formattedDate = component_any.formatDateForZonedDateTime(testDate);

    // Check that it starts with the correct date format
    expect(formattedDate).toMatch(/^2023-05-15T\d{2}:\d{2}:\d{2}\.\d{3}/);
    // Check that it ends with a timezone offset (numbers only)
    expect(formattedDate).toMatch(/[+-]\d{4}$/);
  });
});

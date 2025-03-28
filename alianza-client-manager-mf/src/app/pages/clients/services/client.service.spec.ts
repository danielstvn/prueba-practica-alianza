import { TestBed } from '@angular/core/testing';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ClientService } from './client.service';
import { HelpersService } from '../../../shared/services/helpers.service';
import { Client } from '../interfaces/client';
import { environment } from '../../../../environments/environment';
import { HttpParams } from '@angular/common/http';
import { v4 as uuidv4 } from 'uuid';

describe('ClientService', () => {

  let service: ClientService;

  let httpMock: HttpTestingController;

  let helpersServiceMock: jasmine.SpyObj<HelpersService>;

  const apiUrl = environment.apiClientsUrl;

  const clientId1 = uuidv4();
  const clientId2 = uuidv4();
  const clientId3 = uuidv4();

  beforeEach(() => {
    // Crear mock para HelpersService
    helpersServiceMock = jasmine.createSpyObj('HelpersService', ['createHttpParamsString']);

    TestBed.configureTestingModule({
      providers: [
        ClientService,
        { provide: HelpersService, useValue: helpersServiceMock },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(ClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should find clients by shared key', () => {
    const dummyClients: Client[] = [
      {
        id: clientId1,
        sharedKey: 'jgutierrez',
        name: 'Juliana Gutierrez',
        email: 'jgutierrez@gmail.com',
        phone: '3219876543',
        startDate: '2023-01-01',
        endDate: '2023-12-31',
        dataAdded: '2023-01-01'
      }
    ];

    service.findClientsBySharedKey('jgutierrez').subscribe(clients => {
      expect(clients.length).toBe(1);
      expect(clients[0].sharedKey).toBe('jgutierrez');
    });

    const req = httpMock.expectOne(`${apiUrl}/shared-key/jgutierrez`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyClients);
  });

  it('should find all clients by criteria', () => {
    const mockParams = new HttpParams().set('name', 'test').set('page', '0');
    const filter = { name: 'test', page: 0 };

    // Configurar mock del HelpersService
    helpersServiceMock.createHttpParamsString.and.returnValue(mockParams);

    const dummyClients: Client[] = [
      {
        id: clientId1,
        sharedKey: 'jgutierrez',
        name: 'Juliana Gutierrez',
        email: 'jgutierrez@gmail.com',
        phone: '3219876543',
        startDate: '2023-01-01',
        endDate: '2023-12-31',
        dataAdded: '2023-01-01'
      },
      {
        id: clientId2,
        sharedKey: 'mmartinez',
        name: 'Manuel Martinez',
        email: 'mmartinez@gmail.com',
        phone: '3219876543',
        startDate: '2023-01-01',
        endDate: '2023-12-31',
        dataAdded: '2023-01-01'
      }
    ];

    service.findAllByCriteria({ criteria: {} }).subscribe((clients) => {
      expect(clients.length).toBe(2);
      expect(clients).toEqual(dummyClients);
    });

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params === mockParams
    );
    expect(req.request.method).toBe('GET');
    req.flush(dummyClients);

    // Verificar que se llamó al método del helper
    expect(helpersServiceMock.createHttpParamsString).toHaveBeenCalledWith({
      criteria: {},
    });
  });

  it('should create a new client', () => {
    const newClient: Client = {
      sharedKey: 'jdoe',
      name: 'John Doe',
      email: 'jdoe@gmail.com',
      phone: '3219876543',
      startDate: '2023-01-01',
      endDate: '2023-12-31',
      dataAdded: '2023-01-01'
    };

    const createdClient: Client = {
      id: clientId3,
      ...newClient
    };

    service.createClient(newClient).subscribe(client => {
      expect(client).toEqual(createdClient);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newClient);
    req.flush(createdClient);
  });

  it('should update an existing client', () => {
    const existingClient: Client = {
      id: clientId1,
      sharedKey: 'jdoe',
      name: 'John Doe Updated',
      email: 'jdoe@gmail.com',
      phone: '3219876543',
      startDate: '2023-01-01',
      endDate: '2023-12-31',
      dataAdded: '2023-01-01'
    };

    service.updateClient(existingClient).subscribe(client => {
      expect(client).toEqual(existingClient);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(existingClient);
    req.flush(existingClient);
  });

  it('should export clients to CSV', () => {
    const mockBlob = new Blob(['id,name,email'], { type: 'text/csv' });

    service.exportToCsv().subscribe(response => {
      expect(response).toEqual(mockBlob);
    });

    const req = httpMock.expectOne(`${apiUrl}/export`);
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('blob');
    req.flush(mockBlob);
  });

  // Prueba para casos de error
  it('should handle errors when API calls fail', () => {
    service.findClientsBySharedKey('nonexistent').subscribe({
      next: () => fail('Expected an error, not clients'),
      error: error => {
        expect(error.status).toBe(404);
        expect(error.statusText).toBe('Not Found');
      }
    });

    const req = httpMock.expectOne(`${apiUrl}/shared-key/nonexistent`);
    req.flush('Not found', { status: 404, statusText: 'Not Found' });
  });
});

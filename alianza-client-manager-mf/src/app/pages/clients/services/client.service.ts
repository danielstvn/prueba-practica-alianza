import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../interfaces/client';
import { ClientSearchCriteria } from '../interfaces/client_search_criteria';
import { environment } from '../../../../environments/environment';
import { QueryFilter } from '../../../shared/interfaces/query_filter';
import { HelpersService } from '../../../shared/services/helpers.service';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private apiUrl = environment.apiClientsUrl;

  constructor(private http: HttpClient,private _helpersService: HelpersService) { }

  /**
   * Find clients by shared key
   *
   * @param sharedKey sheared key to find
   * @returns
   */
  findClientsBySharedKey(sharedKey: string): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.apiUrl}/shared-key/${sharedKey}`);
  }

  /**
   * Find all clients by criteria
   *
   * @param filter query filter
   * @returns
   */
  findAllByCriteria(filter: QueryFilter): Observable<Client[]> {
    let value = this._helpersService.createHttpParamsString(filter);

    return this.http.get<Client[]>(`${this.apiUrl}`, { params: value });
  }

  /**
 * Create a new Client
 *
 * @param client object Client
 * @returns
 */
  createClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  /**
   * Update Client
   *
   * @param client object Client
   * @returns
   */
  updateClient(client: Client): Observable<Client> {
    return this.http.put<Client>(this.apiUrl, client);
  }

  /**
  * Export clients to csv
  * @returns csv file
  */
  exportToCsv(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export`, {
      responseType: 'blob'
    });
  }
}

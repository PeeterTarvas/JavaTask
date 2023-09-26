import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ConnectionService} from "./connection.service";
import {Observable} from "rxjs";
import {CompanyDto} from "../dtos/company-dto";

/**
 *  Connection service for handling company related request making
 *  it extends ConnectionService which has all the needed methods for sending requests.
 */
@Injectable({
  providedIn: 'root'
})
export class CompanyWebRequestServiceService extends ConnectionService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
    this.apiEndPoint = this.apiEndPoint + "company/"
  }

  /**
   * Method that generates headers that are needed in the authentication in the back-end.
   * The main thing to get is token which is held in the sessionStorage.
   */
  getHeaders(): {headers: HttpHeaders} {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    });
    return  { headers: headers };
  }

  /**
   * This method overrides the put method from ConnectionService
   * to add headers and to add a custom type to be returned.
   * @param api_path part to be added.
   * @param body of the request.
   */
  override async put(api_path: string, body: any): Promise<Observable<CompanyDto>> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.put(api_path, body, options);
  }

  /**
   * This method overrides the get method from ConnectionService
   * to add headers and a custom type to be returned.
   * @param api_path part to be added.
   * @param body of the request.
   */
  override async get(api_path: string, body?: any): Promise<Observable<CompanyDto>> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.get(api_path, options);
  }

  /**
   * This method overrides the post method from ConnectionService
   * to add headers and a custom type to be returned.
   * @param api_path part to be added.
   * @param body of the request.
   */
  override async post(api_path: string, body: any): Promise<any> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.post(api_path, body, options);
  }
}

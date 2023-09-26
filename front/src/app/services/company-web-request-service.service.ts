import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ConnectionService} from "./connection.service";
import {Observable} from "rxjs";
import {UserDto} from "../dtos/user-dto";
import {LoginResponse} from "../dtos/login-response";
import {CompanyDto} from "../dtos/company-dto";

@Injectable({
  providedIn: 'root'
})
export class CompanyWebRequestServiceService extends ConnectionService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
    this.apiEndPoint = this.apiEndPoint + "company/"
  }

  getHeaders(): {headers: HttpHeaders} {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    });
    return  { headers: headers };
  }

  override async put(api_path: string, body: any): Promise<Observable<CompanyDto>> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.put(api_path, body, options);
  }

  override async get(api_path: string, body?: any): Promise<Observable<CompanyDto>> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.get(api_path, options);
  }

  override async post(api_path: string, body: any): Promise<any> {
    const options: {headers: HttpHeaders} = this.getHeaders();
    return super.post(api_path, body, options);
  }
}

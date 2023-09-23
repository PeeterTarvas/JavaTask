import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
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


  override async get(api_path: string, body?: any): Promise<Observable<CompanyDto>> {
    return await super.get(api_path, body);
  }

  override post(api_path: string, dto: any): Promise<any> {
    return super.post(api_path, dto);
  }
}

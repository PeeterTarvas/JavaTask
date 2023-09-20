import { Injectable } from '@angular/core';
import {ConnectionService} from "./connection.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../dtos/user-dto";
import {LoginResponse} from "../dtos/login-response";

@Injectable({
  providedIn: 'root'
})
export class UserWebRequestServiceService extends ConnectionService{

  constructor(httpClient: HttpClient) {
    super(httpClient);
    this.apiEndPoint = this.apiEndPoint + "user/"
  }


  override async get(api_path: string, body: any): Promise<Observable<LoginResponse>> {
    return await super.get(api_path, body);
  }

  override async post(api_path: string, dto: any): Promise<any> {
    return super.post(api_path, dto);
  }
}

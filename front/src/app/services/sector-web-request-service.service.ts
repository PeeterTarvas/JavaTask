import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../dtos/user-dto";
import {LoginResponse} from "../dtos/login-response";
import {ConnectionService} from "./connection.service";
import {SectorDto} from "../dtos/sector-dto";

@Injectable({
  providedIn: 'root'
})
export class SectorWebRequestServiceService extends ConnectionService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
    this.apiEndPoint = this.apiEndPoint + "sector/"
  }


  override async get(api_path: string): Promise<SectorDto[]> {
    let get = this.apiEndPoint + api_path;
    return new Promise<SectorDto[]>((resolve) => {
      this.httpClient.get(get).subscribe(
        (response: any) => {
          return resolve(response);
        }
      )
    });
  }

}

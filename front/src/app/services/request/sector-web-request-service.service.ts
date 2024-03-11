import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../../dtos/user-dto";
import {LoginResponse} from "../../dtos/login-response";
import {ConnectionService} from "./connection.service";
import {SectorDto} from "../../dtos/sector-dto";

/**
 *  Connection service for handling sector related request making
 *  it extends ConnectionService which has all the needed methods for sending requests.
 */
@Injectable({
  providedIn: 'root'
})
export class SectorWebRequestServiceService extends ConnectionService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
    this.apiEndPoint = this.apiEndPoint + "sector/"
  }


  /**
   * This method overrides the default get method from the parent class.
   * It's main function is to get all the sectors form the back-end so that they could be shown to the user.
   * @param api_path
   */
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

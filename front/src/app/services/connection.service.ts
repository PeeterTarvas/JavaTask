import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CompanyDto} from "../dtos/company-dto";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  apiEndPoint: string = 'http://localhost:8080/';

  constructor(private httpClient: HttpClient) {
    this.httpClient = httpClient;

  }

  public get(api_path: String) {
    let get = this.apiEndPoint + api_path;
    return this.httpClient.get(get);
  }

  public post(api_path: String, companyDto: CompanyDto) {
    let post = this.apiEndPoint + api_path;
    let res = this.httpClient.post(post, companyDto,  { withCredentials: true })
    res.subscribe(r => console.log(r))
  }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../dtos/user-dto";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  protected apiEndPoint: string = 'http://localhost:8080/';

  constructor(protected httpClient: HttpClient) {
  }

  async get(api_path: string, body?: any): Promise<any> {
    let get = this.apiEndPoint + api_path;
    if (body) {
      return this.httpClient.get(get, body);
    }
    return this.httpClient.get(get);
  }


  async put(api_path: string, body: any, options?: {headers: HttpHeaders}) {
    const put = this.apiEndPoint + api_path;
    return new Promise<any>((resolve, reject) => {
      this.httpClient.put(put, body, options).subscribe(
        (response) => {
          resolve(response);
        },
        (error) => {
          reject(error);
        }
      );
    });
  }

  async post(api_path: string, body: any, options?: {headers: HttpHeaders}): Promise<any> {
    const post = this.apiEndPoint + api_path;
    return new Promise<any>((resolve, reject) => {
      this.httpClient.post(post, body, options).subscribe(
        (response) => {
          resolve(response);
        },
        (error) => {
          reject(error);
        }
      );
    });
  }
}

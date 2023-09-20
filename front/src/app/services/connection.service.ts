import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../dtos/user-dto";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  protected apiEndPoint: string = 'http://localhost:8080/';

  constructor(protected httpClient: HttpClient) {
  }

  async get(api_path: string, body: any): Promise<any> {
    let get = this.apiEndPoint + api_path;
    return this.httpClient.get(get, body);
  }

  async post(api_path: string, dto: any): Promise<any> {
    const post = this.apiEndPoint + api_path;
    return new Promise<any>((resolve, reject) => {
      this.httpClient.post(post, dto).subscribe(
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

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserDto} from "../../dtos/user-dto";

/**
 * This class is for connecting to the back-end.
 * This class is extended by all the other connection classes that handle different request needed to be sent.
 */
@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  protected apiEndPoint: string = 'http://localhost:8080/';

  constructor(protected httpClient: HttpClient) {
  }

  /**
   * Method for sending get requests for the back-end.
   * @param api_path that has the endpoint path that this method will call.
   * @param body that holds the details that are to be sent.
   */
  async get(api_path: string, body?: any): Promise<any> {
    let get = this.apiEndPoint + api_path;
    if (body) {
      return this.httpClient.get(get, body);
    }
    return this.httpClient.get(get);
  }

  /**
   * Method for sending put requests - for updating.
   * @param api_path that has the endpoint path that this method will call.
   * @param body that holds the details that are to be sent.
   * @param options will contain the headers that are to be sent with the request.
   */
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

  /**
   * Method for sending put requests - for saving data for the user.
   * @param api_path that has the endpoint path that this method will call.
   * @param body that holds the details that are to be sent.
   * @param options will contain the headers that are to be sent with the request.
   */
  async post(api_path: string, body: any, options?: {headers: HttpHeaders}): Promise<any> {
    const post = this.apiEndPoint + api_path;
    return new Promise<any>((resolve, reject) => {
      this.httpClient.post(post, body, options).subscribe(
        (response) => {
          resolve(response);
        },
        (error) => {
          console.log(error)
          reject(error);
        }
      );
    });
  }
}

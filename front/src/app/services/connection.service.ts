import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ConnectionService {

  protected apiEndPoint: string = 'http://localhost:8080/';

  constructor(protected httpClient: HttpClient) {
  }

  public get(api_path: string, body: any): Observable<any> {
    let get = this.apiEndPoint + api_path;
    return this.httpClient.get(get, body);
  }

  public post(api_path: string, dto: any): any {
    let post = this.apiEndPoint + api_path;
    let resp: any;
    this.httpClient.post(post, dto, {withCredentials: true}).subscribe(r => resp = r)
    return resp;
  }
}

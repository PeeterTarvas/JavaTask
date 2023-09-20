import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {LoginResponse} from "../dtos/login-response";
import {UserDto} from "../dtos/user-dto";
import {UserWebRequestServiceService} from "./user-web-request-service.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private currentUserSubject: BehaviorSubject<LoginResponse | undefined>;
  public currentUser: Observable<LoginResponse | undefined>;
  private userService: UserWebRequestServiceService;

  constructor( userService: UserWebRequestServiceService) {
    const currentUserString = localStorage.getItem('currentUser');
    const currentUserJson = currentUserString ? JSON.parse(currentUserString) : undefined;
    this.currentUserSubject = new BehaviorSubject(currentUserJson);
    this.currentUser = this.currentUserSubject.asObservable();
    this.userService = userService;
  }

  public get getCurrentUserValue(): LoginResponse | undefined {
    return this.currentUserSubject ? this.currentUserSubject.value : undefined
  }

  async login(loginRequest: UserDto): Promise<LoginResponse | Error> {
    try {
      const loginResponse: LoginResponse = await this.userService.post("login", loginRequest);
      if (loginResponse && loginResponse.token) {
        sessionStorage.setItem('username', loginResponse.username);
        sessionStorage.setItem('token', loginResponse.token);
        this.currentUserSubject.next(loginResponse);
        return loginResponse;
      } else {
        throw new Error("Bad details");
      }
    } catch (error) {
      return new Error("Login failed");
    }
  }


  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    this.currentUserSubject.next(undefined);
  }

}

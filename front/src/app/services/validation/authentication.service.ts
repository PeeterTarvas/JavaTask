import { Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {LoginResponse} from "../../dtos/login-response";
import {UserDto} from "../../dtos/user-dto";
import {UserWebRequestServiceService} from "../request/user/user-web-request-service.service";

/**
 * This class is for handling user authentication and logging in.
 */
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

  /**
   * Method for user logging-in.
   * First it sends a login request to the back end, the method is post because
   * it's easier to send the user details as a body I know it should be get. User details are sent with the UserDto.
   * If the user exists then it is returned by the back-end as a LoginResponse.
   * The LoginResponse contains the users username and token, which are used for authenticating requests.
   * These attributes are saved to the sessionStorage to be easily used later.
   * It returns the response indicating that the login has been successful.
   * It is not successful then it returns an error.
   * @param loginRequest
   */
  async login(loginRequest: UserDto): Promise<LoginResponse | Error> {
    const loginResponse: LoginResponse = await this.userService.post("login", loginRequest);
    if (loginResponse && loginResponse.token) {
        sessionStorage.setItem('username', loginResponse.username);
        sessionStorage.setItem('token', loginResponse.token);
        this.currentUserSubject.next(loginResponse);
        return loginResponse;
      }
      throw new Error("Login failed");

  }

  /**
   * This is for enabling logout, basically removes username and storage from storage.
   */
  logout() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('token');
    this.currentUserSubject.next(undefined);
  }

}

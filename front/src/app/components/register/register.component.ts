import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/validation/authentication.service";
import {Router} from "@angular/router";
import {UserDto} from "../../dtos/user-dto";
import {UserWebRequestServiceService} from "../../services/request/user-web-request-service.service";

/**
 * This component meant for the user to register their account.
 */
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  constructor(private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService,
              private router: Router,
              private userService: UserWebRequestServiceService) {
  }

  ngOnInit(): void {
    if (this.authenticationService.getCurrentUserValue) {
      this.router.navigate(['/form']);
    }
  }

  /**
   * This method is for submitting a new account with the username and password that the user has provided.
   * If the user with that username already exists or no details were provided then the registering fails.
   * Else the user is prompted to the login page where they can login to their account.
   *
   */
  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    const {username, password} = this.registerForm.value
    const registerRequest: UserDto = {username: username, password: password};
    this.userService.post("register",registerRequest).then(
      (response) => {
        this.router.navigate(['/login'])
      });
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.registerForm.controls[controlName].hasError(errorName);
  }

}

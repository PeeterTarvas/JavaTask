import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/validation/authentication.service";
import {UserDto} from "../../dtos/user-dto";

/**
 * This is the component for handling the users login.
 * It is an input form that requires the users username and a password with which they are registered.
 * This is also first component that the user will see when they view the app.
 * If the user want there is also a register link to the Register component where they can register their account.
 */
@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {


  loginForm: FormGroup = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });
  errorMessage: string | undefined;

  constructor(private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService,
              private router: Router) {
  }

  /**
   * If the user already has logged in then, and they are on this page then they will be forwarded to the
   * InputForm component where they can edit his/her company.
   */
  ngOnInit(): void {
    if (this.authenticationService.getCurrentUserValue) {
      this.router.navigate(['/form']);
    }
  }

  /**
   * This is the main method that logs-in the user if they have provided their account details.
   * If the form is invalid then it does nothing.
   * Else it constructs the UserDto object with which the login request is made through the Authentication service.
   * If the login is successful then the user will be forwarded to the InputForm component where they
   * can see and edit their company details.
   * Else an error message is displayed on the screen.
   */
  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    const {username, password} = this.loginForm.value
    const loginRequest: UserDto = {username: username, password: password}
    this.authenticationService.login(loginRequest).then((response) => {
      console.log(response)
      if (response instanceof Error) {
        this.errorMessage = response.message;
      } else {
        this.router.navigate(['form']);
      }
    }
    );
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.loginForm.controls[controlName].hasError(errorName);
  }


}

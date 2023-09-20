import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {UserDto} from "../../dtos/user-dto";
import {first} from "rxjs";
import {LoginResponse} from "../../dtos/login-response";

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

  ngOnInit(): void {
    if (this.authenticationService.getCurrentUserValue) {
      this.router.navigate(['/form']);
    }
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    const {username, password} = this.loginForm.value
    const loginRequest: UserDto = {username: username, password: password}
    this.authenticationService.login(loginRequest).then((response) =>
    {
      if (response instanceof Error) {
        // Display the error message on the screen
        // For example, you can set it to a property in your component
        this.errorMessage = response.message;
      } else {
        // Login successful, navigate to the 'form' page
        this.router.navigate(['form']);
      }
    }
    );
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.loginForm.controls[controlName].hasError(errorName);
  }


}

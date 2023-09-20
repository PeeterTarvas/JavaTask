import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {UserDto} from "../../dtos/user-dto";
import {first} from "rxjs";
import {LoginResponse} from "../../dtos/login-response";
import {UserWebRequestServiceService} from "../../services/user-web-request-service.service";

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

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    const {username, password} = this.registerForm.value
    const registerRequest: UserDto = {username: username, password: password};
    console.log(registerRequest)
    this.userService.post("register",registerRequest).then(
      (response) => {
        this.router.navigate(['/login'])
      });
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.registerForm.controls[controlName].hasError(errorName);
  }

}

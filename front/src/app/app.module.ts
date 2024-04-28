import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SelectorSelectComponent } from './components/form-page/selector-select/selector-select.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { CompanyNameBoxComponent } from './components/form-page/company-name-box/company-name-box.component';
import { InputFormComponent } from './components/form-page/input-form/input-form.component';
import { TermsComponent } from './components/form-page/terms/terms.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { AppRoutingModule } from './app-routing.module';
import {Routes} from "@angular/router";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import { RegisterPageComponent } from './components/register-page/register-page.component';
import {MatRadioModule} from "@angular/material/radio";
import {MatOptionModule} from "@angular/material/core";


@NgModule({
  declarations: [
    AppComponent,
    SelectorSelectComponent,
    CompanyNameBoxComponent,
    InputFormComponent,
    TermsComponent,
    LoginPageComponent,
    RegisterPageComponent
  ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatCardModule,
        ReactiveFormsModule,
        MatInputModule,
        MatButtonModule,
        MatRadioModule,
        MatOptionModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {


}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SelectorSelectComponent } from './components/selector-select/selector-select.component';
import {FormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { CompanyNameBoxComponent } from './components/company-name-box/company-name-box.component';
import { InputFormComponent } from './components/input-form/input-form.component';
import { TermsComponent } from './components/terms/terms.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { AppRoutingModule } from './app-routing.module';
import {Routes} from "@angular/router";


@NgModule({
  declarations: [
    AppComponent,
    SelectorSelectComponent,
    CompanyNameBoxComponent,
    InputFormComponent,
    TermsComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {


}

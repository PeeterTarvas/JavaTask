import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { SelectorSelectComponent } from './selector-select/selector-select.component';
import {FormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { CompanyNameBoxComponent } from './company-name-box/company-name-box.component';
import { InputFormComponent } from './input-form/input-form.component';
import { TermsComponent } from './terms/terms.component';


@NgModule({
  declarations: [
    AppComponent,
    SelectorSelectComponent,
    CompanyNameBoxComponent,
    InputFormComponent,
    TermsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

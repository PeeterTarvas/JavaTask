import { Component } from '@angular/core';

@Component({
  selector: 'app-company-name-box',
  templateUrl: './company-name-box.component.html',
  styleUrls: ['./company-name-box.component.css']
})
export class CompanyNameBoxComponent {

    protected name: string = '';

    getName() {
      return this.name;
    }
}

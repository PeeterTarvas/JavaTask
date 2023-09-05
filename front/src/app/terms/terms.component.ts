import { Component } from '@angular/core';

@Component({
  selector: 'app-terms',
  templateUrl: './terms.component.html',
  styleUrls: ['./terms.component.css']
})
export class TermsComponent {
  isChecked: boolean = false;  // Boolean flag
  doSomething() {
    if (this.isChecked) {
      console.log('checkbox is checked');
    } else {
      console.log('checkbox is unchecked');
    }
  }
}

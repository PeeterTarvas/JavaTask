import {Component, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-company-name-box',
  templateUrl: './company-name-box.component.html',
  styleUrls: ['./company-name-box.component.css']
})
export class CompanyNameBoxComponent {

  @Output() nameEvent: EventEmitter<string> = new EventEmitter();
  name: string = '';
    getName() {
      this.nameEvent.emit(this.name)
    }
}

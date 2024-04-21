import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-terms',
  templateUrl: './terms.component.html',
  styleUrls: ['./terms.component.css']
})
export class TermsComponent {
  @Input() isChecked: boolean = false;  // Boolean flag

  @Output() isCheckedEvent: EventEmitter<boolean> = new EventEmitter();
  getIsChecked() {
    this.isCheckedEvent.emit(this.isChecked)
  }
}

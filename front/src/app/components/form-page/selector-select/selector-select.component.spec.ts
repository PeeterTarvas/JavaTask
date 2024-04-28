import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectorSelectComponent } from './selector-select.component';

describe('SelectorSelectComponent', () => {
  let component: SelectorSelectComponent;
  let fixture: ComponentFixture<SelectorSelectComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectorSelectComponent]
    });
    fixture = TestBed.createComponent(SelectorSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

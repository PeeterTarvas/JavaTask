import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyNameBoxComponent } from './company-name-box.component';

describe('CompanyNameBoxComponent', () => {
  let component: CompanyNameBoxComponent;
  let fixture: ComponentFixture<CompanyNameBoxComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyNameBoxComponent]
    });
    fixture = TestBed.createComponent(CompanyNameBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

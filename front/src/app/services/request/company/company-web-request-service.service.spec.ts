import { TestBed } from '@angular/core/testing';

import { CompanyWebRequestServiceService } from './company-web-request-service.service';

describe('CompanyWebRequestServiceService', () => {
  let service: CompanyWebRequestServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompanyWebRequestServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

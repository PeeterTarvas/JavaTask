import { TestBed } from '@angular/core/testing';

import { SectorWebRequestServiceService } from './sector-web-request-service.service';

describe('SectorWebRequestServiceService', () => {
  let service: SectorWebRequestServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SectorWebRequestServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

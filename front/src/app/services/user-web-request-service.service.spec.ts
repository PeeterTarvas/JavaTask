import { TestBed } from '@angular/core/testing';

import { UserWebRequestServiceService } from './user-web-request-service.service';

describe('UserWebRequestServiceService', () => {
  let service: UserWebRequestServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserWebRequestServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

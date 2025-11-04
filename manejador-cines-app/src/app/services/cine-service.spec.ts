import { TestBed } from '@angular/core/testing';

import { CineService } from './cine-service';

describe('CineService', () => {
  let service: CineService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CineService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

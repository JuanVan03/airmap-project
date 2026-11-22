import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { TransporteService } from './transporte.service';

describe('TransporteService', () => {
  let service: TransporteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(TransporteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

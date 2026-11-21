import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { AsistenteService } from './asistente.service';

describe('AsistenteService', () => {
  let service: AsistenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()],
    });
    service = TestBed.inject(AsistenteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { PreferenciasService } from './preferencias.service';

describe('PreferenciasService', () => {
  let service: PreferenciasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideRouter([])],
    });
    service = TestBed.inject(PreferenciasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

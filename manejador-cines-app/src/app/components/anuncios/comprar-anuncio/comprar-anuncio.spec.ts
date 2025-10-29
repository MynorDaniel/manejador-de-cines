import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComprarAnuncio } from './comprar-anuncio';

describe('ComprarAnuncio', () => {
  let component: ComprarAnuncio;
  let fixture: ComponentFixture<ComprarAnuncio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComprarAnuncio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComprarAnuncio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

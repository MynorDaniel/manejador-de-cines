import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreciosTipoComponent } from './precios-tipo-component';

describe('PreciosTipoComponent', () => {
  let component: PreciosTipoComponent;
  let fixture: ComponentFixture<PreciosTipoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreciosTipoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreciosTipoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

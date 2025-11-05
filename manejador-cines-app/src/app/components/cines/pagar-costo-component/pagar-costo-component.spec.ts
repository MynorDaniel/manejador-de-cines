import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagarCostoComponent } from './pagar-costo-component';

describe('PagarCostoComponent', () => {
  let component: PagarCostoComponent;
  let fixture: ComponentFixture<PagarCostoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagarCostoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagarCostoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

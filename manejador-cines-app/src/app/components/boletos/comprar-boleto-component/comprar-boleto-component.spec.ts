import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComprarBoletoComponent } from './comprar-boleto-component';

describe('ComprarBoletoComponent', () => {
  let component: ComprarBoletoComponent;
  let fixture: ComponentFixture<ComprarBoletoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComprarBoletoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComprarBoletoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

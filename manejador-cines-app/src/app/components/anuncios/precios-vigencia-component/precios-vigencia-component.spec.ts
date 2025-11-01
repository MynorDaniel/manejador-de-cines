import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreciosVigenciaComponent } from './precios-vigencia-component';

describe('PreciosVigenciaComponent', () => {
  let component: PreciosVigenciaComponent;
  let fixture: ComponentFixture<PreciosVigenciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreciosVigenciaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreciosVigenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

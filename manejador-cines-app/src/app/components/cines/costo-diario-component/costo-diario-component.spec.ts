import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CostoDiarioComponent } from './costo-diario-component';

describe('CostoDiarioComponent', () => {
  let component: CostoDiarioComponent;
  let fixture: ComponentFixture<CostoDiarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CostoDiarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CostoDiarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

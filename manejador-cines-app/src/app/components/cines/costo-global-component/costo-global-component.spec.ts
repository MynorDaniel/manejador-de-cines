import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CostoGlobalComponent } from './costo-global-component';

describe('CostoGlobalComponent', () => {
  let component: CostoGlobalComponent;
  let fixture: ComponentFixture<CostoGlobalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CostoGlobalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CostoGlobalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

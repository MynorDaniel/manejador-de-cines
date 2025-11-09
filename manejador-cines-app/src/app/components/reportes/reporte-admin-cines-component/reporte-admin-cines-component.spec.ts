import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteAdminCinesComponent } from './reporte-admin-cines-component';

describe('ReporteAdminCinesComponent', () => {
  let component: ReporteAdminCinesComponent;
  let fixture: ComponentFixture<ReporteAdminCinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteAdminCinesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporteAdminCinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

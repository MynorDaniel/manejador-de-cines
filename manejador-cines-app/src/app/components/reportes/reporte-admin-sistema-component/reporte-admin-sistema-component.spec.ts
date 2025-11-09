import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteAdminSistemaComponent } from './reporte-admin-sistema-component';

describe('ReporteAdminSistemaComponent', () => {
  let component: ReporteAdminSistemaComponent;
  let fixture: ComponentFixture<ReporteAdminSistemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporteAdminSistemaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporteAdminSistemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

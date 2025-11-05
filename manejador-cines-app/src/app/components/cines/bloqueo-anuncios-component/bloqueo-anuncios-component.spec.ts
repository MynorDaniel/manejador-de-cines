import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BloqueoAnunciosComponent } from './bloqueo-anuncios-component';

describe('BloqueoAnunciosComponent', () => {
  let component: BloqueoAnunciosComponent;
  let fixture: ComponentFixture<BloqueoAnunciosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BloqueoAnunciosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BloqueoAnunciosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

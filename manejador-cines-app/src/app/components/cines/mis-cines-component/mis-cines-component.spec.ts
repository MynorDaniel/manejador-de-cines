import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisCinesComponent } from './mis-cines-component';

describe('MisCinesComponent', () => {
  let component: MisCinesComponent;
  let fixture: ComponentFixture<MisCinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisCinesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisCinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

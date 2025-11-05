import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerCinesComponent } from './ver-cines-component';

describe('VerCinesComponent', () => {
  let component: VerCinesComponent;
  let fixture: ComponentFixture<VerCinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerCinesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerCinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

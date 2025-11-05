import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerCineComponent } from './ver-cine-component';

describe('VerCineComponent', () => {
  let component: VerCineComponent;
  let fixture: ComponentFixture<VerCineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerCineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerCineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

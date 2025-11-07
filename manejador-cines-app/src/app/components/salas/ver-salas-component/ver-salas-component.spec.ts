import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerSalasComponent } from './ver-salas-component';

describe('VerSalasComponent', () => {
  let component: VerSalasComponent;
  let fixture: ComponentFixture<VerSalasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerSalasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerSalasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

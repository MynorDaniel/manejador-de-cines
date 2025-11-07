import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearProyeccionComponent } from './crear-proyeccion-component';

describe('CrearProyeccionComponent', () => {
  let component: CrearProyeccionComponent;
  let fixture: ComponentFixture<CrearProyeccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearProyeccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearProyeccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

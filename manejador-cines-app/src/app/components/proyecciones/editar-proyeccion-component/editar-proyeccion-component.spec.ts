import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarProyeccionComponent } from './editar-proyeccion-component';

describe('EditarProyeccionComponent', () => {
  let component: EditarProyeccionComponent;
  let fixture: ComponentFixture<EditarProyeccionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarProyeccionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarProyeccionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

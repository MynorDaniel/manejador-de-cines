import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarAnuncio } from './editar-anuncio';

describe('EditarAnuncio', () => {
  let component: EditarAnuncio;
  let fixture: ComponentFixture<EditarAnuncio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarAnuncio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarAnuncio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

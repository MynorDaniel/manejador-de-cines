import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnunciosWidget } from './anuncios-widget';

describe('AnunciosWidget', () => {
  let component: AnunciosWidget;
  let fixture: ComponentFixture<AnunciosWidget>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnunciosWidget]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnunciosWidget);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNotebookModal } from './create-notebook.modal';

describe('CreateNotebookModal', () => {
  let component: CreateNotebookModal;
  let fixture: ComponentFixture<CreateNotebookModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateNotebookModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateNotebookModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

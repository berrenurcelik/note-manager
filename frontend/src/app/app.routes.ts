import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { NotebooksComponent } from './pages/notebooks/notebooks.component';
import { NotesComponent } from './pages/notes/notes.component';

export const routes: Routes = [
  { path: '', redirectTo: '/notebooks', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'notebooks', component: NotebooksComponent },
  { path: 'notes', component: NotesComponent },
  { path: 'notes/:notebookId', component: NotesComponent }
];

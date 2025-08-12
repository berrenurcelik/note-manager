import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { NotesComponent } from './pages/notes/notes.component';
import {NotebooksComponent} from './pages/notebooks/notebooks.component';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'home', component: NotebooksComponent },
  { path: 'notes', component: NotesComponent },
  { path: 'notes/:notebook_id', component: NotesComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
];


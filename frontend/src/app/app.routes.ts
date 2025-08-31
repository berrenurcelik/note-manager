/**
 * @fileoverview Definiert die Haupt-Routing-Konfiguration für die Anwendung.
 * @description Dieses Modul exportiert ein Array von `Routes`, das die Navigationspfade und die zugehörigen Komponenten festlegt.
 */
import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { NotebooksComponent } from './pages/notebooks/notebooks.component';
import { NotesComponent } from './pages/notes/notes.component';

/**
 * Ein Array von `Routes`, das die Navigationsregeln für die Anwendung enthält.
 *
 * @type {Routes}
 */
export const routes: Routes = [
  /**
   * Leitet den Root-Pfad ('/') auf die Login-Seite um.
   */
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  /**
   * Route für die Login-Seite.
   */
  { path: 'login', component: LoginComponent },
  /**
   * Route für die Registrierungsseite.
   */
  { path: 'register', component: RegisterComponent },
  /**
   * Route für die Notizbuch-Übersicht.
   */
  { path: 'notebooks', component: NotebooksComponent },
  /**
   * Route für die Notizen-Seite, die alle Notizen ohne Notizbuch-Kontext anzeigt.
   */
  { path: 'notes', component: NotesComponent },
  /**
   * Route für die Notizen-Seite, gefiltert nach einer spezifischen Notizbuch-ID.
   */
  { path: 'notes/:notebookId', component: NotesComponent },
];

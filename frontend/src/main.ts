/**
 * @fileoverview Der Haupteinstiegspunkt für das Bootstrapping der Angular-Anwendung.
 * @description Dieses Modul importiert die notwendigen Funktionen und die Hauptkomponente, um die Anwendung zu starten.
 * @since 1.0.0
 */
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

/**
 * Startet die Angular-Anwendung.
 * @param {App} App Die Hauptkomponente der Anwendung.
 * @param {appConfig} appConfig Die Konfiguration der Anwendung.
 * @returns {Promise<any>} Ein Promise, das bei erfolgreichem Start aufgelöst wird.
 * @catch Gibt Fehler in der Konsole aus, falls das Bootstrapping fehlschlägt.
 */
bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));

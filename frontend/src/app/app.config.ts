/**
 * @fileoverview Definiert die Anwendungskonfiguration für die Angular-Anwendung.
 * @description Dieses Modul konfiguriert die Provider, die für die Bootstrapping der Anwendung erforderlich sind, wie z. B. Router, HTTP-Client und Zonen-Wechselerkennung.
 * @since 1.0.0
 */
import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

/**
 * Die Haupt-Anwendungskonfiguration.
 * @type {ApplicationConfig}
 */
export const appConfig: ApplicationConfig = {
  /**
   * Ein Array von Providern für die Anwendung.
   */
  providers: [
    /**
     * Stellt globale Error-Listener für den Browser bereit.
     */
    provideBrowserGlobalErrorListeners(),
    /**
     * Konfiguriert die Zonen-Wechselerkennung mit Event-Coalescing.
     */
    provideZoneChangeDetection({ eventCoalescing: true }),
    /**
     * Konfiguriert den Router mit den definierten Routen.
     */
    provideRouter(routes),
    /**
     * Stellt den `HttpClient` für die HTTP-Anfragen bereit.
     */
    provideHttpClient()
  ]
};

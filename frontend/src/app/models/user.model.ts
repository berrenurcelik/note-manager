/**
 * @fileoverview Die Schnittstelle für die Darstellung der Benutzerdaten.
 * @description Diese Schnittstelle definiert die Struktur der Benutzerdaten, die von der API abgerufen werden.
 */
export interface User {
  /**
   * Die eindeutige ID des Benutzers.
   * @type {string}
   */
  id: string;
  /**
   * Der Benutzername des Benutzers, der für die Anmeldung verwendet wird.
   * @type {string}
   */
  username: string;
  /**
   * Der Vorname des Benutzers.
   * @type {string}
   */
  firstName: string;
  /**
   * Der Nachname des Benutzers.
   * @type {string}
   */
  lastName: string;
  /**
   * Die E-Mail-Adresse des Benutzers.
   * @type {string}
   */
  email: string;
}

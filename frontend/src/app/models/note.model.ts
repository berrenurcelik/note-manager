/**
 * @fileoverview Die Datenmodellklasse für eine Notiz.
 * @description Diese Klasse definiert die Struktur und die Eigenschaften einer einzelnen Notiz.
 */
export class Note {
  /**
   * Ein Index-Signature-Feld, das den Zugriff auf beliebige Eigenschaften über eine String-ID ermöglicht.
   * @private
   */
  [id: string]: any;

  /**
   * Die eindeutige ID der Notiz.
   * @type {string}
   */
  public id: string;

  /**
   * Der Titel der Notiz.
   * @type {string}
   */
  public title: string;

  /**
   * Der Zeitstempel, wann die Notiz erstellt wurde.
   * @type {Date}
   */
  public createdAt: Date;

  /**
   * Der Zeitstempel, wann die Notiz zuletzt geändert wurde.
   * @type {Date}
   */
  public modifiedAt: Date;

  /**
   * Der Inhalt der Notiz.
   * @type {string}
   */
  public content: string;

  /**
   * Die ID des Benutzers, dem die Notiz gehört.
   * @type {string}
   */
  public userId: string;

  /**
   * Die ID des Notizbuchs, zu dem die Notiz gehört.
   * @type {string}
   */
  public notebookId: string;

  /**
   * Erstellt eine Instanz der Note-Klasse.
   * @param {string} id Die eindeutige ID der Notiz.
   * @param {string} title Der Titel der Notiz.
   * @param {Date} createdAt Der Zeitstempel der Erstellung.
   * @param {Date} modifiedAt Der Zeitstempel der letzten Änderung.
   * @param {string} content Der Inhalt der Notiz.
   * @param {string} userId Die ID des Benutzers, dem die Notiz gehört.
   * @param {string} notebookId Die ID des übergeordneten Notizbuchs.
   */
  constructor(
    id: string,
    title: string,
    createdAt: Date,
    modifiedAt: Date,
    content: string,
    userId: string,
    notebookId: string
  ) {
    this.id = id;
    this.title = title;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.content = content;
    this.userId = userId;
    this.notebookId = notebookId;
  }
}

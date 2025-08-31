import {Note} from './note.model';

/**
 * @fileoverview Die Datenmodellklasse für ein Notizbuch.
 * @description Diese Klasse definiert die Struktur und die Eigenschaften eines einzelnen Notizbuchs,
 * das eine Sammlung von Notizen enthält.
 */
export class Notebook {
  /**
   * Ein Index-Signature-Feld, das den Zugriff auf beliebige Eigenschaften über eine String-ID ermöglicht.
   * @private
   */
  [id: string]: any;

  /**
   * Die eindeutige ID des Notizbuchs.
   * @type {string}
   */
  public id: string;

  /**
   * Der Titel des Notizbuchs.
   * @type {string}
   */
  public title: string;

  /**
   * Der Zeitstempel, wann das Notizbuch erstellt wurde.
   * @type {Date}
   */
  public createdAt: Date;

  /**
   * Ein Array von Notiz-Objekten, die in diesem Notizbuch enthalten sind.
   * @type {Note[]}
   */
  public notes: Note[];

  /**
   * Die ID des Benutzers, dem das Notizbuch gehört.
   * @type {string}
   */
  public userId: string;

  /**
   * Der URL oder Dateipfad des Bildes, das das Notizbuch repräsentiert.
   * @type {string}
   */
  public image: string;

  /**
   * Erstellt eine Instanz der Notebook-Klasse.
   * @param {string} id Die eindeutige ID des Notizbuchs.
   * @param {string} title Der Titel des Notizbuchs.
   * @param {Date} createdAt Der Zeitstempel der Erstellung.
   * @param {Note[]} [notes=[]] Ein optionales Array von Notizen, standardmäßig leer.
   * @param {string} userId Die ID des Benutzers, dem das Notizbuch gehört.
   * @param {string} image Der URL des repräsentativen Bildes.
   */
  constructor(
    id: string,
    title: string,
    createdAt: Date,
    notes: Note[] = [],
    userId: string,
    image: string
  ) {
    this.id = id;
    this.title = title;
    this.createdAt = createdAt;
    this.notes = notes;
    this.userId = userId;
    this.image = image;
  }
}

import {Note} from './note.model';

export class Notebook {
  [id: string]: any;
  public id: string;
  public title: string;
  public createdAt: Date;
  public notes: Note[];
  public userId: string;
  public image: string;

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

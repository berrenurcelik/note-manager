import { Note } from './note.model';

export class Notebook {
  [id: string]: any;
  public id: string;
  public title: string;
  public createdAt: Date;
  public notes: Note[];
  public user_id: string;
  public coverImage: string;

  constructor(
    id: string,
    title: string,
    createdAt: Date,
    notes: Note[] = [],
    user_id: string,
    coverImage: string = ''
  ) {
    this.id = id;
    this.title = title;
    this.createdAt = createdAt;
    this.notes = notes;
    this.user_id = user_id;
    this.coverImage = coverImage;
  }
}

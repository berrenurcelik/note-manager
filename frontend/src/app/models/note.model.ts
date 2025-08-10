export class Note {
  [id: string]: any;
  public id: string;
  public title: string;
  public createdAt: Date;
  public modifiedAt: Date;
  public content: string;
  public user_id: string;
  public notebook_id: string;

  constructor(
    id: string,
    title: string,
    createdAt: Date,
    modifiedAt: Date,
    content: string,
    user_id: string,
    notebook_id: string
  ) {
    this.id = id;
    this.title = title;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.content = content;
    this.user_id = user_id;
    this.notebook_id = notebook_id;
  }
}

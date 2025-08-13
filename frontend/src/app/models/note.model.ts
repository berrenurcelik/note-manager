export class Note {
  [id: string]: any;
  public id: string;
  public title: string;
  public createdAt: Date;
  public modifiedAt: Date;
  public content: string;
  public userId: string;
  public notebookId: string;

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

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from './type';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  baseUrl: string = 'http://localhost:8080/post';
  constructor(private http: HttpClient) {}

  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/all`);
  }
  getOnePost(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/id/${id}`);
  }
  addOnePost(content: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/add`, { content });
  }

  deleteOnePost(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, {
      responseType: 'text',
    });
  }
  updateOnePost(id: number, content: string): Observable<Post> {
    return this.http.put<Post>(`${this.baseUrl}/update/${id}`, { content });
  }

  getFollowPost(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/follow`);
  }
}

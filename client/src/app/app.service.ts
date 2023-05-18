import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RegisterErr, User } from './type';
@Injectable({
  providedIn: 'root',
})
export class AppService {
  constructor(private http: HttpClient, private router: Router) {}
  baseUrl: string = 'http://localhost:8080';
  isLogin: boolean = false;
  loginErr: string = '';
  registerErr: RegisterErr = { username: '', password: '' };
  username: string = '';

  login(username: string, password: string) {
    const loginForm: FormData = new FormData();
    loginForm.append('username', username);
    loginForm.append('password', password);
    loginForm.append('submit', 'login');

    this.http
      .post(`${this.baseUrl}/login`, loginForm, {
        responseType: 'text',
        withCredentials: true,
      })
      .subscribe((res) => {
        console.log(res);

        if (res === 'login') {
          this.isLogin = true;
          this.loginErr = '';

          this.username = username;

          sessionStorage.setItem('user', username);
          this.router.navigate(['detail']);
        } else {
          this.isLogin = false;
          this.loginErr = res;
        }
      });
  }
  logout() {
    this.http
      .post(`${this.baseUrl}/logout`, {}, { responseType: 'text' })
      .subscribe((res) => {
        console.log(res);
        if (res === 'bye') {
          this.isLogin = false;
          this.router.navigateByUrl('login');
          this.username = '';

          sessionStorage.removeItem('user');
        }
      });
  }

  getDetail(): Observable<any> {
    return this.http.get(`${this.baseUrl}/user/hello`, {
      responseType: 'text',
    });
  }
  getHome(): Observable<string> {
    return this.http.get(`${this.baseUrl}/user/home`, { responseType: 'text' });
  }

  register(username: string, password: string) {
    this.http
      .post(`${this.baseUrl}/user/register`, { username, password }, {})
      .subscribe((res: any) => {
        if (res && res['message'] !== 'register successfully') {
          console.log(res);
          this.registerErr = res;
        } else {
          this.router.navigateByUrl('login');
        }
      });
  }

  checkIsLogin() {
    const username = sessionStorage.getItem('user');
    if (username) {
      this.isLogin = true;
      this.username = username;
    }
  }
  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/user/u/${id}`);
  }

  followUser(id: number): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/follow/user/${id}`,
      {},
      { responseType: 'text' }
    );
  }
  //
  uploadImg(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('image', file);
    return this.http.post(`${this.baseUrl}/user/upload`, formData, {
      responseType: 'text',
    });
  }
}

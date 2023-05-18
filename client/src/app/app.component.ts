import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title?: string = '';
  baseUrl: string = 'http://localhost:8080';
  constructor(private http: HttpClient, public appService: AppService) {
    http
      .get(`${this.baseUrl}/user/home`, { responseType: 'text' })
      .subscribe((res) => {
        this.title = res;
      });
  }
  logout() {
    this.appService.logout();
  }

  ngOnInit(): void {
    this.appService.checkIsLogin();
  }
}

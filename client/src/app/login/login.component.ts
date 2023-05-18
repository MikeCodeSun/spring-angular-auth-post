import { Component } from '@angular/core';
import { AppService } from '../app.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  constructor(public appService: AppService) {}
  username: string = '';
  password: string = '';
  onSubmit() {
    this.appService.login(this.username, this.password);
  }
}

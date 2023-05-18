import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  constructor(public appService: AppService) {}
  username: string = '';
  password: string = '';

  onSubmit() {
    this.appService.register(this.username, this.password);
  }
}

import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css'],
})
export class DetailComponent implements OnInit {
  constructor(private appService: AppService) {}

  detailName: string = '';

  ngOnInit() {
    this.appService.getDetail().subscribe((res) => (this.detailName = res));
  }
}

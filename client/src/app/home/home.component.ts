import { Component } from '@angular/core';
import { AppService } from '../app.service';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  constructor(
    private appService: AppService,
    private postService: PostService
  ) {}
  homeName: string = '';
  posts: Post[] = [];
  ngOnInit() {
    this.appService.getHome().subscribe((res) => (this.homeName = res));
    this.postService.getFollowPost().subscribe(
      (res) => {
        this.posts = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}

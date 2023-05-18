import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {
  constructor(private postService: PostService) {}
  posts: Post[] = [];
  content: string = '';
  contentErrMsg: string = '';
  isUserlogin: boolean = false;
  onSubmit() {
    this.postService.addOnePost(this.content).subscribe(
      (res) => {
        this.posts.unshift(res);
        this.content = '';
      },
      (err) => {
        if (err.error) {
          this.contentErrMsg = err.error.content;
        }
      }
    );
  }
  ngOnInit(): void {
    this.postService.getAllPosts().subscribe((res) => {
      this.posts = res;
    });
    if (sessionStorage.getItem('user')) {
      this.isUserlogin = true;
    } else {
      this.isUserlogin = false;
    }
  }
}

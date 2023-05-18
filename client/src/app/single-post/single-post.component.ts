import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from '../app.service';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-single-post',
  templateUrl: './single-post.component.html',
  styleUrls: ['./single-post.component.css'],
})
export class SinglePostComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private appService: AppService,
    private router: Router
  ) {}
  post?: Post;
  postBelong: boolean = false;
  isEdit: boolean = false;
  content: string = '';
  editContentErrMsg: string = '';
  openEdit() {
    this.isEdit = true;
  }
  closeEdit() {
    this.isEdit = false;
  }
  updatePost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.updateOnePost(postId, this.content).subscribe(
      (res) => {
        this.post = res;
        this.closeEdit();
      },
      (err) => {
        console.log(err);
        this.editContentErrMsg = err.error.content;
      }
    );
  }

  getPost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getOnePost(postId).subscribe(
      (res) => {
        this.post = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
  deletePost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.deleteOnePost(postId).subscribe(
      (res) => {
        console.log(res);
        if (res === 'delete') {
          this.router.navigateByUrl('post');
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }
  checkPostBelong() {
    if (this.appService.username === this.post?.user.username) {
      this.postBelong = true;
    } else {
      this.postBelong = false;
    }
  }

  ngOnInit(): void {
    this.getPost();
  }
  ngDoCheck() {
    this.checkPostBelong();
  }
}

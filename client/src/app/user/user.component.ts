import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from '../app.service';
import { User } from '../type';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  constructor(
    private appService: AppService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  user: User = {
    id: 0,
    username: '',
    image: '',
    followers: 0,
    following: 0,
    follow: false,
  };
  file?: File;
  isUploda: boolean = false;
  @ViewChild('imageInput') imageInput!: ElementRef;
  getUser() {
    const userId = Number(this.route.snapshot.paramMap.get('id'));

    this.appService.getUser(userId).subscribe(
      (res) => {
        this.user = res;
        if (res.image) {
          this.user.image = 'http://localhost:8080/image/' + res.image;
        }
      },
      (err) => {
        console.log(err);
        this.router.navigateByUrl('login');
      }
    );
  }
  followUser() {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    this.appService.followUser(userId).subscribe(
      (res) => {
        const username = sessionStorage.getItem('user');
        if (res === 'follow') {
          this.user.followers += 1;
          this.user.follow = true;
          if (this.user.username === username) {
            this.user.following += 1;
          }
        } else {
          this.user.followers -= 1;
          this.user.follow = false;
          if (this.user.username === username) {
            this.user.following -= 1;
          }
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }
  checkIsUser() {
    const username = sessionStorage.getItem('user');
    return this.user.username === username;
  }

  getImage(e: Event) {
    const input = e.target as HTMLInputElement;
    if (!input.files?.length) {
      return;
    }
    this.file = input.files[0];
  }

  upload() {
    if (!this.file) return;

    this.appService.uploadImg(this.file).subscribe(
      (res) => {
        this.user.image = 'http://localhost:8080/image/' + res;
        this.closeUpload();
        this.imageInput.nativeElement.value = '';
      },
      (err) => {
        console.log(err);
      }
    );
  }

  openUpload() {
    this.isUploda = true;
  }

  closeUpload() {
    this.isUploda = false;
  }

  ngOnInit(): void {
    this.getUser();
  }
}

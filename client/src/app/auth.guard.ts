import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AppService } from './app.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private appService: AppService, private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const componentName = route.component?.name;

    if (
      this.appService.isLogin &&
      componentName !== 'LoginComponent' &&
      componentName !== 'RegisterComponent'
    ) {
      return true;
    } else if (
      this.appService.isLogin &&
      (componentName === 'LoginComponent' ||
        componentName === 'RegisterComponent')
    ) {
      this.router.navigateByUrl('detail');
      return false;
    } else if (
      !this.appService.isLogin &&
      (componentName === 'LoginComponent' ||
        componentName === 'RegisterComponent')
    ) {
      return true;
    }
    return false;
  }
}

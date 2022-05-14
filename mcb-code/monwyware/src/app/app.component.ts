import { Component } from '@angular/core';
import {UtilService} from "./services/util.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private utilService:UtilService,private router:Router) {
  }
  logout(){
    this.utilService.logout();
    this.router.navigateByUrl('');
  }
}

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../services/http-service.service';
import { UtilService } from '../services/util.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginFormGroup:FormGroup;

  constructor(private router:Router,private utilService:UtilService,private httpService:HttpService) { 
    this.loginFormGroup = new FormGroup({
      username: new FormControl(null,[Validators.required,Validators.minLength(3),Validators.maxLength(20)]),
      password: new FormControl(null,[Validators.required,Validators.minLength(3),Validators.maxLength(20)])
    })
  }

  ngOnInit(): void {
    if(this.utilService.isLoggedIn()){
      this.router.navigateByUrl('uploadFile')
    }
  }

  login(){
    const payload = this.loginFormGroup.getRawValue();
    this.httpService.login(payload).subscribe(d=>{
      this.utilService.authenicate(d);
      this.router.navigateByUrl('uploadFile')
    })
  }

}

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor() { }

  getUserToken():any|null{
   let userData =  localStorage.getItem("token");
  return userData=userData?JSON.parse(userData):null;
  }

  isLoggedIn():boolean{
    return this.getUserToken()?true:false;
  }

  authenicate(data:any){
    localStorage.setItem('token',JSON.stringify(data))
  }

  logout(){
    localStorage.removeItem('token')
  }
}

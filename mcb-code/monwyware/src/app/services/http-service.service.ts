import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {first} from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private httpClient:HttpClient) { }

  login(payload:{username:string,password:String}){
   return this.httpClient.post("authenticate",payload).pipe(first());
  }

  uploadFile(fileData:any){
    const formData = new FormData();
    formData.append('name',fileData.fileName);
    formData.append('file',fileData.file);
    formData.append('documentType ',fileData.type);
    formData.append('ext',fileData.fileExt);
    formData.append('fileType',fileData.fileType);
   return this.httpClient.post("upload",formData).pipe(first());

  }
}

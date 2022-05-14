import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../services/http-service.service';

@Component({
  selector: 'app-file-handler',
  templateUrl: './file-handler.component.html',
  styleUrls: ['./file-handler.component.scss']
})
export class FileHandlerComponent implements OnInit {

  fileExt:any;
  fileType:string='';

  fileForm:FormGroup= new FormGroup({
    'type':new FormControl('',[Validators.required]),
    'fileName':new FormControl({value:'',disabled:false},[Validators.required]),
    'fileType':new FormControl('',[Validators.required]),
    'fileExt':new FormControl('',[Validators.required]),
    'file': new FormControl('',[Validators.required])
  })

  constructor(private httpService:HttpService) { }

  ngOnInit(): void {
  }

  uploadFile(event:any) {
    event = event.files
      const element = event[0];
      this.setValue('file',element);
      this.setValue('fileName',element?.name);
      this.setValue('fileType',element?.type);
      this.setValue('fileExt', element?.name.split('.').pop());
  }

  setValue(controlName:string, value:any){
    this.fileForm.get(controlName)?.setValue(value);
  }

  sendFile(){
    const fileData = this.fileForm.getRawValue();    
    this.httpService.uploadFile(fileData).subscribe(d=>{})
  }

}

import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'source-file-uploader',
  templateUrl: './source-file-upload.component.html',
  styleUrls: ['./source-file-upload.component.css'],
})

export class SourceFileUploaderComponent {

  public uploader: FileUploader;
  public isUploadCompleted:boolean = false;

  constructor() {
    this.uploader = new FileUploader({
      url:'/api/uploader',
      isHTML5: true
    });

    this.uploader.onCompleteItem = (item, response, status, header) => {
      if (status === 200) {
        //Your code goes here
        alert("ok");
        this.isUploadCompleted =  true;
      }
    };


  }

}

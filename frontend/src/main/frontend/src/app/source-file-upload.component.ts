import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { Router } from '@angular/router';

import { HeroService } from './hero.service';

@Component({
  selector: 'source-file-uploader',
  templateUrl: './source-file-upload.component.html',
  styleUrls: ['./source-file-upload.component.css'],
  providers: [HeroService]
})

export class SourceFileUploaderComponent {

  public uploader: FileUploader;
  public isUploadCompleted:boolean = false;

  constructor(private router: Router,
              private heroService: HeroService) {

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

  onSubmit():void {
    this.heroService.process()
      .then( response => console.log("Success"),
       error => console.log("Error"));
  };

  onCancel(): void {
    this.router.navigate(['/detail']);
  }


}

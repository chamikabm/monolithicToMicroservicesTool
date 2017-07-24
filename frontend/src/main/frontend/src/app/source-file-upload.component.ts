import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'source-file-uploader',
  templateUrl: './source-file-upload.component.html',
  styleUrls: ['./source-file-upload.component.css'],
})

export class SourceFileUploaderComponent {
  public uploader:FileUploader = new FileUploader({url:'/api/uploader'});
}

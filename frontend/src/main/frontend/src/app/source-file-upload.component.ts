import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';

import { Http } from '@angular/http';

import { MicroService } from './micro-service';

@Component({
  selector: 'source-file-uploader',
  templateUrl: './source-file-upload.component.html',
  styleUrls: ['./source-file-upload.component.css']
})

export class SourceFileUploaderComponent {

  public uploader: FileUploader;
  public isUploadCompleted:boolean = false;
  public isProcessCompleted:boolean = false;
  public microservices: MicroService[];
  public errormessage: string;

  constructor(public http: Http) {

    this.uploader = new FileUploader({
      url:'/api/uploader',
      isHTML5: true
    });

    this.uploader.onCompleteItem = (item, response, status, header) => {
      if (status === 200) {
        this.isUploadCompleted =  true;
      }
    };
  }

  onSubmit():void {
    this.http.get('/api/process', {}).subscribe(
      (res:any) => {
        this.microservices = res.json() as MicroService[];
        this.isProcessCompleted = true;
      },
      (error) => {
        console.log("Error Occurred "+ error);
        if (error.status == 500) {
          this.errormessage = error._body;
          this.isProcessCompleted = true;
        }

      },
      () => {
        console.log("Event Completed.");
      }
    )
  };

  onCancel(): void {
    this.isUploadCompleted =  false;
    this.uploader.clearQueue();
  }

  onRemove(): void {
    this.isUploadCompleted =  false;
    this.uploader.clearQueue();
  }

  getMicroServiceListStyleClass(riskLevel:string):string {
    switch (riskLevel) {
      case "LOW_RISK":
        return "accordion-toggle list-group-item-success";
      case "MEDIUM_RISK":
        return "accordion-toggle list-group-item-info";
      case "HIGH_RISK":
        return "accordion-toggle list-group-item-danger";
      default:
        return "accordion-toggle list-group-item-action";
    }
  }

  getMicroServiceBadgeStyleClass(riskLevel:string):string {
    switch (riskLevel) {
      case "LOW_RISK":
        return "badge badge-success";
      case "MEDIUM_RISK":
        return "badge badge-info";
      case "HIGH_RISK":
        return "badge badge-danger";
      default:
        return "badge badge-default";
    }
  }

  getImportancePercentage(riskLevel:string):number {
    switch (riskLevel) {
      case "LOW_RISK":
        return 75;
      case "MEDIUM_RISK":
        return 63;
      case "HIGH_RISK":
        return 72;
      default:
        return 80;
    }
  }
}

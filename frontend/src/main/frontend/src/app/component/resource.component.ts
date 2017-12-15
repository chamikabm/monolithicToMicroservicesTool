import { Component } from '@angular/core';

@Component({
  selector: 'about',
  templateUrl: '../page/resource.component.html'
})

export class ResourceComponent {
  goToResourceDirectory(): void {
    window.open("https://drive.google.com/open?id=1HUSeed230Ty7Cb7XmkpCCpvMCSsVHyBi", "_blank");
  }
}


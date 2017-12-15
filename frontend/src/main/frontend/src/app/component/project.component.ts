import { Component } from '@angular/core';

@Component({
  selector: 'about',
  templateUrl: '../page/project.component.html'
})

export class ProjectComponent {
  goToGitRepo(): void {
    window.open("https://github.com/chamikabm/monolithicToMicroservicesTool", "_blank");
  }
}


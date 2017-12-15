import { Component } from '@angular/core';

@Component({
  selector: 'about',
  templateUrl: '../page/about.component.html',
  styleUrls: ['../style/about.component.css']
})

export class AboutComponent {
  goToMyLinkedIn(): void {
    window.open("https://www.linkedin.com/in/chamikakasun", "_blank");
  }
  goToMyFacebook(): void {
    window.open("https://www.facebook.com/kasun.chamika", "_blank");
  }
  goToMyTwitter(): void {
    window.open("https://twitter.com/ChamikaKasun?lang=en", "_blank");
  }
  goToMyGoogle(): void {
    window.open("https://plus.google.com/u/0/+ChamikaKasun", "_blank");
  }
}


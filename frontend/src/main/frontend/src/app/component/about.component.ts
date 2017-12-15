import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';

@Component({
  selector: 'about',
  templateUrl: '../page/about.component.html',
  styleUrls: ['../style/about.component.css']
})

export class AboutComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {  }
}


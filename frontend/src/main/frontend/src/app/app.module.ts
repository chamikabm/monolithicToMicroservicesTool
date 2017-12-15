import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule }    from '@angular/http';

import { AppRoutingModule }     from './app-routing.module';
import { FileSelectDirective } from 'ng2-file-upload';

import { AppComponent }        from './component/app.component';
import { DashboardComponent, }        from './component/dashboard.component';
import { SourceFileUploaderComponent }     from './component/source-file-upload.component';
import { AboutComponent }     from './component/about.component';
import { ProjectComponent }     from './component/project.component';
import { ResourceComponent }     from './component/resource.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    SourceFileUploaderComponent,
    AboutComponent,
    ProjectComponent,
    ResourceComponent,
    FileSelectDirective
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})

export class AppModule {}

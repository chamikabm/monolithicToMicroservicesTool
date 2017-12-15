import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent }   from './component/dashboard.component';
import { SourceFileUploaderComponent }  from './component/source-file-upload.component';
import { AboutComponent }  from './component/about.component';
import { ProjectComponent }  from './component/project.component';
import { ResourceComponent }  from './component/resource.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard',  component: DashboardComponent },
  { path: 'upload',     component: SourceFileUploaderComponent },
  { path: 'about',     component: AboutComponent },
  { path: 'project',     component: ProjectComponent },
  { path: 'resource',     component: ResourceComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule {}

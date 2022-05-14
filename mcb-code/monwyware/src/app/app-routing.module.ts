import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FileHandlerComponent } from './file-handler/file-handler.component';
import { LoginComponent } from './login/login.component';
import { LoggedInGuard } from './security/logged-in.guard';

const routes: Routes = [{
  path:'',
  redirectTo:'login',
  pathMatch:'full'
},{
  path:'login',
  component:LoginComponent,
  pathMatch:'full',
},{
  path:'uploadFile',
  component:FileHandlerComponent,
  canActivate:[LoggedInGuard],
  pathMatch:'full'
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

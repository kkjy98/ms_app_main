import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UiLayoutComponent } from './Layout/ui-layout/ui-layout.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { SignupComponent } from './signup/signup.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login', // Redirect to login page by default
    pathMatch: 'full'
  },
  {
    path: '',
    component: UiLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent, children: []},
      { path: 'home', component: HomeComponent, children: []},
      { path: 'sign-up', component: SignupComponent, children: []}
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

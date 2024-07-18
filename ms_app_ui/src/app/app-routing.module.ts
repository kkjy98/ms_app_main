import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './auth.guard';
import { UiLayoutComponent } from './Layout/ui-layout/ui-layout.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { SignupComponent } from './signup/signup.component';
import { ExpenseComponent } from './expense/expense.component';
import { DataGraphComponent } from './data-graph/data-graph.component';

const routes: Routes = [
  // {
  //   path: '',
  //   redirectTo: 'login', // Redirect to login page by default
  //   pathMatch: 'full'
  // },
  {
    path: '',
    component: UiLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent, children: []},
      { path: 'home', component: HomeComponent ,canActivate: [authGuard], children: []},
      { path: 'exp', component: ExpenseComponent ,canActivate: [authGuard], children: []},
      { path: 'graph', component: DataGraphComponent ,canActivate: [authGuard], children: []},
      { path: 'sign-up', component: SignupComponent, children: []}
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

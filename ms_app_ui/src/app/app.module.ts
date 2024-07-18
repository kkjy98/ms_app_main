import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UiLayoutComponent } from './Layout/ui-layout/ui-layout.component';
import { HeaderComponent } from './header/header.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { LoginComponent } from './login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './home/home.component';
import { SignupComponent } from './signup/signup.component';
import { ExpenseComponent } from './expense/expense.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // Required for Angular Material
import { MatNativeDateModule } from '@angular/material/core';
import { DataGraphComponent } from './data-graph/data-graph.component';
import {ChartModule,AccumulationChartComponent, AccumulationChart, IAccLoadedEventArgs, AccumulationTheme, AccumulationChartAllModule, ChartAllModule} from '@syncfusion/ej2-angular-charts';
import {GridModule} from '@syncfusion/ej2-angular-grids';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { BaseChartDirective } from 'ng2-charts';

@NgModule({
  declarations: [
    AppComponent,
    UiLayoutComponent,
    HeaderComponent,
    SidebarComponent,
    LoginComponent,
    HomeComponent,
    SignupComponent,
    ExpenseComponent,
    DataGraphComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDatepickerModule,
    MatInputModule,
    MatFormFieldModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatNativeDateModule,
    ChartModule,
    GridModule,
    AccumulationChartAllModule, 
    ChartAllModule, 
    BaseChartDirective
  ],
  providers: [
    provideAnimationsAsync(),
    provideCharts(withDefaultRegisterables())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

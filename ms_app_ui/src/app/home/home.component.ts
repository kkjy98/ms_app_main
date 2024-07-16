import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  username!:any;

  ngOnInit(): void {
    this.username=localStorage.getItem("username");
}

}

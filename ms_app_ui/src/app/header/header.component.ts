import { Component, inject } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  isLogin=false;
  isSignIn=true;
  username:any;
  activeLink: string = 'login';
  authService = inject(AuthService);

  setActiveLink(link: string) {
    this.activeLink = link;
  }

  ngOnInit() :void{
    this.checkSignIn();
  }

  checkSignIn() {
    this.username=localStorage.getItem("username");

    if(this.username=""){
      this.isSignIn=false;
    }
  }

  


}

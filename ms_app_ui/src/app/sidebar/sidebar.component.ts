import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  activeLink: string = 'home';
  username!:any;

  constructor(private router: Router) { }

  setActiveLink(link: string) {
    this.activeLink = link;
    console.log(this.activeLink);
  }

  ngOnInit(): void {
    this.username = localStorage.getItem("username");
  }

  signOut() {
 
    localStorage.removeItem("access_token");
    // Optionally, you can navigate to the login page after sign-out
    this.router.navigate(['/login']);
  }
}

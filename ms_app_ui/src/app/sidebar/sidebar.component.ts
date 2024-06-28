import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  activeLink: string = 'home';

  setActiveLink(link: string) {
    this.activeLink = link;
    console.log(this.activeLink);
  }
}

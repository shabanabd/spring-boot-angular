import { Component,ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;
  isCollapsed = true;

  constructor(public authService: AuthService) { }
  logout() {
    this.authService.doLogout()
  }

  toggleMenu() {
    // this.sidenav.toggle();
    this.sidenav.open();
    this.isCollapsed = !this.isCollapsed;
  }
}

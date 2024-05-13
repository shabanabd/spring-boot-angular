import {Component} from '@angular/core';
import {ApplicationService} from "../../services/application.service";
import {Statistics} from "../../shared/statistics";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  statistics: Statistics| null = null;
  constructor(private applicationService: ApplicationService) {
    this.applicationService.getApplicationStatistics()
      .subscribe(data =>{
        this.statistics = data;
      });
  }

}

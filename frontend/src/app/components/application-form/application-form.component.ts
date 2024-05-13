import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ApplicationService} from "../../services/application.service";
import {Application} from "../../shared/application";

@Component({
  selector: 'app-application-form',
  templateUrl: './application-form.component.html',
  styleUrl: './application-form.component.css'
})
export class ApplicationFormComponent {
  applicationForm: FormGroup;
  title = "Create Application";
  application:Application;
  constructor(
    private applicationService: ApplicationService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA)private data: any) {
    this.application =data;
    if (this.application != null) {
      this.title = "Update Application";
      this.applicationForm = this.fb.group({
        salesAgentId: [this.application.salesAgentEmail],
        accountType: [this.application.accountType],
        applicationStatus: [this.application.applicationStatus],
        businessCategory: [this.application.businessCategory]
      });
    } else {
      this.applicationForm = this.fb.group({
        salesAgentId: [''],
        accountType: [''],
        applicationStatus: [''],
        businessCategory: ['']
      });
    }
  }
  closeDialog(){
    this.dialog.closeAll();
  }
  saveApp(){
    const newApp = this.application? this.application : new Application();
    newApp.salesAgentEmail = this.applicationForm.get("salesAgentId")?.value;
    newApp.accountType = this.applicationForm.get("accountType")?.value;
    newApp.applicationStatus = this.applicationForm.get("applicationStatus")?.value;
    newApp.businessCategory = this.applicationForm.get("businessCategory")?.value;
    if (this.application != null) {
      this.applicationService.updateApplication(newApp)
        .subscribe(appId => this.dialog.closeAll());
    } else {
      this.applicationService.addApplication(newApp)
        .subscribe(appId => this.dialog.closeAll());
    }
  }
}

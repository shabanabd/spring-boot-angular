import {Component, ViewChild} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import {MatTableDataSource} from "@angular/material/table";
import {MatSort, Sort} from '@angular/material/sort';
import {merge, startWith, switchMap, of as observableOf, tap} from "rxjs";
import {ApplicationService} from "../../services/application.service";
import {Application} from "../../shared/application";
import {catchError, map} from "rxjs/operators";
import {FormBuilder, FormGroup} from "@angular/forms";
import { formatDate } from '@angular/common';
import {MatDialog} from "@angular/material/dialog";
import {ApplicationFormComponent} from "../application-form/application-form.component";



@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent {
  @ViewChild(MatPaginator) paginator: MatPaginator| null = null;
  @ViewChild(MatSort) sort: MatSort| null = null;
  searchForm: FormGroup;

  constructor(
    private applicationService: ApplicationService,
    public fb: FormBuilder,
    private dialog: MatDialog
  ) {
    this.searchForm = this.fb.group({
      searchFld: [''],
      createdOn: [''],
      updatedOn: ['']
    });
  }

  displayedColumns: string[] = ['seqNo', 'id', 'salesAgentEmail', 'accountType', 'createdAt', 'applicationStatus',
    'businessCategory', 'updatedAt','edit', 'delete'];
  dataSource = new MatTableDataSource<Application>();
  totalData: number = 0;

  ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort!.sortChange.subscribe(() => (this.paginator!.pageIndex = 0));

    merge(this.sort!.sortChange, this.paginator!.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          // this.isLoadingResults = true;
          let createdDate = this.searchForm.get("createdOn")?.value? formatDate(this.searchForm.get("createdOn")?.value,'yyyy-MM-dd', "en-US") : "";
          let updatedDate = this.searchForm.get("updatedOn")?.value? formatDate(this.searchForm.get("updatedOn")?.value,'yyyy-MM-dd', "en-US") : "";
          let param = {
            "searchText": this.searchForm.get("searchFld")?.value,
            "createdDate": createdDate,
            "updatedDate": updatedDate,
            "sort": this.sort!.active + ',' + this.sort!.direction,
            "page": this.paginator!.pageIndex,
            "size": this.paginator!.pageSize
          }
          return this.applicationService.getApplications(param)
            .pipe(catchError(() => observableOf(null)));
        }),
        map((data) => {
          if (data === null) {
            return [];
          }
          this.totalData = data.totalElements;
          return data.content;
        })
      )
      .subscribe((data) => (this.dataSource = new MatTableDataSource(data)));
  }

  filter() {
    let createdDate = this.searchForm.get("createdOn")?.value? formatDate(this.searchForm.get("createdOn")?.value,'yyyy-MM-dd', "en-US") : "";
    let updatedDate = this.searchForm.get("updatedOn")?.value? formatDate(this.searchForm.get("updatedOn")?.value,'yyyy-MM-dd', "en-US") : "";
    let param = {
      "searchText": this.searchForm.get("searchFld")?.value,
      "createdDate": createdDate,
      "updatedDate": updatedDate,
      "sort": this.sort!.active + ',' + this.sort!.direction,
      "page": this.paginator!.pageIndex,
      "size": this.paginator!.pageSize
    }
    this.applicationService.getApplications(param)
      .subscribe(apps =>{
        this.totalData = apps.totalElements;
        this.dataSource = new MatTableDataSource(apps.content);
      });
  }

  delete(id:number){
    console.log("DELETE:"+id);
    this.applicationService.deleteApplication(id)
      .subscribe(() => this.filter());
  }

  edit(app:Application){
    console.log(app);
    let dialogRef = this.dialog.open(ApplicationFormComponent,{
      height: '550px',
      width: '400px',
      data:app
    });

    // subscription on close
    dialogRef.afterClosed()
      .subscribe(() => {
        this.filter();
      });
  }

  createApp(){
    let dialogRef = this.dialog.open(ApplicationFormComponent,{
      height: '550px',
      width: '400px',
      data: null
    });

    // subscription on close
    dialogRef.afterClosed()
      .subscribe(() => {
        this.filter();
      });
  }

  exportCSV() {
    let createdDate = this.searchForm.get("createdOn")?.value? formatDate(this.searchForm.get("createdOn")?.value,'yyyy-MM-dd', "en-US") : "";
    let updatedDate = this.searchForm.get("updatedOn")?.value? formatDate(this.searchForm.get("updatedOn")?.value,'yyyy-MM-dd', "en-US") : "";
    let param = {
      "searchText": this.searchForm.get("searchFld")?.value,
      "createdDate": createdDate,
      "updatedDate": updatedDate,
      "sort": this.sort!.active + ',' + this.sort!.direction,
      "page": this.paginator!.pageIndex,
      "size": this.paginator!.pageSize
    }
    this.applicationService.exportApplications(param)
      .subscribe(response =>{
        console.log(response);
        let blob = new Blob([response.body], { type: 'text/csv;charset=utf-8' });
        let url = window.URL.createObjectURL(blob);
        let fileLink = document.createElement('a');
        fileLink.href = url;
        fileLink.download = 'Applications.csv';
        fileLink.click();
      });
  }
}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { YachtListComponent } from './yacht-list/yacht-list.component';
import { RentalFormComponent } from './rental-form/rental-form.component';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppComponent,
    YachtListComponent, 
    RentalFormComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
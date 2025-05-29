import { Component } from '@angular/core';
import { YachtListComponent } from './yacht-list/yacht-list.component';  

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [YachtListComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'oceanic-dreams';
}

import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RentalFormComponent } from '../rental-form/rental-form.component';
interface Yacht {
  id: number;
  name: string;
  builder: string;
  cabins: number;
  crew: number;
  daily_price: number;
  deposit: number;
  guests: number;
  image: string;
  length: string;
  refit: string | number;
  year: number;
}

@Component({
  selector: 'app-yacht-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule,RentalFormComponent],
  templateUrl: './yacht-list.component.html',
  styleUrls: ['./yacht-list.component.scss']
})

export class YachtListComponent implements OnInit {
  yachts: Yacht[] = [];
  filteredYachts: Yacht[] = [];
  searchTerm: string = '';

  constructor(private http: HttpClient) {}

ngOnInit(): void {
  this.http.get<{ [key: string]: Yacht }>('https://p161-7ddfd-default-rtdb.europe-west1.firebasedatabase.app/yachts.json')
    .subscribe(data => {
      this.yachts = Object.values(data);
      this.filteredYachts = this.yachts;
      console.log(this.filteredYachts);
    });
}

  onSearchChange() {
    const term = this.searchTerm.toLowerCase();
    this.filteredYachts = this.yachts.filter(y =>
      y.name.toLowerCase().includes(term) ||
      y.builder.toLowerCase().includes(term)
    );
  }


  selectedYachtId: number | null = null;

  toggleRentalForm(yachtId: number) {
    this.selectedYachtId = this.selectedYachtId === yachtId ? null : yachtId;
  }

  handleRentalResult(result: { success: boolean; message: string }) {
    alert(result.message);
    if (result.success) {
      this.selectedYachtId = null;
    }
  }
}

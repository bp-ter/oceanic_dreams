import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rental-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './rental-form.component.html'
})
export class RentalFormComponent {
  @Input() yachtId!: string;
  @Output() rentalResult = new EventEmitter<{ success: boolean, message: string }>();

  rentalForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.rentalForm = this.fb.group({
      userId: ['101', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }

  submitRental() {
    if (!this.rentalForm.valid) return;

    const rentalData = {
      ...this.rentalForm.value,
      yachtId: this.yachtId
    };

    this.http.post('https://p161-7ddfd-default-rtdb.europe-west1.firebasedatabase.app/rentals.json', rentalData)
      .subscribe({
        next: () => this.rentalResult.emit({ success: true, message: 'Bérlés sikeresen leadva!' }),
        error: () => this.rentalResult.emit({ success: false, message: 'Hiba történt a bérlés során.' })
      });
  }
}

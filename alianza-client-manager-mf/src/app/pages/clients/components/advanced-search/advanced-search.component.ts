import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClientSearchCriteria } from '../../interfaces/client_search_criteria';
import { DialogModule } from 'primeng/dialog';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-advanced-search',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, DialogModule, CalendarModule],
  templateUrl: './advanced-search.component.html',
  styleUrl: './advanced-search.component.scss',
})
export class AdvancedSearchComponent implements OnInit {

  @Input() display: boolean = false;

  @Output() onClose = new EventEmitter<void>();

  @Output() onSearch = new EventEmitter<ClientSearchCriteria>();

  searchForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.searchForm = this.fb.group({
      sharedKey: [''],
      name: [''],
      email: [''],
      phone: [''],
      startDate: [null],
      endDate: [null],
    });
  }

  closeDialog(): void {
    this.onClose.emit();
  }

  search(): void {
    const formValue = this.searchForm.value;

    const criteria: ClientSearchCriteria = {
      ...formValue,
      startDate: formValue.startDate
        ? this.formatDate(formValue.startDate)
        : undefined,
      endDate: formValue.endDate
        ? this.formatDate(formValue.endDate)
        : undefined,
    };

    Object.keys(criteria).forEach((key) => {
      if (!criteria[key as keyof ClientSearchCriteria]) {
        delete criteria[key as keyof ClientSearchCriteria];
      }
    });

    this.onSearch.emit(criteria);
  }

  resetForm(): void {
    this.searchForm.reset();
  }


  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }
}

import { ChangeDetectionStrategy, Component, ElementRef, ViewChild ,EventEmitter, Output} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Expense } from '../model/expense';
import { ApiServiceService } from '../api-service.service';
import { lastValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-expense',
  templateUrl: './expense.component.html',
  styleUrls: ['./expense.component.css']
 
})
export class ExpenseComponent {

  @ViewChild('expenseList') expenseList!: ElementRef;
  @Output() formSubmitted = new EventEmitter<void>();

  expenseForm!: FormGroup;
  isLoading=false;
  result!:any;
  resultCode!:string;
  errorMessage!: string;
  expensesList: Expense[] = [];

  constructor(private fb: FormBuilder,
    private apiservice: ApiServiceService
  ) {}

  ngOnInit(): void {
    this.loadExpenses(); // Load expenses first

    this.expenseForm = this.fb.group({
      amount: ['', Validators.required],
      category: ['', Validators.required], // Validators.required ensures the category is not empty
      description: [''],
      date: ['', Validators.required] // Validators.required ensures the date is not empty
    });
}
  async onSubmit(): Promise<void> {
    if (this.expenseForm.valid) {
        this.isLoading = true;
        const expense = new Expense();
        expense.amount = this.expenseForm.get('amount')?.value;
        expense.category = this.expenseForm.get('category')?.value;
        expense.date = this.expenseForm.get('date')?.value;
        expense.description = this.expenseForm.get('description')?.value;

        await lastValueFrom(this.apiservice.addExp(expense)).then((res: any) => {
            this.result = res;
            this.resultCode = this.result.code;
            this.formSubmitted.emit();
            this.loadExpenses(); // Refresh the expense list only after submission
        }).catch(err => {
            this.isLoading=false;
            console.log("Error", err);
            this.resultCode = err.error.result_code;
            this.handleError(err);
            
        });
    } else {
        console.log('Form is invalid');
    }

}

  private loadExpenses(): void {
    this.apiservice.getExp(localStorage.getItem("username")).subscribe((res: any) => {
      this.expensesList = res.data;
      console.log("Expense List:", this.expensesList);
      this.scrollToBottom();
    });
  }

  private handleError(httpErrorResponse: HttpErrorResponse) {

    var errorResponse = httpErrorResponse.error;

    if (errorResponse && errorResponse.error) {
      let apiError: any = errorResponse.error;
      let message: string = apiError.message;

      this.resultCode = errorResponse.code;
      this.errorMessage = message;

      return;
    }

  }

  scrollToBottom() {
    try {
      const element = this.expenseList.nativeElement;
      element.scrollTo({
        top: element.scrollHeight - element.clientHeight,
        behavior: 'smooth'
      });
    } catch (err) {
      console.error('Error scrolling to bottom', err);
    }
  }
 
}

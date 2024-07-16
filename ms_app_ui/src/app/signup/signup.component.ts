import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiServiceService } from '../api-service.service';
import { User } from '../model/user';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {
  isLoading = false;
  isError = false;
  signupForm!: FormGroup;
  result!: any;
  resultCode!: number;
  user: User = new User();
  errorMessage: string | undefined;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private apiservice: ApiServiceService
  ) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      username: ['', Validators.required],
      fname: ['', Validators.required],
      lname: ['', Validators.required],
      phoneno: ['', Validators.required]
    });
  }

  async submit() {
    this.isLoading = true;
    this.isError = false;

    // User object
    this.user.email = this.signupForm.controls['email'].value;
    this.user.password = this.signupForm.controls['password'].value;
    this.user.username = this.signupForm.controls['username'].value;
    this.user.firstName = this.signupForm.controls['fname'].value;
    this.user.lastName = this.signupForm.controls['lname'].value;
    this.user.phoneNumber = this.signupForm.controls['phoneno'].value;

    try {
      const res: any = await lastValueFrom(this.apiservice.signUp(this.user));
      this.result = res;
      console.log(this.result);

      // Go to homepage if success
      if (this.result.status == 'success') {
        this.isLoading = false;
        this.router.navigate(['/login']);
        return;
      } else {
        this.isLoading = false;
        this.isError = true;
      }
    } catch (err) {
      this.isLoading = false;
      this.isError = true;
    }
  }
}
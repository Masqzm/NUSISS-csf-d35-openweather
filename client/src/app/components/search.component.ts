import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { WeatherService } from '../weather.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CITIES, WeatherInfo } from '../models';

@Component({
  selector: 'app-search',
  standalone: false,
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  private router = inject(Router)

  private fb = inject(FormBuilder)
  protected form!: FormGroup

  cities: string[] = []

  ngOnInit(): void {
    this.form = this.fb.group({
      city: this.fb.control<string>('', [ Validators.required ])
    })
    
    this.cities = CITIES  
  }

  add() {
    const city = this.form.value.city
    console.info('>>>> city: ', city)

    // Check if there's alrd this city added
    // .some() iterates thru the array & returns true if any element matches the condition
    if(!this.cities.some(cIter => cIter.toLowerCase() === city.toLowerCase()))    
      this.cities.push(city)
    else
      alert("City already added!")

    this.form.reset()
  }
}

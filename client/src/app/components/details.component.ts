import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { WeatherInfo } from '../models';
import { WeatherService } from '../weather.service';

@Component({
  selector: 'app-details',
  standalone: false,
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent implements OnInit, OnDestroy {
  private router = inject(Router)
  private activatedRoute = inject(ActivatedRoute)
  private weatherSvc = inject(WeatherService)

  protected city: string = ''   // city name

  protected subParams!: Subscription

  protected result$ !: Promise<WeatherInfo>
  //protected result!: WeatherInfo

  ngOnInit(): void {
    this.subParams = this.activatedRoute.params.subscribe(
      params => {
        console.info('>>> params: ', params)
        this.city = params['city']
      }
    ) 

    this.search(this.city)
  }
  ngOnDestroy(): void {
    console.info('>>> unsubscribing...')
    this.subParams.unsubscribe()
  }

  search(city: string) {
    this.result$ = this.weatherSvc.searchAsPromise(city)
    // this.result$.then(
    //   result => {
    //     console.info('>>> result: ', result)
    //   }
    // )
  }
}

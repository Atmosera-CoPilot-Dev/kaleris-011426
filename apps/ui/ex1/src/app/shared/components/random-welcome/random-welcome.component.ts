import { Component, OnInit } from '@angular/core';
import { WelcomeService } from 'src/app/core/services/welcome.service';

@Component({
  selector: 'app-random-welcome',
  templateUrl: './random-welcome.component.html',
  styleUrls: ['./random-welcome.component.scss']
})
export class RandomWelcomeComponent implements OnInit {
  welcomeMessage: string;

  constructor(private welcomeService: WelcomeService) {}

  ngOnInit(): void {
    this.welcomeMessage = this.welcomeService.getRandomWelcomeMessage();
  }
}
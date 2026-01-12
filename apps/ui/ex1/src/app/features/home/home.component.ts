import { Component } from '@angular/core';
import { WelcomeService } from 'src/app/core/services/welcome.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  welcomeMessage: string;

  constructor(private welcomeService: WelcomeService) {
    this.welcomeMessage = this.welcomeService.getRandomWelcomeMessage();
  }
}
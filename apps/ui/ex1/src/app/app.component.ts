export class AppComponent {
  title = 'Random Welcome Angular';
  welcomeMessage: string;

  constructor(private welcomeService: WelcomeService) {
    this.welcomeMessage = this.welcomeService.getRandomWelcomeMessage();
  }
}
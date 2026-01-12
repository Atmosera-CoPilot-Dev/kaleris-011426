export class WelcomeService {
    private messages: string[] = [
        "Welcome to our application!",
        "Hello! We're glad to have you here.",
        "Greetings! Enjoy your stay.",
        "Hi there! Hope you find what you're looking for.",
        "Welcome aboard! Let's get started."
    ];

    getRandomWelcomeMessage(): string {
        const randomIndex = Math.floor(Math.random() * this.messages.length);
        return this.messages[randomIndex];
    }
}
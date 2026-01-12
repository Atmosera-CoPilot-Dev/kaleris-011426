# Welcome Randomizer (React + Vite + TS)

A modular React app that displays a random welcome message each time it loads. Designed for easy expansion with a clean separation of components, services, and configuration.

## Structure
- `src/components/WelcomeMessage.tsx`: UI component rendering the message
- `src/services/messageService.ts`: Random selection logic with injectable provider
- `src/config/messages.ts`: Centralized list of messages
- `test/messageService.test.ts`: Vitest unit tests


## Extending
- Add new messages in `src/config/messages.ts`
- Create new components in `src/components` and wire them via `src/App.tsx`
- Add new services under `src/services` for business logic
- Provide different message providers by implementing the same interface as `messageService.ts`
- Add end-to-end tests using Cypress or Playwright.
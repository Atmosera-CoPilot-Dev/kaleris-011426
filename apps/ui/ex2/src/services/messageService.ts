import { messages } from '../config/messages';

export type MessageProvider = () => string[];

export function getRandomMessage(provider: MessageProvider = () => messages): string {
  const list = provider();
  if (!list || list.length === 0) return 'Welcome!';
  const idx = Math.floor(Math.random() * list.length);
  return list[idx];
}

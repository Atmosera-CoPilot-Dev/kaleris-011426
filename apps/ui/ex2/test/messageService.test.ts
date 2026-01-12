import { describe, expect, it } from 'vitest';
import { getRandomMessage } from '../src/services/messageService';

describe('getRandomMessage', () => {
  it('returns fallback when provider empty', () => {
    const msg = getRandomMessage(() => []);
    expect(msg).toBe('Welcome!');
  });

  it('returns one of provided messages', () => {
    const pool = ['A', 'B', 'C'];
    const msg = getRandomMessage(() => pool);
    expect(pool).toContain(msg);
  });
});

import { useMemo } from 'react';
import { getRandomMessage } from '../services/messageService';

interface WelcomeMessageProps {
  provider?: () => string[];
}

export default function WelcomeMessage({ provider }: WelcomeMessageProps) {
  const message = useMemo(() => getRandomMessage(provider), [provider]);

  return (
    <section style={{
      display: 'grid',
      placeItems: 'center',
      minHeight: '100vh',
      fontFamily: 'system-ui, sans-serif',
    }}>
      <div style={{
        padding: '2rem',
        borderRadius: '12px',
        boxShadow: '0 6px 24px rgba(0,0,0,0.1)',
        background: 'white',
        maxWidth: '720px',
        textAlign: 'center',
      }}>
        <h1 style={{ margin: 0, fontSize: '2rem' }}>{message}</h1>
        <p style={{ color: '#555' }}>
          Reload the page to see another message.
        </p>
      </div>
    </section>
  );
}

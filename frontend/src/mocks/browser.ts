import { setupWorker } from 'msw';
import { handlers } from './gitlab';

export const worker = setupWorker(...handlers);

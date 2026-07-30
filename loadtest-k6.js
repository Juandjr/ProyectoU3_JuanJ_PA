import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    saturacion: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: '30s', target: 50 },
        { duration: '30s', target: 150 },
        { duration: '30s', target: 300 },
        { duration: '30s', target: 500 },
      ],
      gracefulRampDown: '0s',
    },
  },
};

export default function () {
  const response = http.get('http://localhost:8080/items');
  check(response, { 'status is 200': (r) => r.status === 200 });
  sleep(1);
}

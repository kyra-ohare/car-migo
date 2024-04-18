import { appConstants } from '../constants';

export const testConstants = {
  baseUrl: appConstants.serverDomain + ':' + appConstants.serverPort + '/v1',
  firstName: 'Mary',
  lastName: 'Green',
  dob: '1990-06-30T00:00:00Z',
  phoneNumber: '0286579635',

  validEmail: 'mary.green@example.com',
  notFoundEmail: 'foo@example.com',
  conflictEmail: 'boo@example.com',
  validPassword: 'Pass1234!',
  invalidPassword: 'noooooo',
  jwtToken:
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c',
  serverDown: 'boooooo',
};

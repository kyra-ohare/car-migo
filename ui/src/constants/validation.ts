const thisPasswordMinSize = 8;
const thisPasswordMaxSize = 65;

export const validation = {
  EMAIL_MIN_SIZE: 5,
  EMAIL_MAX_SIZE: 100,
  INVALID_EMAIL: 'Invalid email format.',
  NO_EMPTY_EMAIL: 'Email must not be empty.',
  NO_EMPTY_PASSWORD: 'Password must not be empty.',
  GENERIC_ERROR_MSG: "Something went wrong! Sorry. It's not you, it's us!",
  PASSWORD_MIN_SIZE: thisPasswordMinSize,
  PASSWORD_MAX_SIZE: thisPasswordMaxSize,
  PASSWORD_RULE: /^(?=.*[A-Z])(?=.*[a-z0-9@#$%^&+=!?]).{8,65}$/,
  VALID_PASSWORD_MESSAGE:
    'Password must be between ' +
    thisPasswordMinSize +
    ' and ' +
    thisPasswordMaxSize +
    ' characters and at least 2 of the following: alphanumeric characters and/or one special character ( @#$%^&+=!? ) and/or one capital letter.',
};

import * as Yup from 'yup';
import { validation } from '../../constants';

export const signInValidationSchema = Yup.object().shape({
  email: Yup.string()
    .email(validation.INVALID_EMAIL)
    .min(validation.EMAIL_MIN_SIZE)
    .max(validation.EMAIL_MAX_SIZE)
    .required(validation.NO_EMPTY_EMAIL),
  password: Yup.string()
    .min(validation.PASSWORD_MIN_SIZE)
    .max(validation.PASSWORD_MAX_SIZE)
    .required(validation.NO_EMPTY_PASSWORD),
});
